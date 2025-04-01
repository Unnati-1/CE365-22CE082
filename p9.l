%{
#include "y.tab.h"
%}

%%

"i"     { return ID; }
"e"     { return ELSE; }
"t"     { return THEN; }
"b"     { return B; }
"a"     { return A; }
[ \t\n] { /* ignore whitespace */ }
.       { return yytext[0]; }

%%

int yywrap() {
    return 1;
}