//
// Created by vladb on 18/11/2025.
//

#include "../header/CalculateNotesParallel.h"
#include "../header/ThreadPool.h"
#include <iostream>
#include <fstream>

using namespace std;

constexpr int numberOfFiles = 10;

void CalculateNotesParallel::run() {
    QueueContainer queue(readerThreads, maxSize);

    LinkedList list;

    const int consumerThreads = P - readerThreads;

    thread threads[consumerThreads];


    for (int i = 0; i < consumerThreads; i++) {
        threads[i] = thread(&CalculateNotesParallel::consumerThread, this, ref(queue), ref(list));
    }

    {
        ThreadPool readPool(readerThreads);
        for (int i = 1; i <= numberOfFiles; i++) {
            string fileName = "Input/project" + to_string(i) + ".txt";
            readPool.enqueue([this, fileName, &queue]() {
                this->readNodesFromFile(fileName, queue);
            });
        }
    }
    for (int i = 0; i < readerThreads; i++) {
        queue.doneProducing();
    }

    for (int i = 0; i < consumerThreads; i++) {
        threads[i].join();
    }
    LinkedList sortedList;      // Lista goală unde vom pune datele ordonate
    vector<thread> sortWorkers; // Vector pentru thread-urile de sortare

    // Refolosim numărul total de procesoare (P) sau un număr fix de workeri
    int numSortWorkers = 4; // Sau P, dacă ai variabila disponibilă

    for (int i = 0; i < numSortWorkers; ++i) {
        sortWorkers.emplace_back([&]() {
            while (true) {
                // 1. EXTRAGERE: Luăm un nod din lista veche (dezordonată)
                // Această funcție trebuie să fie Thread-Safe!
                Node* node = list.extractFirstNode();

                // 2. STOP: Dacă am primit nullptr, lista sursă e goală
                if (node == nullptr) {
                    break;
                }

                // 3. INSERARE: Punem nodul în lista nouă la locul corect
                // Și această funcție trebuie să fie Thread-Safe (hand-over-hand locking)!
                sortedList.addInOrder(node);
            }
        });
    }

    // Așteptăm să termine toți muncitorii de mutat și sortat
    for (auto& t : sortWorkers) {
        t.join();
    }

    sortedList.writeToFile("Results/resultT.txt");
}

void CalculateNotesParallel::consumerThread(QueueContainer &queue, LinkedList &list) {
    while (true) {
        Node *node = queue.removeNode();

        if (node == nullptr) {
            break;
        }

        list.addOrUpdateNode(node);
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
