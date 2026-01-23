#include "../header/HashTable.h"
#include <fstream>

int HashTable::hash(const string &value) {
    int hash_value=0;
    for (char c : value) {
        hash_value=(hash_value*29+c)%SIZE;
    }
    return hash_value;
}
// int HashTable::add(const string &value) {
//     int hash_value = hash(value);
//     while (!table[hash_value].empty()) {
//         if (table[hash_value]==value) {
//             return hash_value;
//         }
//         hash_value=(hash_value+1)%SIZE;
//     }
//     table[hash_value]=value;
//     return hash_value;
//
// }
int HashTable::add(const string &value) {
    int hash_value = hash(value);
    int start = hash_value;

    do {
        if (table[hash_value].empty()) {
            table[hash_value] = value;
            return hash_value;
        }
        if (table[hash_value] == value) {
            return hash_value;
        }
        hash_value = (hash_value + 1) % SIZE;
    } while (hash_value != start);
    return -1;
}

string HashTable::getByPos(int key) {
    if (key < 0 || key >= SIZE)  throw out_of_range("Invalid key");
        return table[key];
}
void HashTable::writeToFile(const string& file_name) {
    ofstream out(file_name);
    if (!out.is_open()) {
        cout<<"Error on opening file";
        exit(1);
    }
    for (int i=0;i<table.size();i++) {
        if (!table[i].empty()) {
            out<<i<<" "<<table[i]<<endl;
        }
        else {
            out<<i<<" --"<<endl;
        }
    }
    cout<<endl;

}