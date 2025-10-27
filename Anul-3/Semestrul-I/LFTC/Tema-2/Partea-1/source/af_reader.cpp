//
// Created by vladb on 27/10/2025.
//

#include "../header/af_reader.h"

#include <sstream>


void af_reader::read_from_file(const string &file_name, AF &af) {
    set<string> states;
    set<int> alphabet;
    string initial_state;
    set<string> final_states;
    multimap<pair<string, int>, string> transitions;
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
                if (!simbol.empty()) alphabet.insert(simbol[0]);
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
    af.set_states(final_states);
    af.set_initial_state(initial_state);
}
