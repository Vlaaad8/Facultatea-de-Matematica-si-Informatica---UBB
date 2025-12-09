#include <iostream>
#include <fstream>
#include <cmath>


#include "mpi.h"
using namespace std;

ofstream out("output.txt");
int main(int argc, char *argv[]) {

    if (argc < 3) {
        exit(1);
    }

    int n = atoi (argv[1]);
    int threads = atoi (argv[2]);


    MPI_Init(&argc, &argv);


    int p, rank;
    double* coef  = new double[n];
    MPI_Comm_size(MPI_COMM_WORLD, &p);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    if (rank == 0) {
        ifstream inP("polinom.txt");

        for (int i = 0; i < n; i++) {
            inP >> coef[i];
        }



        ifstream inD("input.txt");
        auto* date = new double[n];

        for (int i = 0; i < n; i++) {
            inD >> date[i];
        }

        int batchSize = n/ threads;
        int start = batchSize;

        double* dataToKeep = new double[batchSize];

        for (int i = 0; i < batchSize; i++) {
            dataToKeep[i] = date[i];
        }

        MPI_Bcast(coef, n, MPI_DOUBLE, 0, MPI_COMM_WORLD);



        for (int i = 0; i < batchSize; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += coef[j] *pow(dataToKeep[i], j);
            }
            out<<sum<<" ";
        }

        for (int i = 1 ; i < threads; i++) {
            MPI_Send(date+start, batchSize, MPI_DOUBLE, i, 1, MPI_COMM_WORLD);
            start += batchSize;
        }


        for (int i = 1 ; i < threads; i++) {
            auto* tmp = new double[batchSize];
            MPI_Recv(tmp, batchSize, MPI_DOUBLE, i, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            for (int j = 0; j < batchSize; j++) {
                out << tmp[j] << " ";
            }
            delete[] tmp;
        }


        delete[] date;
        delete[] dataToKeep;

    }
    else {
        MPI_Bcast(coef, n, MPI_DOUBLE, 0, MPI_COMM_WORLD);
        int batchSize = n / threads;
        double* data = new double[batchSize];

        MPI_Recv(data,batchSize,MPI_DOUBLE,0,1,MPI_COMM_WORLD,MPI_STATUS_IGNORE);


        for (int i = 0; i < batchSize; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += coef[j] *pow(data[i], j);
            }
           data[i] = sum;
        }
        MPI_Send(data,batchSize,MPI_DOUBLE,0,2,MPI_COMM_WORLD);

    delete[] data;
    }
    delete[] coef;
    MPI_Finalize();
}
