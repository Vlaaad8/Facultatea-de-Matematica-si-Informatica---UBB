%{
#include <stdio.h>
#include <stdlib.h>

int yylex(void);
void yyerror(const char *s);
extern int currentLine;
FILE* errors_file;
%}

%token CT ID
%token PLUS MINUS ORI
%token LT GT ASSIGN
%token POINTCOMMA COMMA QUOTE HASH
%token RBRACE LBRACE RPARANT LPARANT
%token INT FLOAT
%token CIN COUT
%token SHR SHL
%token INCLUDE NAMESPACE IOSTREAM STD USING


%start program

%%

program: HASH INCLUDE LT IOSTREAM GT USING NAMESPACE STD POINTCOMMA INT ID LPARANT RPARANT LBRACE declVar intrComp RBRACE;

declVar: tip listaVar | tip listaVar declVar;
listaVar: ID POINTCOMMA | ID COMMA listaVar;

tip: INT| FLOAT;

intrComp: intr | intr intrComp;

intr: atribuire | intrCitire | intrAfisare;

atribuire: ID ASSIGN operand POINTCOMMA | ID ASSIGN expresie POINTCOMMA;
expresie: operand Operator operand | operand Operator expresie ;
operand: CT | ID ;

conditie: operand | operand operatorRelational operand;

intrCitire: CIN SHR ID POINTCOMMA ;
intrAfisare: COUT SHL ID POINTCOMMA;

Operator: PLUS | MINUS | ORI ;
operatorRelational: LT | GT | ASSIGN;

%%
void yyerror(const char *s)
{    extern char* yytext;

    fprintf(errors_file, "%d Syntactic error near %s\n", currentLine, yytext);
}

int main(void)
{
    extern FILE *yyin;

      yyin = fopen("program.txt", "r");
    if (!yyin) {
        printf("NU AM GASIT program.txt!\n");
        return 1;
    }

    errors_file = fopen("errors.txt","w");
    if (!errors_file) {
        printf("NU AM PUTUT DESCHIDE errors.txt\n");
        return 1;
    }
    if (yyparse() == 0) {
        printf("Correct syntactic program.\n");
    } else {
        printf("Incorrect syntactic program.\n");
    }
    fclose(yyin);
    fclose(errors_file);
    return 0;
}