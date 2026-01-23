//
// Created by vladb on 20/10/2025.
//

#include "../header/validator.h"
#include <iostream>

using namespace std;

bool validator::validateConstant(const string &number) {
    if (number.empty()) return false;

    int dotCount = 0;

    for (char c : number) {
        if (c == '.') {
            dotCount++;
            if (dotCount > 1) return false;
        } else if (!isdigit(c)) {
            return false;
        }
    }

    return true;
}

bool validator::validateIdentifier(const string &id) {
    if (id.empty()) return false;
    if (!isalpha(id[0])) return false;

    for (size_t i = 1; i < id.size(); ++i) {
        if (!isalpha(id[i]) && !isdigit(id[i])) {
            return false;
        }
    }

    return true;
}

int validator::correctDeclarations(const vector<string> &tokens) {
    size_t i = 0;

    if (tokens.size() < 5) return -1;
    if (tokens[i++] != "#") return -1;
    if (tokens[i++] != "include") return -1;
    if (tokens[i++] != "<") return -1;
    if (tokens[i++] != "iostream") return -1;
    if (tokens[i++] != ">") return -1;

    if (tokens.size() < i+4) return -1;
    if (tokens[i++] != "using") return -1;
    if (tokens[i++] != "namespace") return -1;
    if (tokens[i++] != "std") return -1;
    if (tokens[i++] != ";") return -1;

    if (tokens.size() < i+5) return -1;
    if (tokens[i++] != "int") return -1;
    if (tokens[i++] != "main") return -1;
    if (tokens[i++] != "(") return -1;
    if (tokens[i++] != ")") return -1;
    if (tokens[i++] != "{") return -1;

    return i;
}


bool validator::correctDefinitions(const vector<string> &tokens) {
    for (size_t i = 0; i < tokens.size(); ++i) {
        if(tokens[i] == "int" || tokens[i] == "float") {
            i++;
            if(i >= tokens.size() || !validateIdentifier(tokens[i])) {
                return false;
            }
            i++;


            while(i < tokens.size() && tokens[i] != ";") {
                if(tokens[i] == ",") {
                    i++;
                    if(i >= tokens.size() || !validateIdentifier(tokens[i])) {

                        return false;
                    }
                    i++;
                } else {

                    return false;
                }
            }

            if(i >= tokens.size() || tokens[i] != ";") {

                return false;
            }
        }
    }
    return true;
}