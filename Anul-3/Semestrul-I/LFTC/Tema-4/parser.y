%{
#include <stdio.h>
#include <stdlib.h>

int yylex(void);
void yyerror(const char *s);
extern int currentLine;
FILE* errors_file;
%}

%token CT ID
%token PLUS MINUS DIV ORI MOD
%token EQ LT GT NEQ ASSIGN
%token POINTCOMMA COMMA QUOTE HASH
%token RBRACE LBRACE RPARANT LPARANT
%token IF ELSE WHILE
%token INT FLOAT STRING STRUCT
%token CIN COUT
%token SHR SHL
%token INCLUDE NAMESPACE IOSTREAM STD USING


%start program

%%

program: HASH INCLUDE LT IOSTREAM GT USING NAMESPACE STD POINTCOMMA INT ID LPARANT RPARANT LBRACE declVar intrComp RBRACE;

declVar: tip listaVar | tip listaVar declVar;
listaVar: ID POINTCOMMA | ID COMMA listaVar;

tip: INT| FLOAT | STRING | STRUCT ID LBRACE declVar RBRACE;

intrComp: intr | intr intrComp;

intr: atribuire | intrIf | intrWhile | intrCitire | intrAfisare;

atribuire: ID ASSIGN operand POINTCOMMA | ID ASSIGN expresie POINTCOMMA;
expresie: operand Operator operand | operand Operator expresie ;
operand: CT | ID ;
intrIf: IF LPARANT conditie RPARANT LBRACE intrComp RBRACE | IF LPARANT conditie RPARANT LBRACE intrComp RBRACE ELSE LBRACE intrComp RBRACE;
intrWhile: WHILE LPARANT conditie RPARANT LBRACE intrComp RBRACE;
conditie: operand | operand operatorRelational operand;
intrCitire: CIN SHR ID POINTCOMMA ;
intrAfisare: COUT SHL ID POINTCOMMA;
Operator: PLUS | MINUS | DIV | ORI | MOD;
operatorRelational: EQ | LT | GT | NEQ | ASSIGN;

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