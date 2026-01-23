#ifndef TEMA_1_VALIDATOR_H
#define TEMA_1_VALIDATOR_H
#include <string>
#include <vector>
using namespace std;


class validator {
public:
    static bool validateConstant(const string &number);
    static int correctDeclarations(const vector<string> &tokens);
    static bool validateIdentifier(const string &id);
    static bool correctDefinitions(const vector<string> &tokens);
};
#endif //TEMA_1_VALIDATOR_H
