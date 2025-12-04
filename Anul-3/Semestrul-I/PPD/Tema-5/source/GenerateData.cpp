//
// Created by vladb on 17/11/2025.
//
#include <fstream>
#include <random>
#include <vector>
#include <algorithm>
#include <chrono>
#include <string>
using namespace std;

#include "../header/GenerateData.h"
#include <fstream>
#include <random>
#include <vector>
#include <algorithm>
#include <chrono>
#include <string>
#include <iostream>
using namespace std;

void GenerateData::generateData(const string& fileName, const int numberOfStudents, const int minNotes) {

    ofstream out(fileName);
    if (!out.is_open()) {
        cerr << "Cannot open file: " << fileName << endl;
        return;
    }

    static random_device rd;
    static mt19937 gen(rd());

    uniform_int_distribution<int> distCount(minNotes, numberOfStudents);
    int count = distCount(gen);

    vector<int> ids(numberOfStudents);
    for (int i = 0; i < numberOfStudents; ++i) {
        ids[i] = i + 1;
    }

    ranges::shuffle(ids, gen);

    uniform_int_distribution<int> distNote(1, 10);

    for (int i = 0; i < count; ++i) {
        int id = ids[i];
        int nota = distNote(gen);
        out  << id << " " << nota  << '\n';
    }

    out.close();
}
