%{
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define TS_SIZE 100

FILE *fip_file;
FILE *errors_file;
FILE *tsCT_file;
FILE *tsID_file;

char* tsID[100];
char* tsCT[100];

int indexID = -1;
int indexCT = -1;

int currentLine = 1;

int addCT(char* constant){
    for(int i = 0; i <= indexCT; i++) {
        if(strcmp(tsCT[i], constant) == 0) {
            return i;
        }
    }
    if(indexCT < TS_SIZE){
        indexCT++;
        tsCT[indexCT] = strdup(constant);
    }
    return indexCT;
}

int addID(char* identifier){
    for(int i = 0; i <= indexID; i++) {
        if(strcmp(tsID[i], identifier) == 0) {
            return i;
        }
    }
    if(indexID < TS_SIZE){
        indexID++;
        tsID[indexID] = strdup(identifier);
    }
    return indexID;
}

struct ConstantTable{
    char atom[10];
    int code;
};

struct ConstantTable table[] = {
    {"CT", 0},
    {"ID", 1},
    {"+", 2},
    {"-", 3},
    {"/", 4},
    {"*", 5},
    {"%", 6},
    {"==", 7},
    {"<", 8},
    {">", 9},
    {"!=", 10},
    {"=", 11},
    {";", 12},
    {",", 13},
    {"\"", 14},
    {"#", 15},
    {"}", 16},
    {"{", 17},
    {")", 18},
    {"(", 19},
    {"if", 20},
    {"else", 21},
    {"while", 22},
    {"int", 23},
    {"float", 24},
    {"string", 25},
    {"struct", 26},
    {"cin", 27},
    {"cout", 28},
    {">>", 29},
    {"<<", 30},
    {"include", 31},
    {"namespace", 32},
    {"iostream", 33},
    {"std", 34},
    {"using", 35}
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


ID_NECONFORM [0-9][a-zA-Z0-9]+
ID        [a-zA-Z][a-zA-Z0-9]*
CT_FLOAT  [0-9]+\.[0-9]+
CT_INT    0|[1-9]+[0-9]*
CT_STRING \"[^"\n]*\"
STRING_NEINCHIS \"[^"\n]*
%%

\n      { currentLine++; }
[ \t]   { }

{ID_NECONFORM} {
    int code = isConstant(yytext);
    if(code != -1)
        fprintf(fip_file,"%d -\n", code);
    else
        fprintf(errors_file,"Eroare la linia %d: %s\n",currentLine,yytext);
}
{ID} {
    int code = isConstant(yytext);
    if(code != -1){
        fprintf(fip_file, "%d -\n", code);
    } else {
        int fipCode = isConstant("ID");
        int tsCode = addID(yytext);
        fprintf(fip_file, "%d %d\n", fipCode, tsCode);
    }
}

{CT_INT} {int code = isConstant(yytext);
    if(code != -1){
        fprintf(fip_file, "%d -\n", code);
    } else {
        int fipCode = isConstant("ID");
        int tsCode = addCT(yytext);
        fprintf(fip_file, "%d %d\n", fipCode, tsCode);
    }
}

{CT_FLOAT} {int code = isConstant(yytext);
    if(code != -1){
        fprintf(fip_file, "%d -1\n", code);
    } else {
        int fipCode = isConstant("ID");
        int tsCode = addCT(yytext);
        fprintf(fip_file, "%d %d\n", fipCode, tsCode);
    }
}
{CT_STRING} {int code = isConstant(yytext);
    if(code != -1){
        fprintf(fip_file, "%d -1\n", code);
    } else {
        int fipCode = isConstant("CT");
        int tsCode = addCT(yytext);
        fprintf(fip_file, "%d %d\n", fipCode, tsCode);
    }
}
{STRING_NEINCHIS} {
    int code = isConstant(yytext);
    if(code != -1)
        fprintf(fip_file,"%d -\n", code);
    else
        fprintf(errors_file,"Eroare la linia %d: %s\n",currentLine,yytext);
}


"!=" {fprintf(fip_file, "%d",isConstant(yytext));}
"<<" {fprintf(fip_file, "%d",isConstant(yytext));}
">>" {fprintf(fip_file, "%d",isConstant(yytext));}
"==" {fprintf(fip_file, "%d",isConstant(yytext));}

. {
    int code = isConstant(yytext);
    if(code != -1)
        fprintf(fip_file,"%d -\n", code);
    else
        fprintf(errors_file,"Eroare la linia %d: %s\n",currentLine,yytext);
}

%%

int main(){
    yyin = fopen("program.txt", "r");
    if(!yyin){
        printf("NU AM GASIT program.txt!\n");
        return 1;
    }

    fip_file = fopen("FIP/FIP.txt","w");
    errors_file = fopen("FIP/errors.txt","w");
    tsCT_file = fopen("Tables/tsCT.txt","w");
    tsID_file = fopen("Tables/tsID.txt","w");

    yylex();

    for(int i = 0; i <= indexCT; i++)
        fprintf(tsCT_file, "%d %s\n", i, tsCT[i]);

    for(int i = 0; i <= indexID; i++)
        fprintf(tsID_file, "%d %s\n", i, tsID[i]);

    fclose(yyin);
    fclose(fip_file);
    fclose(tsCT_file);
    fclose(tsID_file);
    fclose(errors_file);

    return 0;
}