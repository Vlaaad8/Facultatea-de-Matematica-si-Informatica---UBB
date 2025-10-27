//
// Created by vladb on 20/10/2025.
//

#ifndef TEMA_1_ATOM_GENERATOR_H
#define TEMA_1_ATOM_GENERATOR_H
#include <string>
#include <vector>
#include <map>

using namespace std;
class atom_generator {
public:
    static vector<string> atom_list();

    static map<string,int> constantList();
};


#endif //TEMA_1_ATOM_GENERATOR_H