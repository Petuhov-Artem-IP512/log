#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[]){
    int C;
    printf("Введите число в диапазоне [-9; 9] \n");
    scanf("%d",&C);
    //if(C >= -9 && C<= 9){
    if (C < 0 && C >= -9){
        printf("минус");
        C = -C;
    }
    switch (C)
    {
    case 0: printf(" ноль \n");
        break;
    case 1: printf(" один \n");
        break;
    case 2: printf(" два \n");
        break;
    case 3: printf(" три \n");
        break;
    case 4: printf(" четыре \n");
        break;
    case 5: printf(" пять \n");
        break;
    case 6: printf(" шесть \n");
        break;
    case 7: printf(" семь \n");
        break;
    case 8: printf(" восемь \n");
        break;
    case 9: printf(" девять \n");
        break;
    default: printf("\n Ошибка \n Повторите ввод \n");
        break;
    }
}


