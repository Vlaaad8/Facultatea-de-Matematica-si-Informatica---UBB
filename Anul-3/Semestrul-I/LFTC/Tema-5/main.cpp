#include <iostream>
#include <sstream>

#include "header/FipDecoder.h"
#include "header/Gramatic.h"
#include "header/SyntacticAnalyzer.h"

using namespace std;

vector<string> wordSplitter(const string &line) {
    vector<string> result;
    stringstream ss(line);
    string word;

    while (ss >> word) {
        result.push_back(word);
    }

    return result;
}

int main() {
    while (true) {
        int option;
        cout<<"1. Input a sequnce"<<endl;
        cout<<"2. Input a FIP"<<endl;
        cout<<"3. Exit"<<endl;
        cout<<"Your option: ";
        cin>>option;

        if(option==1) {

            Gramatic g;
            g.loadFromFile("gramatic.txt");

            SyntacticAnalyzer sa(g);
            sa.initialize();
            cout<<"Introduce a sequence: ";
            string line;

            cin>>line;

           vector<string> input = wordSplitter(line);
            sa.parse(input);
        }
        else if(option==2) {
            Gramatic g;
            g.loadFromFile("gramaticMLP.txt");

            SyntacticAnalyzer sa(g);
            sa.initialize();

            FipDecoder fipDecoder;

            vector<string> input = fipDecoder.loadFromFip("inputFIP.txt");
            sa.parse(input);
        }
        else if(option==3) {
            break;
        }
        else {
            cout<<"Invalid option!"<<endl;
        }
    }

    return 0;
}