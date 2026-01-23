//
// Created by vladb on 18/10/2025.
//

#ifndef C___CONVOLUTIONH_H
#define C___CONVOLUTIONH_H

#include <string>
#include <thread>
using namespace std;

class ConvolutionH {
private:
    int N;
    int M;
    int K;
    int P;
    int **matrix;
    int **filter;
    int **new_matrix;
    thread *threads;

    void calculate_convolutionD(int start_idx, int end_idx);

    int compute_element(int i, int j);

public:
    ConvolutionH(int N, int M, int K, int P, int **matrix, int **filter);

    void run();
    int ** getNewMatrix();
};

#endif //C___CONVOLUTIONH_H
