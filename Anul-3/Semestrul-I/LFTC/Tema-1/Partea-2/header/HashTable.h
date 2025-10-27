//
// Created by vladb on 20/10/2025.
//

#ifndef TEMA_1_HASHTABLE_H
#define TEMA_1_HASHTABLE_H
#include <vector>
#include <iostream>
#include <string>

using namespace std;


class HashTable {
private:
    static const int SIZE = 100;
    vector<pair<int, string> > table;
    int position = 0;
    vector<int> map_position;

public:
    HashTable() : table(SIZE, {-1, ""}) {
    }

    static int hash(const string &value);

    int add(const string &value);

    pair<int,string> getByPos(int key) ;

    void writeToFile(const string& file_name);

};


#endif //TEMA_1_HASHTABLE_H
