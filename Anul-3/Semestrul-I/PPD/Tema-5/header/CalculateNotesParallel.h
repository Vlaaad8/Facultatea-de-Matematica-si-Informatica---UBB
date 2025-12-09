#ifndef TEMA_4_CALCULATENOTESPARALLEL_H
#define TEMA_4_CALCULATENOTESPARALLEL_H
#include <thread>

#include "QueueContainer.h"


class CalculateNotesParallel {
private:
    int readerThreads;
    int maxSize;
    int P;;

public:
    CalculateNotesParallel(const int readerT,const int P,const int maxSize) {
        this->readerThreads = readerT;
        this->P = P;
        this->maxSize = maxSize;
    };
    void run();

    void consumerThread(QueueContainer& queue, LinkedList& list);

    void readNodesFromFile(const string &fileName, QueueContainer &queue);
};


#endif
