#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <cctype>

using namespace std;

ifstream inA("atoms.txt");
ifstream inB("TS-ID.txt");
ifstream inD("TS-CT.txt");
ofstream outID("TS-ID.txt");
ofstream outCT("TS-CT.txt");


void generateTS(vector<string> &toSort) {

    map<string,int> tsCT;
    map<string,int> tsID;

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

    int positionCT=0;
    int positionID=0;
    for (string &line: toSort) {
        try {
            int a = stoi(line);
            positionCT++;
            tsCT.insert(pair(line,positionCT));
        }catch (invalid_argument &e) {
            try {
                float a = stof(line);
                positionCT++;
                tsCT.insert(pair(line,positionCT));
            }
            catch (invalid_argument &e) {
                if (!isdigit(line[0]) && !ispunct(line[0])) {
                    positionID++;

                    tsID.insert(pair(line,positionID));
                }
            }
        }
    }
    for (pair<string,int> p : tsCT) {
        outCT<<p.first<<" "<<p.second<<endl;
    }
    for (pair<string,int> p : tsID) {
            outID<<p.first<<" "<<p.second<<endl;

    }
    inA.close();

}

map<string,int> getTSCT() {
    map<string,int> tsCT;
    if (!inD.is_open()) {
        throw invalid_argument("Error opening input file");
    }
    string line;
    while (getline(inD, line)) {
        int idx = line.find(' ');
        if (idx != string::npos) {
            string word = line.substr(0, idx);
            int index = stoi(line.substr(idx + 1));
            tsCT.insert(make_pair(word, index));
        }
    }
    return tsCT;
}

map<string,int> getTSID() {
    map<string,int> tsCT;
    if (!inB.is_open()) {
        throw invalid_argument("Error opening input file");
    }
    string line;
    while (getline(inB, line)) {
        int idx = line.find(' ');
        if (idx != string::npos) {
            string word = line.substr(0, idx);
            int index = stoi(line.substr(idx + 1));
            tsCT.insert(make_pair(word, index));
        }
    }
    return tsCT;
}