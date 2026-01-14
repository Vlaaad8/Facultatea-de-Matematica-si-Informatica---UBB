#include <iostream>
#include <fstream>
#include <string>
#include <random>
#include <chrono>
#include <cuda_runtime.h>

using namespace std;

int k = 3;

#define blockDimension 16
#define CORNER_AT(buffer, bx, by, gridX, c) buffer[(((by) * (gridX) + (bx)) * 4) + (c)]

void generateKernel(const string &fileName, int k) {
    ofstream outFilter(fileName);
    outFilter << k << " " << k << endl;
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            outFilter << rand() % 3 - 1 << " ";
        }
        outFilter << endl;
    }
    outFilter.close();
}

void generateMatrix(const string &fileName, int rows, int columns) {
    ofstream outMatrix(fileName);
    outMatrix << rows << " " << columns << endl;
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
            outMatrix << rand() % 10 << " ";
        }
        outMatrix << endl;
    }
    outMatrix.close();
}

void writeMatrix(const string& fileName,int rows,int columns, int* matrix) {
    ofstream outResult(fileName);
    outResult << rows << " " << columns << endl;
    for (int i = 0; i < rows; i++) {
        for (int j = 0 ; j < columns; j++) {
            outResult << matrix[i * columns + j] << " ";
        }
    outResult << endl;
    }
    outResult.close();
}


void readMatrix(const string &fileName, int * &data) {
    ifstream inFile(fileName);
    int rows, columns;
    inFile >> rows >> columns;
    data = new int[rows * columns];
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
            inFile >> data[i * columns + j];
        }
    }
    inFile.close();
}


int computeElement(int i, int j, int k, int N, int M, int* matrix, int* filter){
    const int half = k/2;
    int sum = 0;

    for(int a = -half; a <= half; a++){
        for(int b = -half; b <= half; b++){
            int x = i + a;
            int y = j + b;


            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x >= N) x = N - 1;
            if (y >= M) y = M - 1;


            sum += matrix[x * M + y] * filter[(a + half) * k + (b + half)];
        }
    }
    return sum;
}

void calculateSequential(int* matrix, int* output, int rows, int cols, int k , int* filter){
    for(int i = 0; i < rows; i++){
        for(int j = 0; j < cols; j++){
            output[i * cols + j] = computeElement(i, j, k, rows, cols, matrix, filter);
        }
    }
}

__global__ void calculateKernel(int *matrix, int n, int m, int *kernel,int* bufferColumns, int* bufferRows,int* bufferCorners, int gridX) {
    __shared__ int shared_block[blockDimension][blockDimension];

    int threadX = threadIdx.x;
    int threadY = threadIdx.y;

    int col = blockIdx.x * blockDim.x + threadX;
    int row = blockIdx.y * blockDim.y + threadY;

    if (row < n && col < m) {
        shared_block[threadY][threadX] = matrix[row * m + col];
    } else {
        shared_block[threadY][threadX] = 0;
    }

    __syncthreads();

    if (row < n && col < m) {
        int suma = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int val = 0;


                int neighRow = row + i;
                int neighCol = col + j;

                int readRow = neighRow;
                int readCol = neighCol;

                if (readRow < 0) readRow = 0;
                if (readRow >= n) readRow = n - 1;
                if (readCol < 0) readCol = 0;
                if (readCol >= m) readCol = m - 1;

                int localRow = threadY + (readRow - row);
                int localCol = threadX + (readCol - col);

                //Decidem de unde luam datele
                if (localRow >= 0 && localRow < blockDimension && localCol >= 0 && localCol < blockDimension) {
                    val = shared_block[localRow][localCol];
                }

                else {
                    int bx = blockIdx.x;
                    int by = blockIdx.y;


                    if (localRow < 0 && localCol < 0) {
                        if (bx > 0 && by > 0)
                            val = CORNER_AT(bufferCorners, bx - 1, by - 1, gridX, 3);
                    }
                    else if (localRow < 0 && localCol >= blockDimension) {
                        if (bx < gridDim.x - 1 && by > 0)
                            val = CORNER_AT(bufferCorners, bx + 1, by - 1, gridX, 2);
                    }
                    else if (localRow >= blockDimension && localCol < 0) {
                        if (bx > 0 && by < gridDim.y - 1)
                            val = CORNER_AT(bufferCorners, bx - 1, by + 1, gridX, 1);
                    }
                    else if (localRow >= blockDimension && localCol >= blockDimension) {
                        if (bx < gridDim.x - 1 && by < gridDim.y - 1)
                            val = CORNER_AT(bufferCorners, bx + 1, by + 1, gridX, 0);
                    }


                    else if (localRow < 0) { // Sus
                        if (by > 0)
                            val = bufferRows[(2 * (by - 1) + 1) * m + readCol];
                    }
                    else if (localRow >= blockDimension) { //Jos
                        if (by < gridDim.y - 1)
                            val = bufferRows[(2 * (by + 1)) * m + readCol];
                    }

                    else if (localCol < 0) { //Stanga
                        if (bx > 0)
                            val = bufferColumns[(2 * (bx - 1) + 1) * n + readRow];
                    }
                    else if (localCol >= blockDimension) { //Dreapta
                        if (bx < gridDim.x - 1)
                            val = bufferColumns[(2 * (bx + 1)) * n + readRow];
                    }
                }


                suma += val * kernel[(i + 1) * 3 + (j + 1)];
            }
        }

        matrix[row * m + col] = suma;
    }
}

