#include<stdio.h>
int main()

{
    char str[20];
    printf("Enter the string ");
    gets(str);
    int i=0;
    while(str[i]=='a')
    {
        i++;
    }
        if( str[i]=='b' && str[i+1]=='b' && str[i+2]=='\0')
        {
            printf("valid string");
        }
        else
        {
            printf("Invalid string");
        }
       return 0;
    }

  
