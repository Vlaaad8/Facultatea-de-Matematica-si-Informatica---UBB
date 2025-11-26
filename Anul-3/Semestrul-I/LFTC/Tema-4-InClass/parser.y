%{
    #include <stdio.h>
    #include <stdlib.h>
    int yylex();
    void yyerror(const char *s);
    extern FILE *yyin;
%}

%token LBR RBR DIGIT OP
%token UNKNOWN_CHAR
%%

input:
      list { printf("se inchid corect\n"); };
list:
    | item list
    ;
item: DIGIT | OP | LBR list RBR;

%%

void yyerror(const char *s) {
    printf("NU se inchid corect\n");
    exit(0);
}

int main() {

    FILE *f = fopen("input.txt", "r");

    if (!f) {
        return 1;
    }

    yyin = f;
    yyparse();

    fclose(f);
    return 0;
}