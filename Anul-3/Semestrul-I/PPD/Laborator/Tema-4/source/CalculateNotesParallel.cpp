//
// Created by vladb on 18/11/2025.
//

#include "../header/CalculateNotesParallel.h"

#include <iostream>
#include <fstream>
#include <list>
using namespace std;

const int numberOfFiles = 10;

void CalculateNotesParallel::run() {
    QueueContainer queue(readerThreads);

    LinkedList list;

    thread threads[P];

    int batchSize = numberOfFiles / readerThreads;
    int start = 1;
    for (int i = 1; i <= readerThreads; i++) {
        threads[i-1]= thread(&CalculateNotesParallel::producerThread,this,start,start+batchSize-1,ref(queue));
        start+=batchSize;
    }

    for (int i= readerThreads; i < P; i++) {
        threads[i]= thread(&CalculateNotesParallel::consumerThread,this,ref(queue),ref(list));
    }

    for (int i= 0; i < P; i++) {
        threads[i].join();
    }

    list.writeToFile("Results/resultT.txt");
}

void CalculateNotesParallel::producerThread(const int startIndex, const int endIndex, QueueContainer &queue) {
    for (int i = startIndex; i <= endIndex; i++) {
        string fileName = "Input/project" + to_string(i) + ".txt";
        readNodesFromFile(fileName, queue);
    }
    queue.doneProducing();
}

void CalculateNotesParallel::consumerThread(QueueContainer &queue, LinkedList &list) {
    while (true) {
        Node* node = queue.removeNode();

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
