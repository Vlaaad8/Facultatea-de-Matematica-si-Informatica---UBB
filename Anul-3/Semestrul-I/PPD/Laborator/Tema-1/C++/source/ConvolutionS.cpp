//
// Created by vladb on 18/10/2025.
//

#include "../header/ConvolutionS.h"

#include <pthread.h>

#include "../header/MatrixGenerator.h"

ConvolutionS::ConvolutionS(int N, int M, int K, int** matrix, int** filter) {
    this->N = N;
    this->M = M;
    this->K = K;
    this->matrix = matrix;
    this->filter = filter;
    this->new_matrix = new int*[N];
    for (int i = 0; i < N; i++) {
        this->new_matrix[i] = new int[M];
    }
}


void ConvolutionS::calculate_convolutionD(const string &result_file) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            new_matrix[i][j] = compute_element(i,j);
        }
    }
    //MatrixGenerator::writeMatrixToFile(new_matrix, "resultSD", N, M);

    //delete[] new_matrix;
}



int ConvolutionS::compute_element(int i, int j) {
    const int half = K/ 2;
    int sum = 0;

    for (int a = -half; a <= half; a++) {
        for (int b = -half; b <= half; b++) {
            int x = i + a;
            int y = j + b;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x >= N) x = N - 1;
            if (y >= M) y = M - 1;
            sum+=matrix[x][y] * filter[a+half][b+half];
        }
    }
    return sum;
}
int** ConvolutionS::getNewMatrix() {
    return new_matrix;
}
ConvolutionS::~ConvolutionS() {
    for (int i = 0; i < N; i++)
        delete[] new_matrix[i];
    delete[] new_matrix;
}
