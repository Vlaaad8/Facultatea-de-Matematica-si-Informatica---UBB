#ifndef TEMA_2_AF_H
#define TEMA_2_AF_H

#include<iostream>
#include <set>
#include <map>
using namespace std;

class AF {
private:
    set<string> states;
    set<int> alphabet;
    string initial_state;
    set<string> final_states;
    multimap<pair<string,int>,string> transitions;
public:
    AF(const set<string> &states, const set<int> &alphabet, const string &initial_state, const set<string> &final_states, const multimap<pair<string,int>,string> &transitions);
    AF();
    bool is_deterministic();

     set<string> states1() const {
        return states;
    }

    void set_states(const set<string> &states) {
        this->states = states;
    }

   set<int> alphabet1() const {
        return alphabet;
    }

    void set_alphabet(const set<int> &alphabet) {
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

     multimap<pair<string, int>, string> transitions1() const {
        return transitions;
    }

    void set_transitions(const multimap<pair<string, int>, string> &transitions) {
        this->transitions = transitions;
    }
};


#endif //TEMA_2_AF_H