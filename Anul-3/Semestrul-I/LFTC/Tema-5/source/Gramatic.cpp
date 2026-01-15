//
// Created by vladb on 05/01/2026.
//

#include "../header/Gramatic.h"
#include <fstream>
#include <iostream>
#include <sstream>
#include <algorithm>

void Gramatic::loadFromFile(const string &filename) {
    ifstream in(filename);
    if (!in.is_open()) {
        cerr << "Can't open file " << filename << endl;
    }

    string line;
    int currentId = 1;

    while (getline(in, line)) {
        stringstream ss(line);
        string nonTerminal, arrow, symbol;
        ss >> nonTerminal >> arrow;
        if (startSymbol.empty()) {
            startSymbol = nonTerminal;
        }
        Production production;
        production.id = currentId;
        production.nonTerminal = nonTerminal;
        nonTerminals.insert(nonTerminal);
        while (ss >> symbol) {
            production.rightPart.push_back(symbol);
        }
        productions.push_back(production);

        currentId++;
    }

    enrichGramatic();

    for (const Production &production: productions) {
        for (const string &literal: production.rightPart) {
            if (!nonTerminals.contains(literal)) {
                terminals.insert(literal);
            }
        }
    }
    terminals.insert("$");
    in.close();
}

void Gramatic::enrichGramatic() {
    string enrichStart = startSymbol + "'";

    Production production;
    production.id = 0;
    production.nonTerminal = enrichStart;
    production.rightPart.push_back(startSymbol);

    productions.insert(productions.begin(), production);

    nonTerminals.insert(enrichStart);


    startSymbol = enrichStart;
}


void Gramatic::printGramatic() {
    std::cout << "Simbol Start: " << startSymbol << "\n\nProductii:\n";
    for (const auto &p: productions) {
        cout << p.nonTerminal << " ";
        for (const auto &sym: p.rightPart) {
            cout << sym << " ";
        }
        cout << "\n";
    }

    std::cout << "\nNeterminale: ";
    for (const auto &nt: nonTerminals) std::cout << nt << " ";

    std::cout << "\nTerminale: ";
    for (const auto &t: terminals) std::cout << t << " ";
    std::cout << "\n\n";
}

const vector<Production>& Gramatic::getProductions() const {
    return this->productions;
}

const set<string>& Gramatic::getTerminals() const {
    return this->terminals;
}

const set<string>& Gramatic::getNonTerminals() const {
    return this->nonTerminals;
}
string Gramatic::getStartSymbol() {
    return this->startSymbol;
}
bool Gramatic::isNonTerminal(const string &value) const {
    return this->nonTerminals.contains(value);
}
bool Gramatic::isTerminal(const string &value) const {
    return this->terminals.contains(value);
}
