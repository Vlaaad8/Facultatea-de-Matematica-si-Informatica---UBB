#include "../header/AF.h"
#include <vector>

AF::AF(const set<string> &states, const set<char> &alphabet, const string &initial_state,
       const set<string> &final_states, const multimap<pair<string, char>, string> &transitions) {
    this->states = states;
    this->alphabet = alphabet;
    this->initial_state = initial_state;
    this->final_states = final_states;
    this->transitions = transitions;
}

AF::AF() {
    this->states = set<string>();
    this->alphabet = set<char>();
    this->initial_state = "";
    this->final_states = set<string>();
    this->transitions = multimap<pair<string, char>, string>();
}

bool AF::is_deterministic() {

    for (auto &node: this->states) {
        for (auto values: this->alphabet) {
            auto range = transitions.equal_range({node, values});
            int size = 0;
            for (auto it = range.first; it != range.second; it++) {
                size++;
            }
            if (size > 1) {
                return false;
            }
        }
    }
    return true;
}


bool AF::is_accepted(string &first_node, const vector<char> &coding) {
    if (!this->is_deterministic()) {
        cout << "AF is not deterministic" << endl;
        return false;
    }
    if (this->initial_state!=first_node) {
        cout <<first_node <<" is not initial state" << endl;
        return false;
    }
    for (auto value: coding) {
        if (!is_from_alphabet(value)) {
            return false;
        }
        auto range = this->transitions.equal_range(pair<string, char>(first_node, value));
        if (range.first == range.second) {
            return false;
        }
        first_node = range.first->second;
    }
    for (const auto &value: final_states) {
        if (first_node == value) {
            return true;
        }
    }
    return false;
}

string AF::longest_prefix(const string &first_node, const vector<char> &coding) {
    if (!this->is_deterministic()) {
        cout << "AF is not deterministic" << endl;
        return "";
    }
    if (first_node != initial_state) {
        cout << first_node + " is not a start position!" << endl;
        return "";
    }
    string current_prefix = "";
    string accepted_prefix = "";
    string current_node = first_node;
    for (auto value: coding) {
        if (!is_from_alphabet(value)) {
            return accepted_prefix;
        }
        auto range = this->transitions.equal_range(pair(current_node, value));
        if (range.first == range.second) {
            return accepted_prefix;
        }
        current_node = range.first->second;
        current_prefix += value;

        for (const string &state: this->final_states) {
            if (current_node == state) {
                accepted_prefix = current_prefix;
            }
        }
    }
    return accepted_prefix;
}
bool AF::is_from_alphabet(char a) {
    for (char c: this->alphabet) {
        if (a == c) {
            return true;
        }
    }
    return false;
}