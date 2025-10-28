//
// Created by vladb on 18/10/2025.
//

#ifndef C___CONVOLUTIONH_H
#define C___CONVOLUTIONH_H

#include <barrier>
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
    thread *threads;

    void calculate_convolution(int start_idx, int end_idx, barrier<> &wait_point);

    int compute_element(int j,int* prev_row,int* current_row, int* below_row) const;

public:
    ConvolutionH(int N, int M, int K, int P, int **matrix, int **filter);

    void run();
    int** get_matrix() const;
};

#endif //C___CONVOLUTIONH_H
