#include "../header/af_reader.h"

#include <limits>
#include <sstream>


void af_reader::read_from_file(const string &file_name, AF &af) {
    set<string> states;
    set<char> alphabet;
    string initial_state;
    set<string> final_states;
    multimap<pair<string, char>, string> transitions;
    ifstream in(file_name);
    if (!in.is_open()) {
        cout << "Error in opening the input file!" << endl;
        exit(1);
    }

    string line;
    while (getline(in, line)) {
        if (line.empty()) continue;

        if (line.find("Stari:") == 0) {
            string rest = line.substr(6);
            stringstream ss(rest);
            string nod;
            while (getline(ss, nod, ',')) {
                states.insert(nod);
            }
        } else if (line.find("Alfabet:") == 0) {
            string rest = line.substr(8);
            stringstream ss(rest);
            string simbol;
            while (getline(ss, simbol, ',')) {
                if (!simbol.empty()) alphabet.insert(simbol[0]);;
            }
        } else if (line.find("StareInitiala:") == 0) {
            initial_state = line.substr(14);
        } else if (line.find("StariFinale:") == 0) {
            string rest = line.substr(12);
            stringstream ss(rest);
            string nod;
            while (getline(ss, nod, ',')) {
                final_states.insert(nod);
            }
        } else if (line.find("Tranzitii:") == 0) {
            string rest = line.substr(10);
            stringstream ss(rest);
            string tranzitie;
            while (getline(ss, tranzitie, ',')) {
                size_t p1 = tranzitie.find("->");
                size_t p2 = tranzitie.rfind("->");
                if (p1 != string::npos && p2 != string::npos && p1 != p2) {
                    string de = tranzitie.substr(0, p1);
                    char simbol = tranzitie[p1 + 2];
                    string la = tranzitie.substr(p2 + 2);
                    transitions.insert({{de, simbol}, la});
                }
            }
        }
    }

    in.close();
    af.set_final_states(final_states);
    af.set_transitions(transitions);
    af.set_alphabet(alphabet);
    af.set_states(states);
    af.set_initial_state(initial_state);
}
void af_reader::read_from_command(AF &af) {
    set<string> states;
    set<char> alphabet;
    string initial_state;
    set<string> final_states;
    multimap<pair<string, char>, string> transitions;

    string line, token;
    string from, to;
    char symbol;
    stringstream ss;

    cin.ignore(numeric_limits<streamsize>::max(), '\n');

    cout << "Introduce states:";
    getline(cin, line);
    ss.str(line);
    ss.clear();
    while (getline(ss, token, ',')) {
        if (!token.empty()) states.insert(token);
    }


    cout << "Introduce alphabet:";
    getline(cin, line);
    ss.str(line);
    ss.clear();
    while (getline(ss, token, ',')) {
        if (!token.empty()) alphabet.insert(token[0]);
    }

    cout << "Introduce initial state:";
    getline(cin, initial_state);

    cout << "Introduce final states:";
    getline(cin, line);
    ss.str(line);
    ss.clear();
    while (getline(ss, token, ',')) {
        if (!token.empty()) final_states.insert(token);
    }

    cout << "Introduce transitions in the format: from->symbol->to:";
    getline(cin, line);
    ss.str(line);
    ss.clear();
    while (getline(ss, token, ',')) {
        if (token.empty()) continue;

        size_t p1 = token.find("->");
        size_t p2 = token.rfind("->");

        if (p1 != string::npos && p2 != string::npos && p1 != p2) {
            from = token.substr(0, p1);
            symbol = token[p1 + 2];
            to = token.substr(p2 + 2);
            transitions.insert({{from, symbol}, to});
        } else {
            cout << "Warning: invalid transition format: " << token << endl;
        }
    }

    af.set_states(states);
    af.set_alphabet(alphabet);
    af.set_initial_state(initial_state);
    af.set_final_states(final_states);
    af.set_transitions(transitions);

    cout << "AF loaded successfully from command." << endl;
}
