%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int yylex(void);
void yyerror(const char *s);
extern int currentLine;
FILE* errors_file;
FILE* output_file;
%}

%code requires {
    #include "attribute.h"
}

%union{
char varname[64];
attributes attrib;
}



%token <varname> CT ID
%token PLUS MINUS ORI
%token LT GT ASSIGN
%token POINTCOMMA COMMA QUOTE HASH
%token RBRACE LBRACE RPARANT LPARANT
%token INT FLOAT
%token CIN COUT
%token SHR SHL
%token INCLUDE NAMESPACE IOSTREAM STD USING


%type <attrib> expresie termen factor

%start program

%%

program: HASH INCLUDE LT IOSTREAM GT USING NAMESPACE STD POINTCOMMA INT ID LPARANT RPARANT LBRACE {
    fprintf(output_file,"bits 32\n\nglobal start\n\n");
    fprintf(output_file,"extern exit,printf,scanf\n");
    fprintf(output_file,"import exit msvcrt.dll\n");
    fprintf(output_file,"import printf msvcrt.dll\n");
    fprintf(output_file,"import scanf msvcrt.dll\n");
    fprintf(output_file,"\nsegment .data use32 class=data\n");
    fprintf(output_file,"\tformat_in db \"%%d\", 0\n");
    fprintf(output_file,"\tformat_out db \"%%d\",10 ,0\n");
    fprintf(output_file,"\tmessage  db \"n=\",10, 0\n");
} declVar {
        fprintf(output_file,"\nsegment code use32 class=code\n");
        fprintf(output_file, "\tglobal ..start\n");
        fprintf(output_file, "..start:\n\n");
    } intrComp RBRACE;

declVar: tip listaVar;
listaVar: ID POINTCOMMA {
    fprintf(output_file,"\t%s dd 0\n",$1);
} | ID COMMA listaVar {
    fprintf(output_file,"\t%s dd 0\n",$1);
};

tip: INT | FLOAT;

intrComp: intr | intr intrComp;

intr: atribuire | intrCitire | intrAfisare;

atribuire: ID ASSIGN expresie POINTCOMMA {
    fprintf(output_file,"%s", $3.cod);
    fprintf(output_file,"pop eax\nmov [%s], eax\n", $1);
};

expresie: expresie PLUS termen {sprintf($$.cod, "%s%spop ebx\npop eax\nadd eax, ebx\npush eax\n",$1.cod, $3.cod); }
            | expresie MINUS termen {sprintf($$.cod, "%s%spop ebx\npop eax\nsub eax, ebx\npush eax\n",$1.cod,$3.cod); }
            | termen {strcpy($$.cod,$1.cod);};

termen: factor { strcpy($$.cod, $1.cod); }
      | termen ORI factor { sprintf($$.cod, "%s%spop ebx\npop eax\nimul eax, ebx\npush eax\n", $1.cod, $3.cod); }
      ;

factor: ID { sprintf($$.cod, "push dword [%s]\n", $1); }
      | CT { sprintf($$.cod, "push %s\n", $1); }
      | LPARANT expresie RPARANT { strcpy($$.cod, $2.cod); };

intrCitire: CIN SHR ID POINTCOMMA { fprintf(output_file,"push dword message\ncall [printf]\nadd esp,4*1\npush %s\npush format_in\ncall [scanf]\nadd esp,8\n", $3); };
intrAfisare: COUT SHL ID POINTCOMMA { fprintf(output_file, "push dword [%s]\npush format_out\ncall [printf]\nadd esp,8\n", $3); };


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
        printf("Error at program.txt!\n");
        return 1;
    }

    errors_file = fopen("errors.txt","w");
    if (!errors_file) {
        printf("Error at errors.txt\n");
        return 1;
    }

    output_file = fopen("translation.asm","w");
    if(!output_file){
        printf("Error at translation.asm\n");
        return 1;
    }
    if (yyparse() == 0) {
        fclose(yyin);
        fclose(errors_file);
        fclose(output_file);

        system("nasm -f obj translation.asm -o translation.obj");
        system("alink -oPE -subsys con translation.obj");
        system("start ollydbg translation.exe");
        printf("Correct syntactic program.\n");

    } else {
        printf("Incorrect syntactic program.\n");
    }

    return 0;
}