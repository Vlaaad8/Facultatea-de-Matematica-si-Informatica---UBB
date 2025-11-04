#include "../header/ConvolutionH.h"
#include "../header/MatrixGenerator.h"
#include <barrier>

ConvolutionH::ConvolutionH(int N, int M, int K, int P, int **matrix, int **filter) {
    this->N = N;
    this->M = M;
    this->K = K;
    this->P = P;
    this->matrix = matrix;
    this->filter = filter;
    this->threads = new thread[P];
}

void ConvolutionH::run() {
    int rows_per_thread = N / P;
    int extra = N % P;
    int start_idx = 0;
    barrier<> wait_point(P);

    for (int i = 0; i < P; i++) {
        int end_idx = start_idx + rows_per_thread;
        if (extra > 0) {
            extra--;
            end_idx++;
        }


        threads[i] = thread([this, start_idx, end_idx,&wait_point]() {
            this->calculate_convolution(start_idx, end_idx, wait_point);
        });

        start_idx = end_idx;
    }


    for (int i = 0; i < P; i++) {
        threads[i].join();
    }
}

void ConvolutionH::calculate_convolution(int start_idx, int end_idx, barrier<> &wait_point) {
    int *previous_row = new int [M];
    int *current_row = new int [M];
    int *bellow_row = new int [M];

    int *up_row = nullptr;
    int *down_row = nullptr;

    if (start_idx > 0) {
        up_row = new int [M];
        for (int i = 0; i < M; i++) {
            up_row[i] = matrix[start_idx - 1][i];
        }
    }
    if (end_idx < N) {
        down_row = new int [M];
        for (int i = 0; i < M; i++) {
            down_row[i] = matrix[end_idx][i];
        }
    }

    wait_point.arrive_and_wait();

    if (start_idx == 0) {
        for (int i = 0; i < M; i++) {
            previous_row[i] = matrix[0][i];
        }
    } else {
        for (int i = 0; i < M; i++) {
            previous_row[i] = up_row[i];
        }
    }

    for (int i = start_idx; i < end_idx; i++) {
        for (int j = 0; j < M; j++) {
            current_row[j] = matrix[i][j];
        }
        if (i == end_idx - 1) {
            if (end_idx == N) {
                for (int j = 0; j < M; j++) {
                    bellow_row[j] = matrix[N - 1][j];
                }
            } else {
                for (int j = 0; j < M; j++) {
                    bellow_row[j] = down_row[j];
                }
            }
        } else {
            for (int j = 0; j < M; j++) {
                bellow_row[j] = matrix[i + 1][j];
            }
        }
        for (int j = 0; j < M; j++) {
           int value = compute_element(j,previous_row,current_row,bellow_row);
            matrix[i][j]=value;
        }
        swap(previous_row, current_row);

    }
    delete[] bellow_row;
    delete[] up_row;
    delete[] down_row;
    delete[] previous_row;
    delete[] current_row;
}

int ConvolutionH::compute_element(int j, int *prev_row, int *current_row, int *below_row) const {
    const int half = K / 2;
    int sum = 0;
    for (int a = -half; a <= half; a++) {
        for (int b = -half; b <= half; b++) {
            int y = j + b;
            if (y < 0) y = 0;
            if (y >= M) y = M - 1;
            int value = 0;
            if (a == -1) {
                value = prev_row[y];
            } else if (a == 0) {
                value = current_row[y];
            } else {
                value = below_row[y];
            }
            sum += value * filter[a + half][b + half];
        }
    }
    return sum;
}

int **ConvolutionH::get_matrix() const {
    return this->matrix;
}
