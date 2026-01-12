#include <iostream>

#include "header/FipDecoder.h"
#include "header/Gramatic.h"
#include "header/SyntacticAnalyzer.h"

using namespace std;

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

            std::vector<std::string> input = {"x", "x" , "b"};
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