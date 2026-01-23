#include "../header/HashTable.h"
#include <fstream>

int HashTable::hash(const string &value) {
    int hash_value=0;
    for (char c : value) {
        hash_value=(hash_value*18+c)%SIZE;
    }
    return hash_value;
}
int HashTable::add(const string &value) {
    int hash_value = hash(value);
    while (table[hash_value].first!=-1) {
        if (table[hash_value].second==value) {
            return table[hash_value].first;
        }
        hash_value=(hash_value+1)%SIZE;
    }
    table[hash_value]={position,value};
    map_position.push_back(hash_value);
    position++;
    return position-1;;

}
pair<int,string> HashTable::getByPos(int key) {
        return table[map_position[key]];
}
void HashTable::writeToFile(const string& file_name) {
    ofstream out(file_name);
    if (!out.is_open()) {
        cout<<"Error on opening file";
        exit(1);
    }
    for (int i=0;i<map_position.size();i++) {
        pair<int,string> p= getByPos(i);
            // out<< p.second <<" "<<p.first<<" "<<map_position[i]<<endl;
            out<<p.second<<" "<<p.first<<endl;
    }
    for (int i=0;i<table.size();i++) {
        cout<<i<<" "<<table[i].first<<" "<<table[i].second<<endl;
    }
    cout<<endl;

}