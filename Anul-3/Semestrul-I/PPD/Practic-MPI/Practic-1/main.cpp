#include <iostream>
#include <fstream>

#include "GenerateData.h"
#include "mpi.h"

using namespace std;

int main(int argc, char *argv[]) {
    if (argc < 5) {
        exit(1);
    }

    int N = atoi(argv[1]);
    int n = atoi(argv[2]);
    int a = atoi(argv[3]);
    int b = atoi(argv[4]);
    int M = 2 * N;

    MPI_Init(NULL, NULL);
    int rank, P;
    MPI_Comm_size(MPI_COMM_WORLD, &P);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    if (rank == 0) {
        GenerateData::generateData("inputA.txt",n,a);
        GenerateData::generateData("inputB.txt",n,b);
    }

    MPI_Barrier(MPI_COMM_WORLD);

    if (rank == 0) {
        ifstream inA("inputA.txt");

        double *data = new double[n];

        int a_file, n_file;
        inA >> n_file >> a_file;

        for (int i = 0; i < n; i++) {
            inA >> data[i];
        }

        int batchSize = n / N;
        int start = batchSize;

        double *dataToKeep = new double[batchSize];


        for (int i = 0; i < batchSize; i++) {
            double newValue = 1;
            for (int j = 1; j <= a_file; j++) {
                newValue *= data[i];
            }
            dataToKeep[i] = newValue;
        }

        for (int i = 2; i < M; i = i + 2) {
            MPI_Send(&a_file, 1,MPI_INT, i, 1,MPI_COMM_WORLD);
            MPI_Send(data + start, batchSize, MPI_DOUBLE, i, 0, MPI_COMM_WORLD);
            start = start + batchSize;
        }

        double totalSum = 0;

        for (int i = 2; i < M; i = i + 2) {
            double tmp;
            MPI_Recv(&tmp, 1, MPI_DOUBLE, i, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            totalSum += tmp;
        }
        double *dataFromB = new double[batchSize];

        MPI_Recv(dataFromB, batchSize, MPI_DOUBLE, 1, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        double tmp = 0;

        for (int i = 0; i < batchSize; i++) {
            tmp += dataFromB[i] * dataToKeep[i];
        }
        totalSum += tmp;

        cout << "Total sum is " << totalSum << endl;


        delete[] data;
        delete[] dataFromB;
        delete[] dataToKeep;
    } else if (rank == 1) {
        ifstream inB("inputB.txt");

        double *data = new double[n];

        int b_file, n_file;

        inB >> n_file >> b_file;

        for (int i = 0; i < n; i++) {
            inB >> data[i];
        }
        int batchSize = n / N;
        int start = batchSize;

        double *dataToKeep = new double[batchSize];

        for (int i = 0; i < batchSize; i++) {
            double newValue = 1;
            for (int j = 1; j <= b_file; j++) {
                newValue *= data[i];
            }
            dataToKeep[i] = newValue;
        }

        for (int i = 3; i < M; i = i + 2) {
            MPI_Send(&b_file, 1,MPI_INT, i, 1,MPI_COMM_WORLD);
            MPI_Send(data + start, batchSize, MPI_DOUBLE, i, 0, MPI_COMM_WORLD);
            start = start + batchSize;
        }

        MPI_Send(dataToKeep, batchSize, MPI_DOUBLE, 0, 2, MPI_COMM_WORLD);

        delete[] data;
        delete[] dataToKeep;
    } else {
        int batchSize = n / N;
        double *data = new double[batchSize];
        int receiveFlag = rank % 2;
        int power;

        MPI_Recv(&power, 1, MPI_INT, receiveFlag, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(data, batchSize,MPI_DOUBLE, receiveFlag, 0,MPI_COMM_WORLD,MPI_STATUS_IGNORE);

        for (int i = 0; i < batchSize; i++) {
            double newValue = 1;
            for (int j = 1; j <= power; j++) {
                newValue *= data[i];
            }
            data[i] = newValue;
        }

        if (receiveFlag) {
            MPI_Send(data, batchSize, MPI_DOUBLE, rank - 1, 2, MPI_COMM_WORLD);
        } else {
            double *receivedData = new double[batchSize];
            MPI_Recv(receivedData, batchSize, MPI_DOUBLE, rank + 1, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            double computed = 0;
            for (int i = 0; i < batchSize; i++) {
                computed += (receivedData[i] * data[i]);
            }

            MPI_Send(&computed, 1, MPI_DOUBLE, 0, 3, MPI_COMM_WORLD);

            delete[] receivedData;
        }


        delete[] data;
    }
    MPI_Finalize();
}
