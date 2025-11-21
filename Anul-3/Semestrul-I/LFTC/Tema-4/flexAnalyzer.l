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
    {"/",        DIV},
    {"*",        ORI},
    {"%",        MOD},
    {"==",       EQ},
    {"<",        LT},
    {">",        GT},
    {"!=",       NEQ},
    {"=",        ASSIGN},
    {";",        POINTCOMMA},
    {",",        COMMA},
    {"\"",       QUOTE},
    {"#",        HASH},
    {"}",        RBRACE},
    {"{",        LBRACE},
    {")",        RPARANT},
    {"(",        LPARANT},
    {"if",       IF},
    {"else",     ELSE},
    {"while",    WHILE},
    {"int",      INT},
    {"float",    FLOAT},
    {"string",   STRING},
    {"struct",   STRUCT},
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
SUFIX       (([uU]?[lL]{0,2})|([lL]{0,2}[uU]?))?
ERR_ID [1-9]+[a-zA-Z][a-zA-Z0-9]*
ID        [a-zA-Z][a-zA-Z0-9]*
CT_HEXA   0[xX][0-9a-fA-f]+{SUFIX}
ERR_BINARA 0[bB][01]*[2-9][0-9a-zA-Z]*{SUFIX}
CT_BINARA 0[bB][01]+{SUFIX}
ERR_OCTAL 0[0-7]+[8-9][0-9a-zA-Z]*{SUFIX}
CT_OCTAL 0[0-7]+{SUFIX}
ERR_FLOAT     [0-9]*\.[0-9]*\.[0-9.]*{SUFIX}
CT_FLOAT  [0-9]+\.[0-9]+([eE][+-]?[0-9]+)?{SUFIX}
CT_INT    0|[1-9]+[0-9]*{SUFIX}
CT_STRING \"[^"\n]*\"
STRING_NEINCHIS \"[^"\n]*
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


{CT_HEXA} {int code = isConstant(yytext);
    if(code != -1){
         return code;
    } else {
        return CT;
    }
}
{ERR_BINARA} {
int code = isConstant(yytext);
    if(code != -1)
       return code;
    else
        fprintf(errors_file,"%d Lexical error: %s\n",currentLine,yytext);
}
{CT_BINARA} {int code = isConstant(yytext);
    if(code != -1){
        return code;
    } else {
        return CT;
    }
}
{ERR_OCTAL} {
int code = isConstant(yytext);
    if(code != -1)
       return code;
    else
        fprintf(errors_file,"%d Lexical error: %s\n",currentLine,yytext);
}
{CT_OCTAL} {int code = isConstant(yytext);
    if(code != -1){
        return code;
    } else {
        return CT;
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
{CT_STRING} {int code = isConstant(yytext);
    if(code != -1){
       return code;
    } else {
        return CT;
    }
}
{STRING_NEINCHIS} {
    int code = isConstant(yytext);
    if(code != -1)
      return code;
    else
        fprintf(errors_file,"%d Lexical error: %s\n",currentLine,yytext);
}


"!=" {return NEQ;}
"<<" {return SHL;}
">>" {return SHR;}
"==" {return EQ;}

. {
    int code = isConstant(yytext);
    if(code != -1)
       return code;
    else
        fprintf(errors_file,"%d Lexical error: %s\n",currentLine,yytext);
}

%%
