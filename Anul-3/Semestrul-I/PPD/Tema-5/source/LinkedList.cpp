#include "../header/LinkedList.h"
#include <mutex>
#include <fstream>
#include <iostream>
using namespace std;

void Node::modifyNote(const int note1) {
    if (this->note == 0) {
        this->note = note1;
    } else {
        this->note += note1;
    }
}

void Node::setNextNode(Node *node) {
    this->next = node;
}

int Node::getId() const {
    return this->id;
}

int Node::getNote() const {
    return this->note;
}

Node *Node::getNextNode() const {
    return this->next;
}

Node::Node(const int id, const int note) {
    this->id = id;
    this->note = note;
    this->next = nullptr;
}

Node *LinkedList::getNodeById(const int id) const {
    Node *currentNode = this->head;
    while (currentNode != nullptr) {
        if (currentNode->getId() == id) {
            return currentNode;
        }
        currentNode = currentNode->getNextNode();
    }
    return nullptr;
}

void Node::lockNode() {
    this->nodeLock.lock();
}

void Node::unlockNode() {
    this->nodeLock.unlock();
}


void LinkedList::addInOrder(Node *node) {
    Node *previousNode = head;
    previousNode->lockNode();
    Node *currentNode = head->getNextNode();
    currentNode->lockNode();

    while (currentNode != tail && currentNode->getNote() > node->getNote()) {
        Node *tempNode = previousNode;
        previousNode = currentNode;

        currentNode = currentNode->getNextNode();
        currentNode->lockNode();

        tempNode->unlockNode();
    }
    this->size++;
    node->setNextNode(previousNode->getNextNode());
    previousNode->setNextNode(node);

    currentNode->unlockNode();
    previousNode->unlockNode();
}


void LinkedList::addOrUpdateNode(Node *node) {
    Node *previousNode = head;
    previousNode->lockNode();
    Node *currentNode = head->getNextNode();
    currentNode->lockNode();

    while (currentNode != tail && currentNode->getId() < node->getId()) {
        Node *tempNode = previousNode;
        previousNode = currentNode;

        currentNode = currentNode->getNextNode();
        currentNode->lockNode();

        tempNode->unlockNode();
    }

    if (currentNode->getId() == node->getId()) {
        currentNode->modifyNote(node->getNote());

        currentNode->unlockNode();
        previousNode->unlockNode();

        delete node;
    } else {
        this->size++;
        node->setNextNode(previousNode->getNextNode());
        previousNode->setNextNode(node);

        currentNode->unlockNode();
        previousNode->unlockNode();
    }
}

Node *LinkedList::extractFirstNode() {
    this->head->lockNode();

    Node* firstReal = this->head->getNextNode();
    firstReal->lockNode();

    if (firstReal == this->tail) {
        firstReal->unlockNode();
        head->unlockNode();
        return nullptr;
    }
    head->setNextNode(firstReal->getNextNode());
    firstReal->unlockNode();
    head->unlockNode();

    firstReal->setNextNode(nullptr);

    return firstReal;
}

Node *LinkedList::getHead() {
    return this->head;
}

int LinkedList::getSize() const {
    return this->size;
}

void LinkedList::writeToFile(const string &fileName) {
    // lock_guard<mutex> lock(mtx);

    ofstream out(fileName);
    if (!out.is_open()) {
        cout << "Cannot open file: " << fileName << endl;
        return;
    }

    Node *current = head;
    while (current != nullptr) {
        out << current->getId() << " " << current->getNote() << '\n';
        current = current->getNextNode();
    }

    out.close();
}
