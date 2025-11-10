#include "../header/atom_generator.h"
#include <vector>
#include <map>
#include <fstream>
#include <iostream>
using namespace std;

vector<string> atom_generator::file_list(){
    vector<string> atoms;
    ifstream in("Partea-2/program.txt");
    if (!in.is_open()) {
        cout << "Error in opening input file" << endl;
        exit(0);
    }
    string c;
    while (getline(in, c)) {
       atoms.push_back(c);
    }
    return atoms;
}

map<string ,int> atom_generator::constantList() {
    ifstream inC("Partea-2/constantTable.txt");
    map<string, int> res;
    if (!inC.is_open()) {
        cout << "Error in opening Vocabulary file" << endl;
        exit(1);
    }
    string line;
    while (getline(inC, line)) {
        int idx = line.find(' ');
        if (idx != string::npos) {
            string word = line.substr(0, idx);
            int index = stoi(line.substr(idx + 1));
            res.insert(make_pair(word, index));
        }
    }
    return res;
}