__global__ void saveBordersKernel(int* matrix, int* bufferColumns, int* bufferRows,
                                  int* bufferCorners, int N, int M, int gridX){
    int threadX = threadIdx.x;
    int threadY = threadIdx.y;


    int col = blockIdx.x * blockDim.x + threadX;
    int row = blockIdx.y * blockDim.y + threadY;

    if (row >= N || col >= M) return;

    if (threadY == 0) {
        //SUS
        bufferRows[(2 * blockIdx.y) * M + col] = matrix[row * M + col];
    }
    if (threadY == blockDim.y - 1 || row == N - 1) {
        //JOS
        bufferRows[(2 * blockIdx.y + 1) * M + col] = matrix[row * M + col];
    }
    if (threadX == 0) {
        //STANGA
        bufferColumns[(2 * blockIdx.x) * N + row] = matrix[row * M + col];
    }

    if (threadX == blockDim.x - 1 || col == M - 1) {
        //DREAPTA
        bufferColumns[(2 * blockIdx.x + 1) * N + row] = matrix[row * M + col];
    }

    if (threadX == 0 && threadY == 0) {
        int bx = blockIdx.x;
        int by = blockIdx.y;

        int row0 = by * blockDim.y;
        int col0 = bx * blockDim.x;
        int row1 = min(row0 + (int)blockDim.y - 1, N - 1);
        int col1 = min(col0 + (int)blockDim.x - 1, M - 1);

        CORNER_AT(bufferCorners, bx, by, gridX, 0) = matrix[row0 * M + col0];
        CORNER_AT(bufferCorners, bx, by, gridX, 1) = matrix[row0 * M + col1];
        CORNER_AT(bufferCorners, bx, by, gridX, 2) = matrix[row1 * M + col0];
        CORNER_AT(bufferCorners, bx, by, gridX, 3) = matrix[row1 * M + col1];
    }



}

bool filesAreEqual(const string &file_name1, const string &file_name2) {
    ifstream f1(file_name1);
    ifstream f2(file_name2);
    if (!f1.is_open() || !f2.is_open()) {
        cout << "Error in opening files" << endl;
        return false;
    }
    f1.seekg(0, ios::end);
    f2.seekg(0, ios::end);
    if (f1.tellg() != f2.tellg()) {
        cout << "Not equal"<<endl;
        return false;
    }
    f1.seekg(0, ios::beg);
    f2.seekg(0, ios::beg);

    char c1,c2;
    while (f1.get(c1) && f2.get(c2)) {
        if (c1 != c2) {
            return false;
        }
    }
    cout<< "Equal"<<endl;
    return true;
}