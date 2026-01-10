#include <iostream>

#include "header/Gramatic.h"
#include "header/SyntacticAnalyzer.h"

using namespace std;

int main() {

    Gramatic g;
    g.loadFromFile("gramatic.txt");

    SyntacticAnalyzer sa(g);
    sa.runFirstAndFollow();
    sa.buildCanonicalCollection();
    sa.buildParsingTable();

    std::vector<std::string> input = {"x", "x" , "b"};
    sa.parse(input);

    return 0;
}