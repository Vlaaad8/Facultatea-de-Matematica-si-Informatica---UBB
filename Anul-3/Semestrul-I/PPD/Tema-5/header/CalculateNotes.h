#ifndef TEMA_4_CALCULATENOTES_H
#define TEMA_4_CALCULATENOTES_H
#include <vector>
#include <string>
#include "LinkedList.h"
using namespace std;

class CalculateNotes {
public:
    void run();
private:
    void readNodesFromFile(const string &fileName, LinkedList &list);
    void readNodesFromDataBase(const string &table,LinkedList &list);
};


#endif //TEMA_4_CALCULATENOTES_H