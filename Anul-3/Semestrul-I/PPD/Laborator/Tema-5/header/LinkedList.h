#ifndef TEMA_4_LINKEDLIST_H
#define TEMA_4_LINKEDLIST_H
#include <mutex>
#include <string>
using namespace std;

class Node {
private:
    int id;
    int note;
    Node *next;
    mutex nodeLock;

public:
    Node(int id, int note);

    void modifyNote(int note1);

    void setNextNode(Node *node);

    int getId() const;

    int getNote() const;

    Node *getNextNode() const;

    void lockNode();
    void unlockNode();
};

class LinkedList {
private:
    Node *head;
    Node *tail;
    int size;

public:
    LinkedList() {
        head = new Node(-1,-1000);
        tail = new Node(9999, 9999);
        head->setNextNode(tail);
        tail->setNextNode(nullptr);
        size = 0;
    }

    Node *getNodeById(int id) const;

    void addOrUpdateNode(Node *node);

    int getSize() const;

    Node *getHead();

    Node* extractFirstNode();

    void writeToFile(const string &fileName);

    void addInOrder(Node* node);

};


#endif //TEMA_4_LINKEDLIST_H
