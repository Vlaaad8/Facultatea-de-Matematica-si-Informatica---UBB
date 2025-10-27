//
// Created by vladb on 27/10/2025.
//

#include "../header/AF.h"

AF::AF(const set<string> &states, const set<int> &alphabet, const string &initial_state, const set<string> &final_states, const multimap<pair<string, int>,string > &transitions) {
    this->states = states;
    this->alphabet = alphabet;
    this->initial_state = initial_state;
    this->final_states = final_states;
    this->transitions = transitions;
}
AF::AF() {
    this->states = set<string>();
    this->alphabet = set<int>();
    this->initial_state = "";
    this->final_states = set<string>();
    this->transitions = multimap<pair<string, int>,string>();
}

bool AF::is_deterministic() {
    for (auto &transition: this->transitions) {
        int size=0;
        auto range =transitions.equal_range(transition.first);
        for (auto it = range.first; it != range.second; it++) {
            size++;
        }
        if (size!=1) {
            return false;
        }
    }
    return true;
}
