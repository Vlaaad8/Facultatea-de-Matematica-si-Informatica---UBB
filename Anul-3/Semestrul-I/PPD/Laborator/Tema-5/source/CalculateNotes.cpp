//
// Created by vladb on 17/11/2025.
//

#include "../header/CalculateNotes.h"
#include "../header/LinkedList.h"
#include <string>
#include <fstream>
#include <iostream>
#include <vector>
#include <sqlite3.h>

using namespace std;


void CalculateNotes::run() {
    LinkedList list = LinkedList();
    LinkedList sortedList = LinkedList();

    //File Version

    // for (int i = 1; i <= 10; i++) {
    //     string fileName = "Input/project" + to_string(i) + ".txt";
    //     readNodesFromFile(fileName, list);
    // }


    //DataBase Version

    for (int i = 1; i <= 10; i++) {
        string tableName = "project" + to_string(i);
        readNodesFromDataBase(tableName, list);
    }

    while (true) {
        Node* node = list.extractFirstNode();
        if (node == nullptr) {
            break;
        }
        sortedList.addInOrder(node);
    }

    sortedList.writeToFile("Results/resultS.txt");
}


void CalculateNotes::readNodesFromFile(const string &fileName, LinkedList &list) {
    ifstream in(fileName);

    if (!in.is_open()) {
        cout << "Cannot open file: " << fileName << endl;
    }

    int id, nota;

    while (in >> id >> nota) {
        Node *node = new Node(id, nota);
        list.addOrUpdateNode(node);
    }

    in.close();
}
void CalculateNotes::readNodesFromDataBase(const string &table, LinkedList &list) {
    sqlite3* dataBase;
    sqlite3_stmt *statement;

    const string databaseName = "Laborator-5.sqlite";
    if ( sqlite3_open(databaseName.c_str(), &dataBase) != SQLITE_OK ) {
        return;
    }
    string sql = "SELECT id , nota FROM "+table;

    if (sqlite3_prepare_v2(dataBase, sql.c_str(), -1, &statement, nullptr) != SQLITE_OK) {
        sqlite3_close(dataBase);
        return;
    }
    while (sqlite3_step(statement) == SQLITE_ROW) {
        int id = sqlite3_column_int(statement, 0);
        int nota = sqlite3_column_int(statement, 1);

        Node *node = new Node(id, nota);
        list.addOrUpdateNode(node);
    }

    sqlite3_finalize(statement);
    sqlite3_close(dataBase);
}