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

public:
    Node(int id, int note);

    void modifyNote(int note1);

    void setNextNode(Node *node);

    int getId() const;

    int getNote() const;

    Node *getNextNode() const;
};

class LinkedList {
private:
    Node *head;
    int size;
    mutex mtx;

public:
    LinkedList() {
        head = nullptr;
        size = 0;
    }

    Node *getNodeById(int id) const;

    void addOrUpdateNode(Node *node);

    int getSize() const;

    Node *getHead();

    void writeToFile(const string &fileName);
};


#endif //TEMA_4_LINKEDLIST_H
