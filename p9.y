%{
#include <stdio.h>
int yylex();
void yyerror(const char *s);
int valid = 1;
%}

%token ID ELSE THEN B A

%%

S:  ID E THEN S Sp  { }
  | A              { }
  ;

Sp: ELSE S         { }
  | /* epsilon */  { }
  ;

E:  B              { }
  ;

%%

void yyerror(const char *s) {
    valid = 0;
    printf("Invalid string\n");
}

int main() {
    yyparse();
    if(valid) {
        printf("Valid string\n");
    }
    return 0;
}