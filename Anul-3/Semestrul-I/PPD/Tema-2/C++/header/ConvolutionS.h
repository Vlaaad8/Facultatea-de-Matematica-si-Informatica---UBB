//
// Created by vladb on 18/10/2025.
//

#ifndef C___CONVOLUTIONS_H
#define C___CONVOLUTIONS_H
#include <string>
using namespace std;

class ConvolutionS {
private:
    int N;
    int M;
    int K;
    int **matrix;
    int **filter;

public:
    ConvolutionS(int N, int M, int K, int **matrix, int **filter);

    int compute_element(int j,int* prev_row,int* current_row, int* below_row) const;


    void calculate_convolution();

    int ** get_matrix() const;

};


#endif //C___CONVOLUTIONS_H
