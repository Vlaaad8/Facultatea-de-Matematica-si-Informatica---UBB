//
// Created by vladb on 18/10/2025.
//

#ifndef C___ESTIMATETIME_H
#define C___ESTIMATETIME_H
#include <pthread.h>


class EstimateTime {
private:
    int **matrix;
    int **filter;
    int P;
    int N;
    int M;
    int K;

public:
    EstimateTime(int N, int M,int P,int K);
    void run();
    double estimate_conv_dyn_S() ;
    double estimate_conv_dyn_H(int threads) ;
    double estimate_conv_dyn_V(int threads) ;
};


#endif //C___ESTIMATETIME_H
