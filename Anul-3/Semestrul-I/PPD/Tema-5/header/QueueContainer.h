//
// Created by vladb on 17/11/2025.
//

#ifndef TEMA_4_QUEUECONTAINER_H
#define TEMA_4_QUEUECONTAINER_H
#include <queue>
#include <mutex>
#include <atomic>
#include <condition_variable>

#include "LinkedList.h"
using namespace std;

class QueueContainer {
private:
    Node *head;
    Node *tail;
    int size;
    mutex mtx;
    condition_variable hasActiveProducers;
    condition_variable isFull;

    bool stillProducing;
    int maxSize;

    int activeProducers;

public:
    QueueContainer(const int numberOfProducers,const int maxSize) {
        size = 0;
        this->head = nullptr;
        this->tail = nullptr;
        this->maxSize = maxSize;
        this->stillProducing = true;
        this->activeProducers = numberOfProducers;
    }

    Node *getHead() const;

    Node *getTail() const;

    int getSize() const;

    void addNode(Node *node);

    Node* removeNode();

    void doneProducing();
};


#endif