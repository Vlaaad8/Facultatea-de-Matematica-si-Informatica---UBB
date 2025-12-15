//
// Created by vladb on 17/11/2025.
//

#include "../header/CalculateNotes.h"
#include "../header/LinkedList.h"
#include <string>
#include <fstream>
#include <iostream>
#include <vector>

using namespace std;


void CalculateNotes::run() {
    LinkedList list = LinkedList();
    LinkedList sortedList = LinkedList();

    for (int i = 1; i <= 10; i++) {
        string fileName = "Input/project" + to_string(i) + ".txt";
        readNodesFromFile(fileName, list);
    }

    while (true) {
        Node* node = list.extractFirstNode();
        if (node == nullptr) {
            break;
        }
        sortedList.addInOrder(node);
    }

    sortedList.writeToFile("Results/resultS.txt");
}


void CalculateNotes::readNodesFromFile(const string &fileName, LinkedList &list) {
    ifstream in(fileName);

    if (!in.is_open()) {
        cout << "Cannot open file: " << fileName << endl;
    }

    int id, nota;

    while (in >> id >> nota) {
        Node *node = new Node(id, nota);
        list.addOrUpdateNode(node);
    }

    in.close();
}
