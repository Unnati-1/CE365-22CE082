%{
    #include<stdio.h>
    int c=0,lc=0,uc=0,d=0,ss=0,ec=0;
%}
%%
[a-z] {lc++; c++;}
[A-Z] {uc++; c++;}
[*,;#$@] {ss++; c++;} 
[0-9] {d++; c++;}
. {ec++;}
[\n] {
    if(c>=9 && c<=15 && lc>0 && uc>0 && d>0 && ss>0 && ec==0)
    {
        printf("Valid Password");
    }
    else
    {
        printf("Invalid Password");
    }
        
        d=0;lc=0;uc=0;ss=0;    
    }
%%
int main()
{
    printf("Enter Password:");
    yylex();
    return 0;
}
int yywrap()
{
  return 1;
}