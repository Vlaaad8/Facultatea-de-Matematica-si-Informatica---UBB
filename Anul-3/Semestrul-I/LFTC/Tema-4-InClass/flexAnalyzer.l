%{
    #include "parser.tab.h"
    extern int yylex();
%}

%option noyywrap

%%

\(          { return LBR; }
\)          { return RBR; }
\* { return OP; }
[0-9]+      { return DIGIT; }
[ \t]       ;
\*{2,}[0-9]+  { printf("Eroare lexicala:: %s\n", yytext); }
[0-9]\*{2,}  { printf("Eroare lexicala:: %s\n", yytext); }
\n          { return 0; }
.           { printf("Eroare lexicala:: %s\n", yytext);
               return yytext[0]; }

%%