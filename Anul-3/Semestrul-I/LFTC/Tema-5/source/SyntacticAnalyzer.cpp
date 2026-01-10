//
// Created by vladb on 06/01/2026.
//

#include "../header/SyntacticAnalyzer.h"

#include <iostream>

void SyntacticAnalyzer::calculateFirst() {
    for (const string &terminal: gramatic.getTerminals()) {
        setFIRST[terminal].insert(terminal);
    }

    for (const string &nonTerminal: gramatic.getNonTerminals()) {
        setFIRST[nonTerminal] = set<string>();
    }

    bool modified = true;
    while (modified) {
        modified = false;

        for (const Production &production: gramatic.getProductions()) {
            string nonTerminal = production.nonTerminal;
            int sizeBefore = setFIRST[nonTerminal].size();

            if (production.rightPart.empty() || production.rightPart[0] == "e") {
                setFIRST[nonTerminal].insert("e");
            } else {
                bool allCanBeEmpty = true;

                for (const string &symbol: production.rightPart) {
                    for (const string &fSymbol: setFIRST[symbol]) {
                        if (fSymbol != "e") {
                            setFIRST[nonTerminal].insert(fSymbol);
                        }
                    }


                    if (setFIRST[symbol].find("e") == setFIRST[symbol].end()) {
                        allCanBeEmpty = false;
                        break;
                    }
                }

                if (allCanBeEmpty) setFIRST[nonTerminal].insert("e");
            }

            if (setFIRST[nonTerminal].size() > sizeBefore) {
                modified = true;
            }
        }
    }
    //TODO to remove soon
    for (const auto &entry: setFIRST) {
        // Afisam doar pentru neterminale
        cout << entry.first << ": { ";
        for (const string &item: entry.second) cout << item << " ";
        cout << "}" << endl;
    }
}

set<string> SyntacticAnalyzer::getFirstOfSequence(const vector<string> &sequence) {
    set<string> result;

    if (sequence.empty()) {
        result.insert("e");
        return result;
    }
    bool allCanBeEmpty = true;
    for (const string &value: sequence) {
        set<string> valueFIRST = this->setFIRST[value];
        for (const string &item: valueFIRST) {
            if (item != "e") {
                result.insert(item);
            }
        }
        if (valueFIRST.find("e") == valueFIRST.end()) {
            allCanBeEmpty = false;
            break;
        }
    }
    if (allCanBeEmpty) result.insert("e");
    return result;
}

void SyntacticAnalyzer::calculateFollow() {
    for (const string &nt: gramatic.getNonTerminals()) {
        setFOLLOW[nt] = set<string>();
    }
    setFOLLOW[gramatic.getStartSymbol()].insert("$");

    bool modified = true;
    while (modified) {
        modified = false;

        for (const Production &production: gramatic.getProductions()) {
            string A = production.nonTerminal;
            const vector<string> &rhs = production.rightPart;

            for (size_t i = 0; i < rhs.size(); ++i) {
                string B = rhs[i];

                if (gramatic.isNonTerminal(B)) {
                    int sizeBefore = setFOLLOW[B].size();

                    vector<string> beta;
                    for (size_t j = i + 1; j < rhs.size(); ++j) {
                        beta.push_back(rhs[j]);
                    }

                    if (!beta.empty()) {
                        set<string> firstBeta = getFirstOfSequence(beta);

                        for (const string &f: firstBeta) {
                            if (f != "e") setFOLLOW[B].insert(f);
                        }


                        if (firstBeta.find("e") != firstBeta.end()) {
                            for (const string &f: setFOLLOW[A]) {
                                setFOLLOW[B].insert(f);
                            }
                        }
                    } else {
                        for (const string &f: setFOLLOW[A]) {
                            setFOLLOW[B].insert(f);
                        }
                    }

                    if (setFOLLOW[B].size() > sizeBefore) {
                        modified = true;
                    }
                }
            }
        }
    }
    for (const auto &entry : setFOLLOW) {
        cout << "FOLLOW(" << entry.first << ") = { ";
        for (const string &item : entry.second) cout << item << " ";
        cout << "}" << endl;
    }
}



