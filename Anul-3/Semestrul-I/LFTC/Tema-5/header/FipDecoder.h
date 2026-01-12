//
// Created by vladb on 12/01/2026.
//

#ifndef TEMA_5_FIPDECODER_H
#define TEMA_5_FIPDECODER_H
#include <map>
#include <string>
#include <vector>

using namespace std ;

class FipDecoder {
private:
    map<int, string> codeToTerminal = {
        {0, "CT"},
        {1, "ID"},
        {2, "+"},
        {3, "-"},
        {4, "/"},
        {5, "*"},
        {6, "%"},
        {7, "=="},
        {8, "<"},
        {9, ">"},
        {10, "!="},
        {11, "="},
        {12, ";"},
        {13, ","},
        {14, "\""},
        {15, "#"},
        {16, "}"},
        {17, "{"},
        {18, ")"},
        {19, "("},
        {20, "if"},
        {21, "else"},
        {22, "while"},
        {23, "int"},
        {24, "float"},
        {25, "string"},
        {26, "struct"},
        {27, "cin"},
        {28, "cout"},
        {29, ">>"},
        {30, "<<"},
        {31, "include"},
        {32, "namespace"},
        {33, "iostream"},
        {34, "std"},
        {35, "using"},
        {36,"main"}
    };

public:
    vector<string> loadFromFip(const string& fileName);
};


#endif //TEMA_5_FIPDECODER_H
