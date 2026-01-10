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
    int id;
    string nonTerminal;
    vector<string> rightPart;
};


class Gramatic {
private:
    string startSymbol;
    set<string> terminals;
    set<string> nonTerminals;
    vector<Production> productions;
public:
    void loadFromFile(const string& filename);
    void enrichGramatic();
    void printGramatic();

    const vector<Production>& getProductions() const;
    const set<string>& getTerminals() const;
    const set<string>& getNonTerminals() const;
    string getStartSymbol();
    bool isNonTerminal(const string &value) const;
    bool isTerminal(const string& value) const;
};


#endif //TEMA_5_GRAMATIC_H
