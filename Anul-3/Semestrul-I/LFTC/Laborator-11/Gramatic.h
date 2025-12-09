//
// Created by vladb on 09/12/2025.
//

#ifndef LABORATOR_11_GRAMATIC_H
#define LABORATOR_11_GRAMATIC_H
#include <map>
#include <string>
#include <vector>

using namespace std;


class Gramatic {
private:
    map<char, vector<string> > rules;

public:
    void  readFromFile(const string &fileName);
};


#endif //LABORATOR_11_GRAMATIC_H
