#ifndef TEMA_2_AF_H
#define TEMA_2_AF_H

#include<iostream>
#include <set>
#include <map>
#include <vector>

using namespace std;

class AF {
private:
    set<string> states;
    set<char> alphabet;
    string initial_state;
    set<string> final_states;
    multimap<pair<string,char>,string> transitions;
public:
    AF(const set<string> &states, const set<char> &alphabet, const string &initial_state, const set<string> &final_states, const multimap<pair<string,char>,string> &transitions);
    AF();
    bool is_deterministic();
    bool is_accepted(string &first_node, const vector<char> &coding) ;
    string longest_prefix(const string &first_node, const vector<char> &coding);
    bool is_from_alphabet(char a);

     set<string> states1() const {
        return states;
    }

    void set_states(const set<string> &states) {
        this->states = states;
    }

   set<char> alphabet1() const {
        return alphabet;
    }

    void set_alphabet(const set<char> &alphabet) {
        this->alphabet = alphabet;
    }

     string initial_state1() const {
        return initial_state;
    }

    void set_initial_state(const string &initial_state) {
        this->initial_state = initial_state;
    }

     set<string> final_states1() const {
        return final_states;
    }

    void set_final_states(const set<string> &final_states) {
        this->final_states = final_states;
    }

     multimap<pair<string,char>, string> transitions1() const {
        return transitions;
    }

    void set_transitions(const multimap<pair<string, char>, string> &transitions) {
        this->transitions = transitions;
    }
};


#endif //TEMA_2_AF_H