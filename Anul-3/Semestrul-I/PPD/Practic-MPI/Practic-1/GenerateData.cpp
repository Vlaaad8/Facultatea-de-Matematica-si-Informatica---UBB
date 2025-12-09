//
// Created by vladb on 07/12/2025.
//

#include "GenerateData.h"
#include <fstream>


void GenerateData::generateData(const string &fileName,int n,int power) {
    ofstream out(fileName);
    if (out.is_open()) {
        out<<n<<" "<<power<<endl;
        for(int i=0;i<n;i++) {
            int value = rand() % 20;
            out<<value<<" ";
        }
    }

}
