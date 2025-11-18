#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[]){
    int m;
    printf("Введите год \n");
    scanf("%d",&m);

    int s = (m - 2008) % 12;
    if (s < 0) s += 12;
    switch (s)
    {
    case 0: printf( "крыса \n");
        break;
    case 1: printf( "бык \n");
        break;
    case 2: printf( "тигр \n");
        break;
    case 3: printf( "заяц \n");
        break;
    case 4: printf( "дракон \n");
        break;
    case 5: printf( "змея \n");
        break;
    case 6: printf( "лошадь \n");
        break;
    case 7: printf( "овца \n");
        break;
    case 8: printf( "обезьяна \n");
        break;
    case 9: printf( "петух \n");
        break;
    case 10: printf( "собака \n");
        break;
    case 11: printf( "свинья \n");
        break;
    default:
        break;
    }
}