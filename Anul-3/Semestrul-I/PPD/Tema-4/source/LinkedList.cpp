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

//TODO refactor this and be sure all cases are covered
// void LinkedList::addOrUpdateNode(Node *node) {
//     lock_guard<mutex> lock(mtx);
//     Node *currentNode = this->head;
//     Node *previousNode = nullptr;
//
//     while (currentNode != nullptr && currentNode->getId() < node->getId()) {
//         previousNode = currentNode;
//         currentNode = currentNode->getNextNode();
//     }
//     if (currentNode != nullptr && currentNode->getId() == node->getId()) {
//         currentNode->modifyNote(node->getNote());
//     } else {
//         this->size++;
//         if (previousNode == nullptr) {
//             this->head = node;
//         } else {
//             Node *nextNode = previousNode->getNextNode();
//             previousNode->setNextNode(node);
//             if (nextNode != nullptr) {
//                 node->setNextNode(nextNode);
//             }
//         }
//     }
// }
void LinkedList::addOrUpdateNode(Node *node) {
    lock_guard<mutex> lock(mtx);
    Node *currentNode = this->head;
    Node *previousNode = nullptr;

    while (currentNode != nullptr && currentNode->getId() < node->getId()) {
        previousNode = currentNode;
        currentNode = currentNode->getNextNode();
    }

    if (currentNode != nullptr && currentNode->getId() == node->getId()) {
        currentNode->modifyNote(node->getNote());
        delete node;
    } else {
        this->size++;
        if (previousNode == nullptr) {
            node->setNextNode(this->head);
            this->head = node;
        } else {
            node->setNextNode(previousNode->getNextNode());
            previousNode->setNextNode(node);
        }
    }
}


Node *LinkedList::getHead() {
    return this->head;
}

int LinkedList::getSize() const {
    return this->size;
}

void LinkedList::writeToFile(const string &fileName) {
    lock_guard<mutex> lock(mtx);

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
