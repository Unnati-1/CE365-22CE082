%{
#include <stdio.h>
#include <math.h>
int yylex();
void yyerror(const char *s);
double result;
%}

%union {
    double val;
}

%token <val> DIGIT
%type <val> L E T F G

%left '+' '-'
%left '*' '/'
%right '^'

%%

L: E '\n'          { result = $1; printf("%.2f\n", result); }
   ;

E: E '+' T         { $$ = $1 + $3; }
 | E '-' T         { $$ = $1 - $3; }
 | T              { $$ = $1; }
 ;

T: T '*' F         { $$ = $1 * $3; }
 | T '/' F         { if($3 == 0) { yyerror("Division by zero"); YYERROR; } else { $$ = $1 / $3; } }
 | F              { $$ = $1; }
 ;

F: G '^' F         { $$ = pow($1, $3); }
 | G              { $$ = $1; }
 ;

G: '(' E ')'       { $$ = $2; }
 | DIGIT          { $$ = $1; }
 ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Invalid expression: %s\n", s);
}

int main() {
    printf("Enter an arithmetic expression: ");
    yyparse();
    return 0;
}