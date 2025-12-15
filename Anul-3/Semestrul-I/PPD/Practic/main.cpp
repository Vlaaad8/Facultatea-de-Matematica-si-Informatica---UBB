#include <cmath>
#include <iostream>
#include <fstream>

#include "Generate.h"
#include "mpi.h"

using namespace std;

ifstream inI("input.txt");
ifstream inP("polinom.txt");

ofstream out("output.txt");

const int X = 2;

int main(int argc, char** argv) {

    MPI_Init(&argc, &argv);

    int rank,P;

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &P);

    if (rank == 0) {
        Generate g;
        g.generateData("input.txt",1000);
        g.generateData("polinom.txt",50);
    }
    MPI_Barrier(MPI_COMM_WORLD);

    int* coef = nullptr;
    int N;
    if (rank == 0 ) {
        inP >> N;
        coef = new int[N];

        for (int i = 0; i < N; i++) {
            inP >> coef[i];
        }

    }
    MPI_Bcast(&N,1,MPI_INT,0,MPI_COMM_WORLD);

    if (rank != 0) {
        coef = new int[N];
    }
    MPI_Bcast(coef,N,MPI_INT,0,MPI_COMM_WORLD);


    if (rank == 0) {
        int n;
        inI >> n;
        int* values = new int[n];
        for (int i = 0; i < n; i++) {
            inI >> values[i];
        }

        int start = 0 ;

        MPI_Request* send_requests = new MPI_Request[P-1];
        MPI_Request* receive_requests = new MPI_Request[n];

        for (int i = 1 ; i < P ;i ++ ) {
            MPI_Isend(values+start,X,MPI_INT,i,1,MPI_COMM_WORLD,&send_requests[i-1]);
            start += X;
        }

        long long* local = new long long[n];
            int aux = 0;
            for (int i = 1; i < P; i++) {
                for (int j = 0; j < X; j++) {
                    MPI_Irecv(&local[aux],1,MPI_LONG_LONG,i,2,MPI_COMM_WORLD,&receive_requests[aux]);
                    aux++;
                }
            }
        int aux2 = 0;
        for (int i = 1; i < P; i++) {
            for (int j = 0; j < X; j++) {
               MPI_Wait(&receive_requests[aux2],MPI_STATUS_IGNORE);
                out<<local[aux2]<<" ";
                aux2++;
            }
        }



        MPI_Waitall(P,send_requests,MPI_STATUS_IGNORE);
        delete[] send_requests;
        delete[] receive_requests;
        // for (int i = 0 ; i < P-1 ; i ++) {
        //     delete[] local[i];
        // }
        delete[] local;
        delete[] values;
        delete[] coef;
    }



    if (rank > 0) {

        int* receiveValues = new int[X];

        MPI_Request receive_requests;
        MPI_Request send_requests ;

        MPI_Irecv(receiveValues,X,MPI_INT,0,1,MPI_COMM_WORLD,&receive_requests);

        MPI_Wait(&receive_requests,MPI_STATUS_IGNORE);

        auto computedSum = new long long[X];

        for (int i = 0; i < X; i++) {
            computedSum[i] = 0;
            for (int j = 0 ; j < N; j++) {
                computedSum[i] += coef[j]* pow (receiveValues[i],N-1-j);
            }
            MPI_Isend(&computedSum[i],1,MPI_LONG_LONG,0,2,MPI_COMM_WORLD,&send_requests);
        }

        //MPI_Isend(computedSum,X,MPI_LONG_LONG,0,2,MPI_COMM_WORLD,&send_requests);

        MPI_Wait(&send_requests,MPI_STATUS_IGNORE);
        delete[] receiveValues;
        delete[] computedSum;
        delete[] coef;


    }

    MPI_Finalize();
}