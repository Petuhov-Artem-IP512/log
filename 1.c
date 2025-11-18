#include <stdio.h>
#include <stdlib.h>
int main(int argc, char* argv[]){
    int m;
    printf("Введите номер месяца \n");
    scanf("%d",&m);
    switch (m)
    {
    case 1: case 2:
        printf("зима, 1 квартал \n");
        break;
    case 3:
        printf("весна, 1 квартал \n");

    case 4: case 5:
        printf("весна, 2 квартал \n");
        break;
    case 6:
        printf("лето, 2 квартал \n");
    case 7: case 8:
        printf("лето, 3 квартал \n");
        break;
    case 9:
        printf("осень, 3 квартал \n");
    case 10: case 11:
        printf("осень, 4 квартал \n");
        break;
    case 12:
        printf("зима, 4 квартал \n");
        break;
    default: printf("\n Error: \n");
        break;
    }

}