State SyntacticAnalyzer::closure(State I) {
    State J = I;
    bool modified = true;

    while (modified) {
        modified = false;

        State currentPicture = J;

        for (const auto &item: currentPicture) {
            const Production &production = gramatic.getProductions()[item.productionIndex];

            if (item.pointPosition < production.rightPart.size()) {
                string B = production.rightPart[item.pointPosition];

                if (gramatic.isNonTerminal(B)) {
                    vector<string> betaAndLookAhead;
                    for (int k = item.pointPosition + 1; k < production.rightPart.size(); ++k) {
                        betaAndLookAhead.push_back(production.rightPart[k]);
                    }
                    betaAndLookAhead.push_back(item.lookAhead);

                    set<string> newLookAhead = getFirstOfSequence(betaAndLookAhead);

                    vector<Production> allProductions = gramatic.getProductions();
                    for (int i = 0; i < allProductions.size(); i++) {
                        if (allProductions[i].nonTerminal == B) {
                            for (const string &lookAhead: newLookAhead) {
                                ItemLR newItem;
                                newItem.lookAhead = lookAhead;
                                newItem.productionIndex = i;
                                newItem.pointPosition = 0;

                                if (J.find(newItem) == J.end()) {
                                    modified = true;
                                    J.insert(newItem);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return J;
}

State SyntacticAnalyzer::goTo(State I, string X) {
    State moveSet;

    for (const auto &item: I) {
        const Production &production = gramatic.getProductions()[item.productionIndex];
        if (item.pointPosition < production.rightPart.size() && X == production.rightPart[item.pointPosition]) {
            ItemLR newItem = item;
            newItem.pointPosition++;
            moveSet.insert(newItem);
        }
    }
    return closure(moveSet);
}

void SyntacticAnalyzer::buildCanonicalCollection() {
    ItemLR initialItem;
    initialItem.lookAhead = "$";
    initialItem.productionIndex = 0;
    initialItem.pointPosition = 0;

    State i0 = closure({initialItem});

    states.push_back(i0);

    for (int i = 0; i < states.size(); i++) {
        set<string> allSymbols;

        for (const auto &production: gramatic.getProductions()) {
            for (auto &symbol: production.rightPart) if (symbol != "e") allSymbols.insert(symbol);
            allSymbols.insert(production.nonTerminal);
        }

        for (const string &X: allSymbols) {
            State nextState = goTo(states[i], X);

            if (!nextState.empty()) {
                int foundId = -1;

                for (int j = 0; j < states.size(); j++) {
                    if (states[j] == nextState) {
                        foundId = j;
                        break;
                    }
                }

                if (foundId == -1) {
                    states.push_back(nextState);
                    gotoTable[i][X] = states.size() - 1;
                } else {
                    gotoTable[i][X] = foundId;
                }
            }
        }
    }
}

void SyntacticAnalyzer::buildParsingTable() {
    const auto& allProductions = gramatic.getProductions();

    for (int i = 0; i < states.size(); ++i) {
        for (auto const &[symbol,nextStateId]: gotoTable[i]) {
            if (gramatic.isTerminal(symbol)) {
                string action = "s" + to_string(nextStateId);

                if (actionTable[i].count(symbol) && actionTable[i][symbol] != action) {
                    cerr << "Conflict in starea " << i << " pe simbolul " << symbol << endl;
                }
                actionTable[i][symbol] = action;
            }
        }
        for (const auto &item: states[i]) {
            const Production &production = allProductions[item.productionIndex];

            if (item.pointPosition == production.rightPart.size()) {
                if (production.nonTerminal == gramatic.getStartSymbol()) {
                    if (item.lookAhead == "$") {
                        actionTable[i]["$"] = "acc";
                    }
                } else {
                    string action = "r" + to_string(production.id);
                    if (actionTable[i].count(item.lookAhead) && actionTable[i][item.lookAhead] != action) {
                        cerr << "Conflict detectat in starea " << i << " pe " << item.lookAhead << endl;
                    }
                    actionTable[i][item.lookAhead] = action;
                }
            }
        }
    }
    for (auto const& [stare, row] : actionTable) {
        for (auto const& [simbol, actiune] : row) {
            cout << "Action[I" << stare << ", " << simbol << "] = " << actiune << endl;
        }
    }
}
bool SyntacticAnalyzer::parse(vector<string> input) {

    if (input.back() != "$") input.push_back("$");


    vector<int> stateStack;
    stateStack.push_back(0);

    vector<int> productionSequence;
    size_t currentTokenIdx = 0;

    while (true) {
        int currentState = stateStack.back();
        string currentSymbol = input[currentTokenIdx];

        // Consultam tabelul ACTION
        if (actionTable[currentState].count(currentSymbol)) {
            string action = actionTable[currentState][currentSymbol];

            if (action == "acc") { // ACCEPT [cite: 426]
                cout << "Secventa acceptata!" << endl;
                cout << "Sirul productiilor: ";
                for (int id : productionSequence) cout << id << " ";
                cout << endl;
                return true;
            }
            else if (action[0] == 's') { // SHIFT [cite: 417]
                int nextState = stoi(action.substr(1));
                stateStack.push_back(nextState); // Punem starea pe stiva
                currentTokenIdx++; // Avansam in input
            }
            else if (action[0] == 'r') { // REDUCE [cite: 418]
                int prodId = stoi(action.substr(1));
                Production p;

                // Gasim productia dupa ID
                for(const auto& prod : gramatic.getProductions()) {
                    if(prod.id == prodId) { p = prod; break; }
                }

                // Eliminam 2*lungime elemente din stiva (aici doar starile sunt in vector)
                // Daca ai o stiva de (simbol, stare), elimini p.rhs.size() perechi
                for (size_t i = 0; i < p.rightPart.size(); ++i) {
                    stateStack.pop_back();
                }

                // GOTO: unde mergem dupa reducere
                int stateAfterPop = stateStack.back();
                if (gotoTable[stateAfterPop].count(p.nonTerminal)) {
                    stateStack.push_back(gotoTable[stateAfterPop][p.nonTerminal]);
                    productionSequence.push_back(prodId);
                } else {
                    cerr << "Eroare in GOTO dupa reducere!" << endl;
                    return false;
                }
            }
        } else {
            cerr << "Eroare sintactica: Simbol neasteptat '" << currentSymbol
                 << "' in starea " << currentState << endl;
            return false;
        }
    }
}