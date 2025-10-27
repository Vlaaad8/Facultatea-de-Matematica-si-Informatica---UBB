#include <iostream>
#include "Partea-1/header/AF.h"
#include "Partea-1/header/af_reader.h"
using namespace std;

void show_menu() {
    cout << "Laborator-2 Menu" << endl;
    cout << "1. Read AF from file" << endl;
    cout << "2. Read AF from Console" << endl;
    cout << "3. Show all states" << endl;
    cout << "4. Show all alphabet" << endl;
    cout << "5. Show all transitions" << endl;
    cout << "6. Show all final states" << endl;
    cout << "Your choice : ";
}

void show_states(AF &af) {
    set<string> states = af.states1();
    cout << "{";
    for (string state: states) {
        cout << state << ",";
    }
    cout << "}" << endl;
}

int main() {
    int option;
    AF af;
    while (true) {
        show_menu();
        cin >> option;
        switch (option) {
            case 1:
                af_reader::read_from_file("input.txt", af);
                break;
            case 3:
                show_states(af);
                break;
            default:
                cout << "Invalid option" << endl;
                break;
        }
    }
}
