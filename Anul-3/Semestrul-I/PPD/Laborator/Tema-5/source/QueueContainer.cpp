//
// Created by vladb on 17/11/2025.
//

#include "../header/QueueContainer.h"

Node *QueueContainer::getHead() const {
    return this->head;
}

Node *QueueContainer::getTail() const {
    return this->tail;
}

int QueueContainer::getSize() const {
    return this->size;
}

void QueueContainer::addNode(Node *node) {
    unique_lock<mutex> lock(mtx);

    isFull.wait(lock, [this] { return this->size < this->maxSize; });

    if (this->head == nullptr) {
        this->head = node;
        this->tail = node;
        this->size++;
    } else if (this->size == 1) {
        this->tail = node;
        this->head->setNextNode(tail);
        this->size++;
    } else if (this->size > 1) {
        this->tail->setNextNode(node);
        this->tail = node;
        this->size++;
    }
    hasActiveProducers.notify_one();
}

Node *QueueContainer::removeNode() {
    unique_lock<std::mutex> lock(mtx);

    //hasActiveProducers.wait(lock, [this] { return (this->size != 0) || !stillProducing; });

    while ((this->size == 0) && stillProducing) {
        hasActiveProducers.wait(lock);
    }

    if (this->size == 0) {
        return nullptr;
    }

    Node *node = this->head;
    if (this->size == 1) {
        this->head = nullptr;
        this->tail = nullptr;
        this->size = 0;
    } else {
        this->head = this->head->getNextNode();
        this->size--;
    }
    isFull.notify_one();
    return node;
}

void QueueContainer::doneProducing() {
    unique_lock<mutex> lock(mtx);

    this->activeProducers--;

    if (this->activeProducers == 0) {
        this->stillProducing = false;
        hasActiveProducers.notify_all();
    }
}
