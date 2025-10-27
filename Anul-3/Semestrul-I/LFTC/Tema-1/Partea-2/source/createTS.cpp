#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <cctype>
#include "../header/HashTable.h"
#include "../header/atom_generator.h"
#include "../header/validator.h"
using namespace std;

ifstream inA("atoms.txt");
ifstream inB("TS-ID.txt");
ifstream inD("TS-CT.txt");
ofstream outE("errors.txt");
ofstream outID("TS-ID.txt");
ofstream outCT("TS-CT.txt");




void generateFIP() {
    ofstream outF("FIP.txt");
    HashTable tsCT;
    HashTable tsID;
    vector<string> atoms= atom_generator::atom_list();;
    map<string,int> constantList= atom_generator::constantList();
    int i=validator::correctDeclarations(atoms) ;
    if (i==-1){
        outE<<"Programul nu respecta structura de inceput; Verifica MLP"<<endl;
    }
    else {
        vector<string> atomsSub(atoms.begin() + i, atoms.end());
        if(!validator::correctDefinitions(atomsSub)) {
            outE << "Declaratii incorecte! ; Verifica MLP!" << endl;
        }
    }
    outF<<"Cod atom"<<" "<<"Valoare TS"<<endl;
    for (string &atom: atoms) {
        if (constantList.contains(atom)) {
                outF<<constantList[atom]<<endl;
        }
        else {
            try {
                int a = stoi(atom);
                if (validator::validateConstant(atom)) {
                    outF<<0<<" "<<tsCT.add(atom)<<endl;
                }
                else {
                    outE<<atom<<"---> Numarul nu respecta MLP"<<endl;
                }
            }catch (invalid_argument &e) {
                try {
                    float a = stof(atom);
                    if (validator::validateConstant(atom)) {
                        outF<<0<<" "<<tsCT.add(atom)<<endl;
                    }
                    else {
                        outE<<atom<<"---> Numarul nu respecta MLP";
                    }
                }
                catch (invalid_argument &e) {
                    if (!isdigit(atom[0]) && !ispunct(atom[0])) {
                        if (validator::validateIdentifier(atom)) {
                            outF<<1<<" "<<tsID.add(atom)<<endl;
                        }
                        else {
                            outE<<atom<<"---> Constanta nu respecta MLP"<<endl;
                        }
                    }
                    else {
                        outE<<atom<<"---> Nu respecta MLP"<<endl;
                    }
                }
            }
        }
    }
    tsCT.writeToFile("TS-CT.txt");
    tsID.writeToFile("TS-ID.txt");
}

int main() {
    generateFIP();
}