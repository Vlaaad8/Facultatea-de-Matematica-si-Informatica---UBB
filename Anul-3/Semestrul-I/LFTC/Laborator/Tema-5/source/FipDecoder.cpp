//
// Created by vladb on 12/01/2026.
//

#include "../header/FipDecoder.h"
#include <fstream>
#include <iostream>
#include <sstream>

vector<string> FipDecoder::loadFromFip(const string &fileName) {
    vector<string> inputSequence;
    ifstream fin(fileName);

    if (!fin.is_open()) {
        cout<<"Can't open file "<<fileName<<endl;
        return inputSequence;
    }

    string line;
    while (getline(fin, line)) {
        if (line.empty()) continue;

        stringstream ss(line);
        int code;
        ss >> code;

        if (codeToTerminal.count(code)) {
            inputSequence.push_back(codeToTerminal[code]);
        } else {
           cout<<"Token was not found in dictionary! Code: "<<code<<endl;
        }
    }
    fin.close();
    return inputSequence;
}

