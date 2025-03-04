%{
    #include<stdio.h>
   
%}
%%
charusat {printf("university");}
. {printf("%c",yytext[0]);}
%%
int main()
{
    printf("Enter string:");
    yylex();
    return 0;
}
int yywrap()
{
    return 1;
}
