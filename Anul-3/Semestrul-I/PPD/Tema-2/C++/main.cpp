#include <iostream>

#include "header/MatrixGenerator.h"
#include "header/EstimateTime.h"
#include "header/ReadFromFile.h"
using namespace std;


bool filesAreEqual(const string &file_name1, const string &file_name2) {
    ifstream f1(file_name1);
    ifstream f2(file_name2);
    if (!f1.is_open() || !f2.is_open()) {
        cout << "Error in opening files" << endl;
        return false;
    }
    f1.seekg(0, ios::end);
    f2.seekg(0, ios::end);
    if (f1.tellg() != f2.tellg()) {
        cout << "Not equal"<<endl;
        return false;
    }
    f1.seekg(0, ios::beg);
    f2.seekg(0, ios::beg);

    char c1,c2;
    while (f1.get(c1) && f2.get(c2)) {
        if (c1 != c2) {
            return false;
        }
    }
    return true;
}

int main(int argc, char *argv[]) {
    if (argc < 5) {
        cout<<"Order is Runs, P, N, M";
        return 1;
    }
    int runs = atoi(argv[1]);
    int P = atoi(argv[2]);
    int N = atoi(argv[3]);
    int M = atoi(argv[4]);

    if (runs==0) {
        MatrixGenerator generator("matrix.txt", "filter.txt", N, M, 3);
        generator.generateMatrix();
        generator.generateFilter();
    }
    //     if (filesAreEqual("resultH.txt","resultS.txt")) {
        EstimateTime estimate_time(N, M, P, 3);
        estimate_time.run();
    }

