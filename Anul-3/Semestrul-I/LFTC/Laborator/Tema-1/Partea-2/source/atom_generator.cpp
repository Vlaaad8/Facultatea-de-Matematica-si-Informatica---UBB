//
// Created by vladb on 20/10/2025.
//

#include "../header/atom_generator.h"
#include <vector>
#include <map>
#include <fstream>
#include <iostream>
using namespace std;

vector<string> atom_generator::atom_list(){
    vector<string> atoms;
    ifstream in("program.txt");
    ofstream out("separatedAtoms.txt");
    if (!in.is_open()) {
        cout << "Error in opening input file" << endl;
        exit(0);
    }

    if (!out.is_open()) {
        cout << "Error in opening output file" << endl;
        exit(0);
    }
    string c;
    while (getline(in, c)) {
        string tmp = "";
        for (int i = 0; i < c.size(); i++) {
            if (ispunct(c[i]) && c[i] != '.') {
                if (!tmp.empty()) {
                    out << tmp << endl;
                    atoms.push_back(tmp);
                    tmp.clear();
                }
                if (ispunct(c[i + 1]) && i + 1 < c.size() && ispunct(c[i + 1]) && c[i+1]!=')' and c[i]!='(')  {
                    string newSimbol = string() + c[i] + c[i + 1];
                    out<<newSimbol<<endl;
                    atoms.push_back(newSimbol);
                    i++;
                }
                else {
                    out<<c[i]<<endl;
                    atoms.push_back(string(1,c[i]));
                }

            } else if (isspace(c[i])) {
                if (!tmp.empty()) {
                    out<<tmp<<endl;
                    atoms.push_back(tmp);
                    tmp.clear();
                }
            } else {
                tmp += c[i];
            }
        }
    }
    return atoms;
}

map<string ,int> atom_generator::constantList() {
    ifstream inC("constantTable.txt");
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

