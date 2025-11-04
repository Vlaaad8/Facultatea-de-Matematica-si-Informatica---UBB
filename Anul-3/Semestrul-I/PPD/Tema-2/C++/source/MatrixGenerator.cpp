//
// Created by vladb on 18/10/2025.
//

#include "../header/MatrixGenerator.h"
#include <string>
#include <fstream>
#include <chrono>
using namespace std;

MatrixGenerator::MatrixGenerator(const string &matrix_file, const string &filter_file, int n, int m, int k)
    : matrix_file(matrix_file), filter_file(filter_file), rows(n), cols(m), k(k) {}


void MatrixGenerator::generateMatrix() const {
    ofstream outM(matrix_file);
    outM << rows << " " << cols<<endl;
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            outM << rand() % 20 << " ";
        }
        outM << endl;
    }




}
void MatrixGenerator::generateFilter() const {
    ofstream outFilter(filter_file);
    outFilter << k << " " << k<<endl;
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            outFilter << rand() % 100 << " ";
        }
        outFilter << endl;
    }
}
void MatrixGenerator::writeMatrixToFile(int **matrix, const string &matrix_file,const int rows,const int cols) {
    ofstream outM(matrix_file);
    for (int i=0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            outM << matrix[i][j] << " ";
        }
        outM << endl;
    }
}

