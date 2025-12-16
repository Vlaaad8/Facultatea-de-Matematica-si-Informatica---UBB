#include <iostream>
#include "header/CalculateNotes.h"
#include "header/CalculateNotesParallel.h"
#include "header/GenerateData.h"
#include <chrono>
using namespace std;
using namespace std::chrono;

int main(int argc, char **argv) {
    const int P = atoi(argv[1]);
    const int readerP = atoi(argv[2]);
    const int run = atoi(argv[3]);
    const int version = atoi(argv[4]);

    if (argc < 4) {
        cerr << "Please provide at least four arguments" << endl;
    }
    if (version == 0) {
        if (run == 1) {
            //File Version

            // const string path = "Input/project";
            // for (int i = 1; i <= 10; i++) {
            //     const string fileName = path + to_string(i) + ".txt";
            //     GenerateData::generateData(fileName, 500, 240);


            //DataBase Version

            const string tableName= "project";
                for (int i = 1 ; i <=10 ;i++) {
                    const string table =  tableName+ to_string(i);
                    GenerateData::generateDataBase(table, 500, 240);
                }

            }


        CalculateNotes calculateStatic;
        auto start = high_resolution_clock::now();
        calculateStatic.run();
        auto end = high_resolution_clock::now();
        auto duration = end-start;

        cout << duration.count()<<endl;
    } else {
        CalculateNotesParallel calculateParallel(readerP, P,50);
        auto start = high_resolution_clock::now();
        calculateParallel.run();
        auto end = high_resolution_clock::now();
        auto duration = end-start;

        cout << duration.count() << endl;
    }
}
