//
// Created by vladb on 10/12/2025.
//

#include "Generate.h"
#include <fstream>


void Generate::generateData(const string& fileName,int numberOfValues) {
    ofstream out(fileName);

    out<<numberOfValues<<endl;
    for (int i = 0; i < numberOfValues; i++) {
        out<<rand() % 9 <<" ";
    }
}