#include <cmath>
#include <iostream>
#include <fstream>
#include "mpi.h"

using namespace std;

ifstream inI("input.txt");
ifstream inP("polinom.txt");

ofstream out("output.txt");

int main(int argc, char** argv) {

    int threads = atoi(argv[1]);
    int n,p;

    MPI_Init(&argc, &argv);
    int rank, P;
    MPI_Comm_size(MPI_COMM_WORLD, &P);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    int* coef = nullptr;
    int* val = nullptr;

    if (rank == 0 ) {
        if (inP.is_open()) {
            inP >> p ;
            coef = new int[p];
            for (int i = 0; i < p; i++) {
                inP >> coef[i];
            }
        }
        if (inI.is_open()) {
            inI >> n;
            val = new int[n];
            for (int i = 0; i < n; i++) {
                inI >> val[i];
            }
        }
    }
    MPI_Bcast(&p,1,MPI_INT,0,MPI_COMM_WORLD);
    MPI_Bcast(&n,1,MPI_INT,0,MPI_COMM_WORLD);

    if (rank != 0) {
        coef = new int[p];
    }

    int batchSize = n / threads;
    int* receiveValues  = new int[batchSize];

    MPI_Bcast(coef,p,MPI_INT,0,MPI_COMM_WORLD);
    MPI_Scatter(val,batchSize,MPI_INT,receiveValues,batchSize,MPI_INT,0,MPI_COMM_WORLD);



    for (int i = 0; i < batchSize; i++) {
        int value = 0 ;
            for (int j = 0; j < p; j++) {
                value += coef[j]*pow(receiveValues[i],j);
            }
        receiveValues[i] = value;
    }
    int * results = new int[n];
    MPI_Gather(receiveValues,batchSize,MPI_INT,results,batchSize,MPI_INT,0,MPI_COMM_WORLD);

    if (rank == 0) {
        for (int i = 0; i < n; i++) {
            out << results[i] << " ";
        }
    }

    MPI_Finalize();
}