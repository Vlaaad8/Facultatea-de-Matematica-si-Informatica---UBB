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
    int **new_matrix;

public:
    ConvolutionS(int N, int M, int K, int **matrix, int **filter);

    int compute_element(int i, int jt);


    void calculate_convolutionD(const string &result_file);

    int** getNewMatrix();
    ~ConvolutionS();
};


#endif //C___CONVOLUTIONS_H
