%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int char_count = 0, word_count = 0, line_count = 1;

%}

%%

[\n]     { line_count++; char_count++; } 
[ \t]+   { char_count += yyleng; }                       
[^ \t\n]+ { word_count++; char_count += yyleng; }    

%%

int main() {
    char input[1000];  
    printf("Enter the input string:\n");
    fgets(input, sizeof(input), stdin);  

    input[strcspn(input, "\n")] = 0;

    yy_scan_string(input);  
    yylex();  

    printf("Characters: %d\n", char_count);
    printf("Words: %d\n", word_count);
    printf("Lines: %d\n", line_count);

    return 0;
}

int yywrap() {
    return 1;
}
