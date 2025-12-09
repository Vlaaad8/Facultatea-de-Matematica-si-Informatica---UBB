#include <iostream>
#include <fstream>

#include "GenerateData.h"
#include "mpi.h"


using namespace std;

ifstream in("input.txt");
ofstream out("output.txt");

int processToReceive(int totalThreads, int rows, int thread) {
    int total = 0;
    for (int i = 0; i < rows; i++) {
        if ((i % (totalThreads - 1) + 1) == thread) {
            total++;
        }
    }
    return total;
}


int main(int argc, char **argv) {
    int threads = atoi(argv[1]);
    int N = atoi(argv[2]);

    MPI_Init(&argc, &argv);

    int P, rank;


    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &P);

    if (rank == 0 ) {
        GenerateData::generate("input.txt", N);
    }
    MPI_Barrier(MPI_COMM_WORLD);

    if (rank == 0) {
        if (in.is_open()) {
            int computed_N;
            in >> computed_N;
            int **data = new int *[computed_N];

            auto* send_request = new MPI_Request[computed_N];
            auto *receive_request = new MPI_Request[computed_N];

            for (int i = 0; i < computed_N; i++) {
                data[i] = new int[N];

                for (int j = 0; j < computed_N; j++) {
                    in >> data[i][j];
                }
                int processToSend = i % (threads - 1) + 1;

                MPI_Isend(data[i], computed_N,MPI_INT, processToSend, 1,MPI_COMM_WORLD, &send_request[i]);
            }

            int *dataR = new int[computed_N];

            for (int i = 0; i < computed_N; i++) {
                int processFromReceive = i % (threads - 1) + 1;

                MPI_Irecv(&dataR[i], 1,MPI_INT, processFromReceive, 2,MPI_COMM_WORLD, &receive_request[i]);
            }



            MPI_Waitall(computed_N, send_request, MPI_STATUS_IGNORE);
            MPI_Waitall(computed_N, receive_request, MPI_STATUS_IGNORE);


            for (int i = 0; i < computed_N; i++) {
                out << dataR[i] << " ";
            }
            for (int i = 0; i < computed_N; i++) {
                delete data[i];
            }
            delete[] data;
            delete[] dataR;
            delete[] receive_request;
            delete[] send_request;
        }
    } else {

        int total = processToReceive(threads, N, rank);
        int **data = new int *[total];

        auto *worker_receive_request = new MPI_Request[total];
        auto *worker_send_request = new MPI_Request[total];
        int *sums = new int[total];
        for (int i = 0; i < total; i++) {
            data[i] = new int[N];

            MPI_Irecv(data[i],N,MPI_INT,0,1,MPI_COMM_WORLD,&worker_receive_request[i]);
            MPI_Wait(&worker_receive_request[i], MPI_STATUS_IGNORE);

            sums[i] = 0;
            for (int j = 0; j < N; j++) {
                sums[i] += data[i][j];
            }

            MPI_Isend(&sums[i], 1,MPI_INT,0,2,MPI_COMM_WORLD,&worker_send_request[i]);
        }

        MPI_Waitall(total, worker_send_request, MPI_STATUS_IGNORE);

        delete[] worker_receive_request;
        delete[] worker_send_request;
        delete[] sums;
        for (int i = 0; i < total; i++) {
            delete[] data[i];
        }
        delete[] data;
    }
    MPI_Finalize();
}
