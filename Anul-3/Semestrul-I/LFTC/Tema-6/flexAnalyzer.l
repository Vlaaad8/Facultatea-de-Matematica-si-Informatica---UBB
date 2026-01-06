%{
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "parser.tab.h"
extern int yylex();
extern FILE *errors_file;
int currentLine = 1;

struct ConstantTable{
    char atom[10];
    int code;
};

struct ConstantTable table[] = {
    {"CT",       CT},
    {"ID",       ID},
    {"+",        PLUS},
    {"-",        MINUS},
    {"*",        ORI},
    {"=",        ASSIGN},
    {";",        POINTCOMMA},
    {",",        COMMA},
    {"\"",       QUOTE},
    {"#",        HASH},
    {"}",        RBRACE},
    {"{",        LBRACE},
    {")",        RPARANT},
    {"(",        LPARANT},
    {"int",      INT},
    {"float",    FLOAT},
    {"cin",      CIN},
    {"cout",     COUT},
    {">>",       SHR},
    {"<<",       SHL},
    {"include",  INCLUDE},
    {"namespace",NAMESPACE},
    {"iostream", IOSTREAM},
    {"std",      STD},
    {"using",    USING}
};

int tableSize = sizeof(table) / sizeof(table[0]);

int isConstant(const char* atom){
    for(int i = 0; i < tableSize; i++){
        if(strcmp(table[i].atom, atom) == 0)
            return table[i].code;
    }
    return -1;
}

%}

%option noyywrap
ERR_ID [1-9]+[a-zA-Z][a-zA-Z0-9]*
ID        [a-zA-Z][a-zA-Z0-9]*
ERR_FLOAT  [0-9]*\.[0-9]*\.[0-9.]*
CT_FLOAT  [1-9]*[0-9]\.[0-9]+
CT_INT    0|[1-9]+[0-9]*
%%

\n      { currentLine++; }
[ \t]   { }


{ERR_ID} {
int code = isConstant(yytext);
    if(code != -1)
        return code;
    else
        fprintf(errors_file,"%d Lexical error: %s\n",currentLine,yytext);
}
{ID} {
    int code = isConstant(yytext);
    if(code != -1){
        return code;
    } else {
        return ID;
    }
}

{ERR_FLOAT} {
int code = isConstant(yytext);
    if(code != -1)
       return code;
    else
        fprintf(errors_file,"%d Lexical error: %s\n",currentLine,yytext);
}
{CT_FLOAT} {int code = isConstant(yytext);
    if(code != -1){
        return code;
    } else {
        return CT;
    }
}
{CT_INT} {int code = isConstant(yytext);
    if(code != -1){
        return code;
    } else {
        return CT;
    }
}

"<<" {return SHL;}
">>" {return SHR;}

. {
    int code = isConstant(yytext);
    if(code != -1)
       return code;
    else
        fprintf(errors_file,"%d Lexical error: %s\n",currentLine,yytext);
}

%%
