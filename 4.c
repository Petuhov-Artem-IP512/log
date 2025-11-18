#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[]){
    int m,  d, T;

    printf("введите номер месяца  \n");
    scanf("%d",&m);
    printf("введите номер дня \n");
    scanf("%d",&d);
    switch (m)
    {
    case 1: T = d;
        break;
    case 2:
        if (d > 29){
            printf("в феврале не может быть больше 29 дней");
            return 1;
        }
        T = 31 + d;
        break;
    case 3:
        T = 31 + 28 + d;
        break;
    case 4:
        if (d > 30){
            printf("в апреле не может быть больше 30 дней");
            return 1;
        }
        T = 31 + 28 + 31 + d;
        break;
    case 5:
        T = 31 + 28 + 31 + 30 +d;
        break;
    case 6:
        if (d > 30){
            printf("в июне не может быть больше 29 дней");
            return 1;
        }T = 31+28+31+30+31+30+31+31+30+31+d;
        T = 31+28+31+30+31+d;
        break;
    case 7:
        T = 31+28+31+30+31+30+d;
        break;
    case 8:
        T = 31+28+31+30+31+30+31+d;
        break;
    case 9:
        if (d > 30){
            printf("в сентябре не может быть больше 29 дней");
            return 1;
        }
        T = 31+28+31+30+31+30+31+31+d;
        break;
    case 10:
        T = 31+28+31+30+31+30+31+31+30+d;
        break;
    case 11:
        if (d > 30){
            printf("в ноябре не может быть больше 29 дней");
            return 1;
        }
        T = 31+28+31+30+31+30+31+31+30+31+d;
        break;
    case 12:
        T = 31+28+31+30+31+30+31+31+30+31+30+d;
        break;
    default:
        break;
    }
    printf("введённая дата это %d-й день года \n", T);
}