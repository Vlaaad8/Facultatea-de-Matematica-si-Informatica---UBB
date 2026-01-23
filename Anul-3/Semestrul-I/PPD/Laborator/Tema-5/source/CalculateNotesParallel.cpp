//
// Created by vladb on 18/11/2025.
//

#include "../header/CalculateNotesParallel.h"

#include <barrier>
#include <sqlite3.h>
#include "../header/ThreadPool.h"
#include <iostream>
#include <fstream>

using namespace std;

constexpr int numberOfFiles = 10;

void CalculateNotesParallel::run() {
    QueueContainer queue(readerThreads, maxSize);
    LinkedList list;
    LinkedList sortedList;

    const int consumerThreads = P - readerThreads;

    barrier<> wait_barrier(consumerThreads);

    thread threads[consumerThreads];


    for (int i = 0; i < consumerThreads; i++) {
        threads[i] = thread(&CalculateNotesParallel::consumerThread, this, ref(queue), ref(list),ref(sortedList) ,ref(wait_barrier));
    }

    {
        ThreadPool readPool(readerThreads);
        for (int i = 1; i <= numberOfFiles; i++) {
            //File Version

            // string fileName = "Input/project" + to_string(i) + ".txt";
            // readPool.enqueue([this, fileName, &queue]() {
            //     this->readNodesFromFile(fileName, queue);
            // });

            //DataBase Version

            string table = "project"+ to_string(i);
            readPool.enqueue([this,table,&queue]() {
                this->readNodesFromDataBase(queue,table);
            });
        }
    }

    for (int i = 0; i < readerThreads; i++) {
        queue.doneProducing();
    }

    for (int i = 0; i < consumerThreads; i++) {
        threads[i].join();
    }

    sortedList.writeToFile("Results/resultT.txt");
}

void CalculateNotesParallel::consumerThread(QueueContainer &queue, LinkedList &list,LinkedList &sortedList, barrier<> &wait_barrier) {
    while (true) {
        Node *node = queue.removeNode();

        if (node == nullptr) {
            break;
        }

        list.addOrUpdateNode(node);
    }
    wait_barrier.arrive_and_wait();

    // acum e momentul sa facem lista sortata

    while (true) {
        Node* node = list.extractFirstNode();
        if (node == nullptr) {
            break;
        }
        sortedList.addInOrder(node);
    }

}

void CalculateNotesParallel::readNodesFromFile(const string &fileName, QueueContainer &queue) {
    ifstream in(fileName);

    if (!in.is_open()) {
        cout << "Cannot open file: " << fileName << endl;
    }

    int id, nota;

    while (in >> id >> nota) {
        Node *node = new Node(id, nota);
        queue.addNode(node);
    }

    in.close();
}
void CalculateNotesParallel::readNodesFromDataBase(QueueContainer &queue,const string &tableName) {
    sqlite3* dataBase;
    sqlite3_stmt *statement;

    const string databaseName = "Laborator-5.sqlite";
    if ( sqlite3_open(databaseName.c_str(), &dataBase) != SQLITE_OK ) {
        return;
    }
    string sql = "SELECT id , nota FROM "+tableName;

    if (sqlite3_prepare_v2(dataBase, sql.c_str(), -1, &statement, nullptr) != SQLITE_OK) {
        sqlite3_close(dataBase);
        return;
    }
    while (sqlite3_step(statement) == SQLITE_ROW) {
        int id = sqlite3_column_int(statement, 0);
        int nota = sqlite3_column_int(statement, 1);

        Node *node = new Node(id, nota);
        queue.addNode(node);
    }

    sqlite3_finalize(statement);
    sqlite3_close(dataBase);
}


