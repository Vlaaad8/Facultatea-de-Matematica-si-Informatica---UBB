#include <iostream>
#include <fstream>
#include "mpi.h"

using namespace std;


ifstream inA("inputA.txt");
ifstream inB("inputB.txt");


int main(const int argc, char **argv) {
    int threads = atoi(argv[1]);
    int M = threads * 2;

    MPI_Init(&argc, &argv);
    int rank, P;

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &P);


    if (rank == 0) {
        int a, n;
        inA >> a >> n;
        int *dateA = new int[n];

        for (int i = 0; i < n; i++) {
            inA >> dateA[i];
        }

        int batchSize = n / threads;
        int start = batchSize;

        int *mySlice = new int[batchSize];

        for (int i = 0; i < batchSize; i++) {
            int newValue = 1;
            for (int j = 1; j <= a; j++) {
                newValue *= dateA[i];
            }
            mySlice[i] = newValue;
        }


        for (int i = 2; i < M; i = i + 2) {
            MPI_Send(&a, 1,MPI_INT, i, 0,MPI_COMM_WORLD);
            MPI_Send(&batchSize, 1,MPI_INT, i, 2,MPI_COMM_WORLD);
            MPI_Send(dateA + start, batchSize,MPI_INT, i, 1,MPI_COMM_WORLD);
            start += batchSize;
        }
        int bSize;

        MPI_Recv(&bSize, 1,MPI_INT, 1, 5,MPI_COMM_WORLD,MPI_STATUS_IGNORE);
        int *bPart = new int[bSize];
        MPI_Recv(bPart, bSize,MPI_INT, 1, 6,MPI_COMM_WORLD,MPI_STATUS_IGNORE);

        int total_sum = 0;
        for (int i = 0; i < bSize; i++) {
            total_sum += bPart[i] * mySlice[i];
        }

        for (int i = 2; i < M; i = i + 2) {
            int tmp;
            MPI_Recv(&tmp, 1, MPI_INT, i, 4,MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            total_sum += tmp;
        }
        cout << total_sum << endl;
    }
    else if (rank == 1) {
        int b, n;
        inB >> b >> n;
        int *dateB = new int[n];
        for (int i = 0; i < n; i++) {
            inB >> dateB[i];
        }
        int batchSize = n / threads;
        int start = batchSize;

        int *mySlice = new int[batchSize];

        for (int i = batchSize; i < 2 * batchSize; i++) {
            int newValue = 1;
            for (int j = 1; j <= b; j++) {
                newValue *= dateB[i];
            }
            mySlice[i-batchSize] = newValue;
        }


        for (int i = 3; i < M; i = i + 2) {
            MPI_Send(&b, 1,MPI_INT, i, 0,MPI_COMM_WORLD);
            MPI_Send(&batchSize, 1,MPI_INT, i, 2,MPI_COMM_WORLD);
            MPI_Send(dateB + start, batchSize,MPI_INT, i, 1,MPI_COMM_WORLD);
            start += batchSize;
        }
        MPI_Send(&batchSize, 1,MPI_INT, 0, 5,MPI_COMM_WORLD);
        MPI_Send(mySlice, batchSize,MPI_INT, 0, 6,MPI_COMM_WORLD);
    } else {
        int fromWho = rank % 2;
        int power, batchSize;

        MPI_Recv(&power, 1, MPI_INT, fromWho, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(&batchSize, 1, MPI_INT, fromWho, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        int *data = new int[batchSize];

        MPI_Recv(data, batchSize, MPI_INT, fromWho, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        for (int i = 0; i < batchSize; i++) {
            int newValue = 1;
            for (int j = 1; j <= power; j++) {
                newValue *= data[i];
            }
            data[i] = newValue;
        }

        if (rank % 2 == 1) {
            MPI_Send(data, batchSize,MPI_INT, rank - 1, 3, MPI_COMM_WORLD);
        } else {
            int *receive_data = new int[batchSize];
            MPI_Recv(receive_data, batchSize,MPI_INT, rank + 1, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            int partial_sum = 0;
            for (int i = 0; i < batchSize; i++) {
                partial_sum += receive_data[i] * data[i];
            }

            MPI_Send(&partial_sum, 1, MPI_INT, 0, 4, MPI_COMM_WORLD);
        }
    }
    MPI_Finalize();
}
