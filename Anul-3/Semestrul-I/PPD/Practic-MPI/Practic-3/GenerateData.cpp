//
// Created by vladb on 08/12/2025.
//

#include "GenerateData.h"
#include <iostream>
#include <fstream>
using namespace std;

void GenerateData::generate(const string &fileName, int N) {
    ofstream outF(fileName);

    outF << N << endl;
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            outF << rand() % 50 << " ";
        }
        outF << endl;
    }
    outF.close();
}

