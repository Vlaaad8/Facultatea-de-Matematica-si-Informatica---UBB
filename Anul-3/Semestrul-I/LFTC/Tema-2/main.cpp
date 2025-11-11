#include <iostream>
#include <sstream>

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
    cout << "7. Test if a sequnce is accpted by AFD" << endl;
    cout << "8. Show the longest prefix from a sequence accepted by AFD"<<endl;
    cout << "Your choice : ";
}

void show_states(AF &af) {
    set<string> states = af.states1();
    cout << "States = {\n";
    for (const string &state: states) {
        cout << state << " ";
    }
    cout << "\n }" << endl;
}

void show_alphabet(AF &af) {
    set<char> alphabet = af.alphabet1();
    cout << "Alphabet = { \n";
    for (const char &letter: alphabet) {
        cout << letter << " ";
    }
    cout << "\n }" << endl;
}

void show_final_states(AF &af) {
    set<string> final_states = af.final_states1();
    cout << "Final states = {\n";
    for (const string &state: final_states) {
        cout << state << " ";
    }
    cout << "\n }" << endl;
}


void show_transitions(AF &af) {
    multimap<pair<string,char>, string> transitions = af.transitions1();

    cout << "Tranzitii = {\n";

    auto it = transitions.begin();
    while (it != transitions.end()) {
        string from = it->first.first;
        char symbol = it->first.second;

        cout << "  (" << from << ", ";
        if (isprint(symbol))
            cout << "'" << symbol << "'";
        else
            cout << symbol;
        cout << ") -> { ";

        auto range = transitions.equal_range(it->first);
        bool first = true;
        for (auto jt = range.first; jt != range.second; ++jt) {
            if (!first) cout << ", ";
            cout << jt->second;
            first = false;
        }
        cout << " }\n";


        it = range.second;
    }

    cout << "}\n";
}

void verify_sequence(AF &af) {
    string codes;
    cout << "Introduce codes: ";
    cin >> codes;
    stringstream ss(codes);
    if (af.is_accepted(codes)) {
        cout << "Accepted" << endl;
    } else {
        cout << "Not accepted" << endl;
    }
}
void longest_prefix(AF &af) {
    vector<char> coding;
    string first_node;
    string codes;

    cout << "Introduce first node: ";
    cin >> first_node;
    cout << "Introduce codes (separated by ,): ";
    cin >> codes;

    stringstream ss(codes);
    string token;
    while (getline(ss, token, ',')) {
        if (!token.empty()) {
            coding.push_back(token[0]);
        }
    }
    string prefix = af.longest_prefix(coding);
    if (prefix.empty()) {
        cout << "I didn't found a valid prefix" << endl;
    }
    else {
        cout<<"Prefix found: "<<prefix<<endl;
    }
}


int main() {
    int option;
    AF af;
    bool running = true;
    while (running) {
        show_menu();
        cin >> option;
        switch (option) {
            case 1:
                af_reader::read_from_file("input2.txt", af);
                break;
            case 2:
                af_reader::read_from_command(af);
                break;
            case 3:
                show_states(af);
                break;
            case 4:
                show_alphabet(af);
                break;
            case 5:
                show_transitions(af);
                break;
            case 6:
                show_final_states(af);
                break;

            case 7:
                verify_sequence(af);
                break;
            case 8:
                longest_prefix(af);
                break;
            case 0:
                running = false;
            default:
                cout << "Invalid option" << endl;
                break;
        }
    }
}
