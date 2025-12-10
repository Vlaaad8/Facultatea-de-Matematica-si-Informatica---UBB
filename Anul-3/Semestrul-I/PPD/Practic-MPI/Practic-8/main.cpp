#include <iostream>
#include <fstream>
#include "mpi.h"

using namespace std;

int main(int argc, char *argv[]) {

    MPI_Init(&argc, &argv);
    int rank,P;

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &P);

    if (rank == 0 ) {
        ifstream("input.txt");

    }



    MPI_Finalize();
}
