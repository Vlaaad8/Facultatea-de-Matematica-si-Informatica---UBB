#include "../header/EstimateTime.h"
#include "../header/ReadFromFile.h"
#include "../header/ConvolutionS.h"
#include <chrono>

#include "../header/ConvolutionH.h"
#include <iostream>

#include "../header/MatrixGenerator.h"
using namespace std::chrono;
using namespace std;

EstimateTime::EstimateTime(int N, int M, int P, int K) {
    this->N = N;
    this->K = K;
    this->P = P;
    this->M = M;
}

void EstimateTime::run() {
    cout << "Tip Matrice N=" << N << "; M=" << M << endl;
    cout << "Tip filter: n=m=" << K << endl;
    cout << "Alocare Dinamica" << endl;
    cout << "Secvential: " << estimate_conv_dyn_S() << "ms" << endl;
    cout << "Thread Orizontal cu P=" << P << ": " << estimate_conv_dyn_H(P) << "ms" << endl;
}

double EstimateTime::estimate_conv_dyn_S() {
    this->matrix = ReadFromFile::readMatrix("matrix.txt");
    this->filter = ReadFromFile::readMatrix("filter.txt");
    auto start_time = high_resolution_clock::now();
    ConvolutionS convolution_s(N, M, K, matrix, filter);
    convolution_s.calculate_convolution();
    auto end_time = high_resolution_clock::now();
    duration<double, milli> round_time = end_time - start_time;
    int **new_matrix = convolution_s.get_matrix();
    MatrixGenerator::writeMatrixToFile(new_matrix, "resultS.txt", N, M);
    return round_time.count();
}

double EstimateTime::estimate_conv_dyn_H(const int threads) {
    this->matrix = ReadFromFile::readMatrix("matrix.txt");
    this->filter = ReadFromFile::readMatrix("filter.txt");
    auto start_time = high_resolution_clock::now();
    ConvolutionH convolution_h(N, M, K, threads, matrix, filter);
    convolution_h.run();
    auto end_time = high_resolution_clock::now();
    duration<double, milli> round_time = end_time - start_time;
    int **new_matrix = convolution_h.get_matrix();
    MatrixGenerator::writeMatrixToFile(new_matrix, "resultH.txt", N, M);
    return round_time.count();
}
