//
// Created by vladb on 09/12/2025.
//

#include "Gramatic.h"
#include <fstream>
#include <iostream>

void Gramatic::readFromFile(const string &fileName) {
    ifstream in(fileName);

    string line;

    while (getline(in, line)) {
        int index = line.find(" -> ");

        char neterminal = line[index - 1];

        string right = line.substr(index + 4);

        int start = 0;
        while (start < right.size()) {
            int indexB = right.find('|', start);
            string prod;

            if (indexB == string::npos) {
                prod = right.substr(start);
                start = right.size();
            } else {
                prod = right.substr(start, indexB - start);
                start = indexB + 1;
            }


            int s = 0;
            while (s < prod.size() && isspace(prod[s])) s++;

            int e = prod.size();
            while (e > s && isspace(prod[e - 1])) e--;

            if (e > s) {
                string cleaned = prod.substr(s, e - s);
                rules[neterminal].push_back(cleaned);
            }
        }
    }


    for (const auto &entry : rules) {
        char neterminal = entry.first;
        const vector<string> &prods = entry.second;

        for (const string &prod : prods) {
            if (prod.find(neterminal) != string::npos) {
                cout << neterminal << " -> " << prod << '\n';
            }
        }
    }

}
