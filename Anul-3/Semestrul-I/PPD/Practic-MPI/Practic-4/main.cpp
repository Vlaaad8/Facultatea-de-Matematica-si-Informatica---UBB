#include <iostream>
#include <fstream>
#include "mpi.h"


using namespace std;

ifstream in("input.txt");
ofstream out("output.txt");

int calculateHow(int totalThreads, int n, int thread) {
    int total = 0;
    for (int i = 0; i < n; i++) {
        int id = i % (totalThreads - 1) + 1;
        if (thread == id) total++;
    }
    return total;
}

int main(int argc, char **argv) {
    if (argc < 2) {
        exit(1);
    }

    int threads = atoi(argv[1]);

    MPI_Init(&argc, &argv);

    int rank, P;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &P);

    int **data = nullptr;
    MPI_Request *send_requests = nullptr;
    MPI_Request *recv_requests = nullptr;
    MPI_Request *n_send = nullptr;

    if (rank == 0) {
        int n;
        if (in.is_open()) {
            in >> n;
            data = new int *[n];
            send_requests = new MPI_Request[n];
            recv_requests = new MPI_Request[n];
            n_send = new MPI_Request[threads-1];

            for (int i = 1; i < threads; i++) {
                MPI_Isend(&n, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &n_send[i-1]);
            }

            for (int i = 0; i < n; i++) {
                data[i] = new int[n];
                for (int j = 0; j < n; j++) {
                    in >> data[i][j];
                }
                int id = i % (threads - 1) + 1;
                MPI_Isend(data[i], n,MPI_INT, id, 1,MPI_COMM_WORLD, &send_requests[i]);
            }

            int *result = new int[n];

            for (int i = 0; i < n; i++) {
                int id = i % (threads - 1) + 1;
                MPI_Irecv(&result[i], 1,MPI_INT, id, 2,MPI_COMM_WORLD, &recv_requests[i]);
            }

            MPI_Waitall(n, recv_requests,MPI_STATUS_IGNORE);
            MPI_Waitall(n, send_requests,MPI_STATUS_IGNORE);
            MPI_Waitall(threads-1, n_send,MPI_STATUS_IGNORE);

            for (int i = 0; i < n; i++) {
                out << result[i] << " ";
            }
        }
    } else {
        MPI_Request n_request;
        int local_n;

        MPI_Irecv(&local_n, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &n_request);

        MPI_Wait(&n_request, MPI_STATUS_IGNORE);

        int total = calculateHow(threads, local_n, rank);

        auto *receive_request = new MPI_Request[total];
        auto *send_request = new MPI_Request[total];

        auto **local_data = new int *[total];


        int *computedResult = new int[total];
        for (int i = 0; i < total; i++) {
            local_data[i] = new int[local_n];
            MPI_Irecv(local_data[i], local_n, MPI_INT, 0, 1, MPI_COMM_WORLD, &receive_request[i]);
            MPI_Wait(&receive_request[i], MPI_STATUS_IGNORE);
            computedResult[i] = 0;
            for (int j = 0; j < local_n; j++) {
                computedResult[i] += local_data[i][j];
            }
            MPI_Isend(&computedResult[i], 1, MPI_INT, 0, 2,MPI_COMM_WORLD, &send_request[i]);
        }

        MPI_Waitall(total, send_request, MPI_STATUS_IGNORE);

        delete[] send_request;
        delete[] receive_request;
        for (int i = 0; i < total; i++) {
            delete[] local_data[i];
        }
        delete[] local_data;
    }


    MPI_Finalize();
}
