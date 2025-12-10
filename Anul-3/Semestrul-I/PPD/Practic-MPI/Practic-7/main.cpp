#include <iostream>
#include <fstream>
#include "mpi.h"

using namespace std;

int total_lines(int threads,int idx, int n) {
    int total = 0;
    for (int i = 0; i < n; i++) {
        int id = i % (threads-1) + 1;
        if (id == idx) total++;
    }
    return total;
}
int main(int argc, char *argv[]) {

    MPI_Init(&argc, &argv);
    int rank, P;

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &P);

    if (rank == 0) {
        ifstream in("input.txt");
        int n , k ;

        in >> n ;

        int** data = new int*[n];
        auto send_requests = new MPI_Request[n];
        auto* recv_requests = new MPI_Request[n];
        auto* n_request = new MPI_Request[P-1];



        for (int i = 0; i < n; i++) {
            data[i] = new int[n];
            for (int j = 0; j < n; j++) {
                in>>data[i][j];
            }
            int id = i % (P-1) + 1;
            MPI_Isend(data[i],n,MPI_INT,id,1,MPI_COMM_WORLD,&send_requests[i]);
        }
        for (int i = 1; i < P; i++) {
            MPI_Isend(&n,1,MPI_INT,i,0,MPI_COMM_WORLD,&n_request[i-1]);
        }

        int global_sum = -1000;
        for (int i = 0; i < n; i++) {
            int tmp;
            int id = i % (P-1) + 1;
            MPI_Irecv(&tmp,1,MPI_INT,id,2,MPI_COMM_WORLD,&recv_requests[i]);

            MPI_Wait(&recv_requests[i],MPI_STATUS_IGNORE);

            if (tmp > global_sum) {
                global_sum = tmp;
            }
        }
        cout<<global_sum<<endl;

        delete[] send_requests;
        delete[] recv_requests;
        delete[] n_request;
        for (int i = 0; i < n; i++) {
            delete[] data[i];
        }
        delete[] data;


    }
    else {
        int n ;
        MPI_Request n_request;

        MPI_Irecv(&n,1,MPI_INT,0,0,MPI_COMM_WORLD,&n_request);
        MPI_Wait(&n_request,MPI_STATUS_IGNORE);

        int total = total_lines(P,rank,n);
        int** data = new int*[total];

        auto send_requests = new MPI_Request[total];
        auto* recv_requests = new MPI_Request[total];

        for (int i = 0; i < total; i++) {
            data[i] = new int[n];
            MPI_Irecv(data[i],n,MPI_INT,0,1,MPI_COMM_WORLD,&recv_requests[i]);
            MPI_Wait(&recv_requests[i],MPI_STATUS_IGNORE);

            int local_sum = 0 ;
            for (int j = 0; j < n; j++) {
                local_sum += data[i][j];
            }
            MPI_Isend(&local_sum,1,MPI_INT,0,2,MPI_COMM_WORLD,&send_requests[i]);

        }
        MPI_Waitall(total,send_requests,MPI_STATUS_IGNORE);

        delete[] send_requests;
        delete[] recv_requests;
        for (int i = 0; i < total; i++) {
            delete[] data[i];
        }
        delete[] data;
    }

    MPI_Finalize();
}