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
    lock_guard<mutex> guard(mtx);
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
}

Node *QueueContainer::removeNode() {
    while (true) {

        std::lock_guard<std::mutex> guard(mtx);

        if (this->size > 0) {
            if (this->size == 1) {
                Node *node = this->head;
                this->head = nullptr;
                this->tail = nullptr;
                this->size = 0;
                return node;
            } else {
                Node *node = this->head;
                this->head = this->head->getNextNode();
                this->size--;
                return node;
            }
        }
        if (activeProducers.load() == 0){
            return nullptr;
        }
    }
}
void QueueContainer::doneProducing() {
    this->activeProducers--;
}
