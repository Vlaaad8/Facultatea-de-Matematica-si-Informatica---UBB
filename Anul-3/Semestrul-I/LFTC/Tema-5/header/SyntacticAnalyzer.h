//
// Created by vladb on 06/01/2026.
//

#ifndef TEMA_5_SYNTACTICANALYZER_H
#define TEMA_5_SYNTACTICANALYZER_H
#include <utility>

#include "Gramatic.h"

struct ItemLR {
    int productionIndex;
    int pointPosition;
    string lookAhead;

    bool operator<(const ItemLR& other) const {
        if (productionIndex != other.productionIndex) return productionIndex < other.productionIndex;
        if (pointPosition != other.pointPosition) return pointPosition < other.pointPosition;
        return lookAhead < other.lookAhead;
    }

    bool operator==(const ItemLR& other) const {
        return productionIndex == other.productionIndex && pointPosition == other.pointPosition && lookAhead == other.lookAhead;
    }
};

typedef set<ItemLR> State;

class SyntacticAnalyzer {
private:
    Gramatic gramatic;
    map<string, set<string> > setFIRST;
    map<string, set<string> > setFOLLOW;

    vector<State> states;

    map<int, map<string, int>> gotoTable;
    map<int, map<string, string>> actionTable;

    void calculateFirst();
    void calculateFollow();

    State closure(State I);
    State goTo(State I, string X);

    void buildCanonicalCollection();
    void buildParsingTable();
    set<string> getFirstOfSequence(const vector<string> &sequence);

public:
    explicit SyntacticAnalyzer(Gramatic gramatic): gramatic(std::move(gramatic)) {}

    bool parse(vector<string> input);

    void initialize() {
        calculateFirst();
        calculateFollow();
        buildCanonicalCollection();
        buildParsingTable();
    }

};


#endif //TEMA_5_SYNTACTICANALYZER_H
