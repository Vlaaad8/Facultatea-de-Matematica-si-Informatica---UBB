#include "../header/ConvolutionH.h"
#include "../header/MatrixGenerator.h"

ConvolutionH::ConvolutionH(int N, int M, int K, int P, int **matrix, int **filter) {
    this->N = N;
    this->M = M;
    this->K = K;
    this->P = P;
    this->matrix = matrix;
    this->filter = filter;
    this->threads = new thread[P];
    this->new_matrix = new int*[N];
    for (int i = 0; i < N; i++) {
        this->new_matrix[i] = new int[M];
    }
}

void ConvolutionH::run() {


    int rows_per_thread = N / P;
    int extra = N % P;
    int start_idx = 0;


    for (int i = 0; i < P; i++) {
        int end_idx = start_idx + rows_per_thread;
        if (extra > 0) {
            extra--;
            end_idx++;
        }


        threads[i] = thread([this, start_idx, end_idx]() {
            this->calculate_convolutionD(start_idx, end_idx);
        });

        start_idx = end_idx;
    }


    for (int i = 0; i < P; i++) {
        threads[i].join();
    }


    //MatrixGenerator::writeMatrixToFile(new_matrix, "resultHD.txt", N, M);


    // for (int i = 0; i < N; i++) {
    //     delete[] new_matrix[i];
    // }
    // delete[] new_matrix;
    // delete[] threads;
    // delete[] matrix;
    // delete[] filter;
}

void ConvolutionH::calculate_convolutionD(int start_idx, int end_idx) {
    for (int i = start_idx; i < end_idx; i++) {
        for (int j = 0; j < M; j++) {
            new_matrix[i][j] = compute_element(i, j);
        }
    }
}

int ConvolutionH::compute_element(int i, int j) {
    const int half = K / 2;
    int sum = 0;

    for (int a = -half; a <= half; a++) {
        for (int b = -half; b <= half; b++) {
            int x = i + a;
            int y = j + b;


            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x >= N) x = N - 1;
            if (y >= M) y = M - 1;

            sum += matrix[x][y] * filter[a + half][b + half];
        }
    }
    return sum;
}
int** ConvolutionH::getNewMatrix() {
    return new_matrix;
}
