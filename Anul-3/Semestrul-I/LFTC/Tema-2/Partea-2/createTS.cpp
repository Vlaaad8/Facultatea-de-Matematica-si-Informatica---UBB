#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <cctype>
#include "header/HashTable.h"
#include "header/atom_generator.h"
#include "header/validator.h"
#include "../Partea-1/header/AF.h"
#include "../Partea-1/header/af_reader.h"

using namespace std;

ofstream outError("Partea-2/errors.txt");
ofstream outFIP("Partea-2/FIP.txt");

void generateFIP() {

    HashTable tsCT;
    HashTable tsID;

    AF AF_ID, AF_INT, AF_FLOAT, AF_OPERATOR, AF_SEPARATOR;

    af_reader::read_from_file("Partea-2/AF-ID.txt", AF_ID);
    af_reader::read_from_file("Partea-2/AF-INT.txt", AF_INT);
    af_reader::read_from_file("Partea-2/AF-FLOAT.txt", AF_FLOAT);
    af_reader::read_from_file("Partea-2/AF_OPERATOR.txt", AF_OPERATOR);
    af_reader::read_from_file("Partea-2/AF_SEPARATOR.txt", AF_SEPARATOR);

    vector<string> file = atom_generator::file_list();
    map<string, int> constantList = atom_generator::constantList();

    outFIP << "Cod atom" << " " << "Valoare TS" << endl;

    for (string &row: file) {
        string current_section;
        for (char c: row) {

            if (AF_OPERATOR.is_accepted(string(1, c)) || AF_SEPARATOR.
                is_accepted(string(1, c))) {


                bool resultID = AF_ID.is_accepted(current_section);
                bool resultINT = AF_INT.is_accepted(current_section);
                bool resultFloat = AF_FLOAT.is_accepted(current_section);

                if (resultID) {
                    outFIP << 0 << " " << tsID.add(current_section) << endl;
                } else if (resultINT || resultFloat) {
                    outFIP << 1 << " " << tsCT.add(current_section) << endl;
                } else {
                    if (constantList.contains(current_section)) {
                        outFIP << constantList[current_section]  << endl;
                    }
                    if (!current_section.empty()) {
                        outError << "Error with: " << current_section << endl;
                    }
                }
                outFIP << constantList[string(1, c)] <<endl;
                current_section.clear();
            } else if (c == ' ') {
                continue;
            } else {
                current_section += c;
                if (constantList.contains(current_section)) {
                    outFIP << constantList[current_section] << endl;
                    current_section.clear();
                }
            }
        }
        if (!current_section.empty()) {
            outError <<"Error with: " << current_section << endl;
            current_section.clear();
        }
    }
    tsCT.writeToFile("Partea-2/TS-CT.txt");
    tsID.writeToFile("Partea-2/TS-ID.txt");
}

int main() {
    generateFIP();
}
