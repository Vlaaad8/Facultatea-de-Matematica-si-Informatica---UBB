//
// Created by vladb on 18/10/2025.
//

#ifndef C___CONVOLUTIONV_H
#define C___CONVOLUTIONV_H
#include <thread>
using namespace std;

class ConvolutionV {
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
    ConvolutionV(int N, int M, int K, int P, int **matrix, int **filter);

    void run();

    int** getNewMatrix();

    ~ConvolutionV();

};


#endif //C___CONVOLUTIONV_H