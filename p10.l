%{
#include "y.tab.h"
#include <stdlib.h>
extern int yylval;
%}

%%

[0-9]+(\.[0-9]+)?   { yylval.val = atof(yytext); return DIGIT; }
"+"                 { return '+'; }
"-"                 { return '-'; }
"*"                 { return '*'; }
"/"                 { return '/'; }
"^"                 { return '^'; }
"("                 { return '('; }
")"                 { return ')'; }
"\n"                { return '\n'; }
[ \t]               { /* ignore whitespace */ }
.                   { return yytext[0]; }

%%

int yywrap() {
    return 1;
}