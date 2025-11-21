//
// Created by vladb on 17/11/2025.
//

#ifndef TEMA_4_QUEUECONTAINER_H
#define TEMA_4_QUEUECONTAINER_H
#include <queue>
#include <mutex>
#include <atomic>
#include "LinkedList.h"
using namespace std;

class QueueContainer {
private:
    Node *head;
    Node *tail;
    int size;
    mutex mtx;
    atomic<int> activeProducers;

public:
    QueueContainer(int numberOfProducers) {
        size = 0;
        this->head = nullptr;
        this->tail = nullptr;
        this->activeProducers = numberOfProducers;
    }

    Node *getHead() const;

    Node *getTail() const;

    int getSize() const;

    void addNode(Node *node);

    Node* removeNode();

    void doneProducing();
};


#endif //TEMA_4_QUEUECONTAINER_H
