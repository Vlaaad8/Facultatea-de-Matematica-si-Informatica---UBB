//
// Created by vladb on 18/11/2025.
//

#ifndef TEMA_4_CALCULATENOTESPARALLEL_H
#define TEMA_4_CALCULATENOTESPARALLEL_H
#include <thread>

#include "QueueContainer.h"


class CalculateNotesParallel {
private:
    int readerThreads;
    int P;;

public:
    CalculateNotesParallel(int readerT,int P) {
        this->readerThreads = readerT;
        this->P = P;
    };
    void run();

    void producerThread(int startIndex, int endIndex,QueueContainer& queue);
    void consumerThread(QueueContainer& queue, LinkedList& list);

    void readNodesFromFile(const string &fileName, QueueContainer &queue);
};


#endif //TEMA_4_CALCULATENOTESPARALLEL_H
