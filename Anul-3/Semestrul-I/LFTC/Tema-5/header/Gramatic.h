//
// Created by vladb on 05/01/2026.
//

#ifndef TEMA_5_GRAMATIC_H
#define TEMA_5_GRAMATIC_H
#include <map>
#include <vector>
#include <string>
#include <set>
using namespace std;

struct Production {
    string terminal;
    vector<string> values;
};


class Gramatic {
private:
    string start;
    set<string> terminals;
    set<string> nonTerminals;
    map<string,

}


#endif //TEMA_5_GRAMATIC_H
