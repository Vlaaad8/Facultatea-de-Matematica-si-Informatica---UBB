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

    void writeToFile(const string &fileName);

    void addInOrder(Node* node);



    Node* extractFirstNode() {
        // 1. Blocăm santinela de început (Head)
        head->lockNode();
        Node* firstReal = head->getNextNode();

        // 2. Blocăm primul nod real (sau Tail dacă lista e goală)
        // Trebuie să blocăm înainte să verificăm, ca să nu modifice altcineva structura
        firstReal->lockNode();

        // Verificăm dacă lista e goală (am dat de santinela Tail)
        if (firstReal == tail) {
            firstReal->unlockNode();
            head->unlockNode();
            return nullptr; // Semnal că nu mai sunt noduri
        }

        // 3. Detașăm nodul din listă (Head va arăta spre următorul)
        head->setNextNode(firstReal->getNextNode());

        // 4. Deblocăm
        firstReal->unlockNode(); // Nodul e acum izolat, e al nostru
        head->unlockNode();      // Lista e liberă pentru alții

        // Curățăm legătura nodului extras (să nu mai arate spre restul listei vechi)
        firstReal->setNextNode(nullptr);

        return firstReal;
    }
};


#endif //TEMA_4_LINKEDLIST_H
