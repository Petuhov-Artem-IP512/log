import java.io.*;
import java.util.*;

public class ClassicIOCacheWithLimit {
    private final Map<String, FileCacheEntry> cache;
    private final int maxSize;

    // Внутренний класс для хранения данных файла в кэше
    private static class FileCacheEntry {
        private final String content;
        private final long lastReadTime;
        private final long lastModifiedTimeAtRead;

        public FileCacheEntry(String content, long lastReadTime, long lastModifiedTimeAtRead) {

            this.content = content;
            this.lastReadTime = lastReadTime;
            this.lastModifiedTimeAtRead = lastModifiedTimeAtRead;
        }

        public String getContent() {
            return content;
        }

        public long getLastReadTime() {
            return lastReadTime;
        }

        public long getLastModifiedTimeAtRead() {
            return lastModifiedTimeAtRead;
        }
    }

    // Конструкторы
    public ClassicIOCacheWithLimit(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, FileCacheEntry> eldest) {
                return size() > ClassicIOCacheWithLimit.this.maxSize;
            }
        };
    }

    public ClassicIOCacheWithLimit() {
        this(100);
    }

    // Основной метод чтения файла
    public String readFile(String filePath) throws IOException {
        File file = new File(filePath);
        
        // Проверка существования файла
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        String absolutePath = file.getAbsolutePath();
        long currentModifiedTime = file.lastModified();
        long currentTime = System.currentTimeMillis();

        // Проверка наличия в кэше и актуальности
        if (cache.containsKey(absolutePath)) {
            FileCacheEntry cachedEntry = cache.get(absolutePath);
            if (isCacheValid(cachedEntry, currentModifiedTime)) {
                // Обновляем время последнего чтения
                cache.put(absolutePath, new FileCacheEntry(
                    cachedEntry.getContent(), 
                    currentTime, 
                    cachedEntry.getLastModifiedTimeAtRead()
                ));
                return cachedEntry.getContent();
            }
        }

        // Чтение файла и обновление кэша
        return updateCache(file, absolutePath, currentModifiedTime);
    }

    // Проверка актуальности кэша
    private boolean isCacheValid(FileCacheEntry cachedEntry, long currentModifiedTime) {
        return cachedEntry.getLastModifiedTimeAtRead() == currentModifiedTime;
    }

    // Обновление кэша
    private String updateCache(File file, String absolutePath, long currentModifiedTime) throws IOException {
        String content = readFileContent(file);
        long currentTime = System.currentTimeMillis();
        
        FileCacheEntry newEntry = new FileCacheEntry(content, currentTime, currentModifiedTime);
        cache.put(absolutePath, newEntry);
        
        return content;
    }

    // Чтение содержимого файла
    private String readFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        char[] buffer = new char[8192]; // 8KB буфер
        
        try (FileReader fileReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(fileReader)) {
            
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, charsRead);
            }
        }
        
        return content.toString();
    }

    // Методы управления кэшем
    public void invalidate(String filePath) {
        File file = new File(filePath);
        cache.remove(file.getAbsolutePath());
    }

    public void invalidateAll() {
        cache.clear();
    }

    public boolean isCached(String filePath) {
        File file = new File(filePath);
        return cache.containsKey(file.getAbsolutePath());
    }

    public int getCachedFilesCount() {
        return cache.size();
    }

    // Методы статистики
    public long getCacheSizeInMemory() {
        long totalSize = 0;
        for (FileCacheEntry entry : cache.values()) {
            totalSize += entry.getContent().length() * 2L; // каждый символ - 2 байта
        }
        return totalSize;
    }

    public void printCacheStats() {
        System.out.println("=== Cache Statistics ===");
        System.out.println("Cached files count: " + getCachedFilesCount());
        System.out.println("Cache size in memory: " + getCacheSizeInMemory() + " bytes");
        System.out.println("Maximum cache size: " + maxSize + " files");
        System.out.println("Cached files:");
        
        for (Map.Entry<String, FileCacheEntry> entry : cache.entrySet()) {
            String filePath = entry.getKey();
            FileCacheEntry cacheEntry = entry.getValue();
            long fileSize = cacheEntry.getContent().length() * 2L;
            Date lastRead = new Date(cacheEntry.getLastReadTime());
            
            System.out.printf("  - %s (size: %d bytes, last read: %s)%n", 
                filePath, fileSize, lastRead);
        }
        System.out.println("========");
    }

    // Тестовый метод
    public static void main(String[] args) {
        try {
            // Создание тестовых файлов
            createTestFiles();
            
            // Создание кэша с небольшим лимитом для демонстрации вытеснения
            ClassicIOCacheWithLimit cache = new ClassicIOCacheWithLimit(2);
            
            System.out.println("=== Testing cache functionality ===");
            
            // Первое чтение файлов - с диска
            long startTime = System.currentTimeMillis();
            String content1 = cache.readFile("test1.txt");
            String content2 = cache.readFile("test2.txt");
            long diskReadTime = System.currentTimeMillis() - startTime;
            
            System.out.println("First read from disk completed in " + diskReadTime + "ms");
            System.out.println("Files in cache: " + cache.getCachedFilesCount());
            
            // Повторное чтение - из кэша
            startTime = System.currentTimeMillis();
            String cachedContent1 = cache.readFile("test1.txt");
            String cachedContent2 = cache.readFile("test2.txt");
            long cacheReadTime = System.currentTimeMillis() - startTime;
            
            System.out.println("Second read from cache completed in " + cacheReadTime + "ms");
            System.out.println("Cache read was " + (diskReadTime / Math.max(cacheReadTime, 1)) + "x faster");
            
            // Проверка наличия файлов в кэше
            System.out.println("test1.txt in cache: " + cache.isCached("test1.txt"));
            System.out.println("test2.txt in cache: " + cache.isCached("test2.txt"));
            
            // Добавление третьего файла для демонстрации вытеснения
            cache.readFile("test3.txt");
            System.out.println("After adding third file:");
            System.out.println("test1.txt in cache: " + cache.isCached("test1.txt")); // Должен быть вытеснен
            System.out.println("test2.txt in cache: " + cache.isCached("test2.txt"));
            System.out.println("test3.txt in cache: " + cache.isCached("test3.txt"));
            
            // Вывод статистики
            cache.printCacheStats();
            
            // Очистка кэша
            cache.invalidate("test2.txt");
            System.out.println("After invalidating test2.txt: " + cache.getCachedFilesCount() + " files in cache");
            
            cache.invalidateAll();
            System.out.println("After complete invalidation: " + cache.getCachedFilesCount() + " files in cache");
            
        } catch (IOException e) {
            System.err.println("Error during testing: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Удаление тестовых файлов
            cleanupTestFiles();
        }
    }
    
    private static void createTestFiles() throws IOException {
        writeToFile("test1.txt", "This is test file 1 content. ".repeat(100));
        writeToFile("test2.txt", "This is test file 2 content. ".repeat(150));
        writeToFile("test3.txt", "This is test file 3 content. ".repeat(200));
    }
    
    private static void writeToFile(String filename, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        }
    }
    
    private static void cleanupTestFiles() {
        new File("test1.txt").delete();
        new File("test2.txt").delete();
        new File("test3.txt").delete();
    }
}
