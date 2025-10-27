#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <cctype>
#include "HashTable.h"

using namespace std;

ifstream inA("atoms.txt");
ifstream inB("TS-ID.txt");
ifstream inD("TS-CT.txt");
ofstream outID("TS-ID.txt");
ofstream outCT("TS-CT.txt");


void generateTS(vector<string> &toSort) {


    HashTable tsCT;
    HashTable tsID;



    if (!inA.is_open()) {
        cout<<"Error opening input file"<<endl;
        exit(1);
    }
    if (!outID.is_open()) {
        cout<<"Error opening output-ID file"<<endl;
        exit(1);
    }
    if (!outCT.is_open()) {
        cout<<"Error opening output-CT file"<<endl;
        exit(1);
    }

    for (string &line: toSort) {
        try {
            int a = stoi(line);
            tsCT.add(to_string(a));
        }catch (invalid_argument &e) {
            try {
                float a = stof(line);
                tsCT.add(to_string(a));
            }
            catch (invalid_argument &e) {
                if (!isdigit(line[0]) && !ispunct(line[0])) {

                    tsID.add(line);
                }
            }
        }
    }
        tsCT.writeToFile("TS-CT.txt");
        tsID.writeToFile("TS-ID.txt");

}

// map<string,int> getTSCT() {
//     map<string,int> tsCT;
//     if (!inD.is_open()) {
//         throw invalid_argument("Error opening input file");
//     }
//     string line;
//     while (getline(inD, line)) {
//         int idx = line.find(' ');
//         if (idx != string::npos) {
//             string word = line.substr(0, idx);
//             int index = stoi(line.substr(idx + 1));
//             tsCT.insert(make_pair(word, index));
//         }
//     }
//     return tsCT;
// }
//
// map<string,int> getTSID() {
//     map<string,int> tsCT;
//     if (!inB.is_open()) {
//         throw invalid_argument("Error opening input file");
//     }
//     string line;
//     while (getline(inB, line)) {
//         int idx = line.find(' ');
//         if (idx != string::npos) {
//             string word = line.substr(0, idx);
//             int index = stoi(line.substr(idx + 1));
//             tsCT.insert(make_pair(word, index));
//         }
//     }
//     return tsCT;
// }