%{
    #include<stdio.h>
%}
%%
[ ] {}
"//".* {printf("single line comment  : %s\n",yytext);}
"/*"([^*]|\*+[^*/])*\*+"/" {printf("multiple line comment  : %s\n",yytext);}
"auto"|"break"|"case"|"char"|"const"|"continue"|"default"|"do"|"double"|"else"|"enum"|"extern"|"float"|"for"|"goto"|"if"|"int"|"long"|"register"|"return"|"short"|"signed"|"sizeof"|"static"|"struct"|"switch"|"typedef"|"union"|"unsigned"|"void"|"volatile"|"while" {printf("keyword  : %s\n",yytext);}
[a-zA-Z_][a-zA-Z0-9_]*  {printf("identifier : %s\n",yytext);}
"++"|"--"|"=="|"!="|">="|"<="|"<<"|">>"|"&&"|"%="|"||"|"+"|"-"|"*"|"/"|"="|"%"|"^"|"\&"|"\|"|"<"|">"|"!" {printf("operator : %s\n" ,yytext);}
[0-9]+"\."[0-9]+ {printf("float constant  : %s\n",yytext);}
[0-9]+ {printf("int constant  : %s ,\n",yytext);}
\"\" {printf("string constant  : %s\n",yytext);}
\'.\' {printf("character constant  : %s\n",yytext);}
[{}(),\[\];] {printf("special symbol  : %s\n",yytext);}
. ;
%%
int main()
{
    yyin=fopen("test5.c","r");
    yylex();
    return 0;
}
int yywrap()
{
    return 1;
}