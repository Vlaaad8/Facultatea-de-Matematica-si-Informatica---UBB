#include "../header/ConvolutionS.h"
#include "../header/MatrixGenerator.h"

ConvolutionS::ConvolutionS(int N, int M, int K, int **matrix, int **filter) {
    this->N = N;
    this->M = M;
    this->K = K;
    this->matrix = matrix;
    this->filter = filter;
}


void ConvolutionS::calculate_convolution() {
    int *previous_row = new int[M];
    int *current_row = new int[M];
    int* below_row = new int[M];

    for (int i = 0; i < M; i++) {
        previous_row[i] = matrix[0][i];
    }


    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            current_row[j] = matrix[i][j];
        }
        int id;
        if (i < N - 1) {
            id = i + 1;
        } else {
            id = N-1;
        }

        for (int z=0;z<M;z++) {
            below_row[z]=matrix[id][z];
        }

        for (int j = 0; j < M; j++) {
            int value = compute_element(j, previous_row, current_row, below_row);
            matrix[i][j] = value;
        }
        swap(previous_row, current_row);
    }
    delete[] below_row;
    delete[] previous_row;
    delete[] current_row;
}


int ConvolutionS::compute_element(int j, int *prev_row, int *current_row, int *below_row) const {
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

int **ConvolutionS::get_matrix() const {
    return this->matrix;
}

