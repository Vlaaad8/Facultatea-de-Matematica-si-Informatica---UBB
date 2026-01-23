//
// Created by vladb on 17/11/2025.
//
#include <fstream>
#include <random>
#include <vector>
#include <algorithm>
#include <chrono>
#include <string>
using namespace std;

#include "../header/GenerateData.h"
#include <fstream>
#include <random>
#include <vector>
#include <algorithm>
#include <chrono>
#include <string>
#include <iostream>
#include <sqlite3.h>
using namespace std;

void GenerateData::generateData(const string& fileName, const int numberOfStudents, const int minNotes) {

    ofstream out(fileName);
    if (!out.is_open()) {
        cerr << "Cannot open file: " << fileName << endl;
        return;
    }

    static random_device rd;
    static mt19937 gen(rd());

    uniform_int_distribution<int> distCount(minNotes, numberOfStudents);
    int count = distCount(gen);

    vector<int> ids(numberOfStudents);
    for (int i = 0; i < numberOfStudents; ++i) {
        ids[i] = i + 1;
    }

    ranges::shuffle(ids, gen);

    uniform_int_distribution<int> distNote(1, 10);

    for (int i = 0; i < count; ++i) {
        int id = ids[i];
        int nota = distNote(gen);
        out  << id << " " << nota  << '\n';
    }

    out.close();
}
void GenerateData::generateDataBase(const string& tableName, int numberOfStudents, int minNotes) {
    sqlite3* db;
    char* errMsg = 0;
    const string dbName = "Laborator-5.sqlite";

    if (sqlite3_open(dbName.c_str(), &db) != SQLITE_OK) {
        cerr << "Eroare la deschiderea DB: " << sqlite3_errmsg(db) << endl;
        return;
    }

    sqlite3_exec(db, "PRAGMA synchronous = OFF;", 0, 0, 0);
    sqlite3_exec(db, "PRAGMA journal_mode = MEMORY;", 0, 0, 0);

    string dropSql = "DROP TABLE IF EXISTS " + tableName + ";";
    sqlite3_exec(db, dropSql.c_str(), 0, 0, 0);


    string createSql = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY, nota INTEGER);";

    if (sqlite3_exec(db, createSql.c_str(), 0, 0, &errMsg) != SQLITE_OK) {
        cerr << "SQL Error: " << errMsg << endl;
        sqlite3_free(errMsg);
        sqlite3_close(db);
        return;
    }


    static random_device rd;
    static mt19937 gen(rd());

    uniform_int_distribution<int> distCount(minNotes, numberOfStudents);
    int count = distCount(gen);

    vector<int> ids(numberOfStudents);
    iota(ids.begin(), ids.end(), 1);
    ranges::shuffle(ids, gen);

    uniform_int_distribution<int> distNote(1, 10);


    sqlite3_exec(db, "BEGIN TRANSACTION;", 0, 0, 0);

    sqlite3_stmt* stmt;
    string insertSql = "INSERT INTO " + tableName + " (id, nota) VALUES (?, ?);";

    if (sqlite3_prepare_v2(db, insertSql.c_str(), -1, &stmt, nullptr) != SQLITE_OK) {
        cerr << "Prepare Error: " << sqlite3_errmsg(db) << endl;
        sqlite3_close(db);
        return;
    }

    for (int i = 0; i < count; ++i) {

        sqlite3_bind_int(stmt, 1, ids[i]);
        sqlite3_bind_int(stmt, 2, distNote(gen));

        sqlite3_step(stmt);
        sqlite3_reset(stmt);
    }


    sqlite3_finalize(stmt);

    sqlite3_exec(db, "COMMIT;", 0, 0, 0);


    string indexSql = "CREATE INDEX IF NOT EXISTS idx_" + tableName + "_cover ON " + tableName + "(id, nota);";
    sqlite3_exec(db, indexSql.c_str(), 0, 0, 0);

    sqlite3_close(db);


}
