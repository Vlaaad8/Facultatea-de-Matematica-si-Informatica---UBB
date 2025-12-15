#ifndef TEMA_4_CALCULATENOTESPARALLEL_H
#define TEMA_4_CALCULATENOTESPARALLEL_H
#include <barrier>
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

    void consumerThread(QueueContainer& queue, LinkedList& list,LinkedList& sortedList, barrier<> &wait_barrier);

    void readNodesFromFile(const string &fileName, QueueContainer &queue);
};


#endif
