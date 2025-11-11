#include "../header/AsyncCalculation.h"
#include <fstream>
#include <mpi.h>
#include "../header/GenerateNumber.h"
using namespace std;
ofstream outA("resultAsync.txt");

void AsyncCalculation::run() {
    int rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    calculator(rank);
}

void AsyncCalculation::calculator(int rank) {
    if (rank == 0) {
        const int dimension = N_Max / (P - 1);
        int extra = N_Max % (P - 1);
        int startPoint = 0;
        MPI_Request* requests=new MPI_Request[P-1];

        for (int pid = 1; pid < P; pid++) {
            int endPoint = startPoint + dimension;
            if (extra > 0) {
                endPoint++;
                extra--;
            }
            int batchSize = endPoint - startPoint;
            int *firstNumber = GenerateNumber::readNumberBlock("firstNumber.txt", startPoint, batchSize);
            int *secondNumber = GenerateNumber::readNumberBlock("secondNumber.txt", startPoint, batchSize);
            MPI_Isend(firstNumber,batchSize,MPI_INT,pid,1,MPI_COMM_WORLD,&requests[pid-1]);
            MPI_Isend(secondNumber,batchSize,MPI_INT,pid,2,MPI_COMM_WORLD,&requests[pid-1]);
        }

    }
    else{
        MPI_Request requests[2];
        const int dimension = N_Max / (P - 1);
        const int extra = N_Max % (P - 1);
        const int batchSize = dimension + ((rank - 1) < extra);

        int *firstNumber = new int[batchSize];
        int *secondNumber = new int[batchSize];
        int* result = new int[batchSize];
        MPI_Irecv(firstNumber,batchSize,MPI_INT,0,1,MPI_COMM_WORLD,&requests[0]);
        MPI_Irecv(secondNumber,batchSize,MPI_INT,0,2,MPI_COMM_WORLD,&requests[1]);

        MPI_Request carryRequest;
        MPI_Waitall(2,requests,MPI_STATUS_IGNORE);

        int carry = sum(firstNumber,secondNumber,result,batchSize);

        if (rank > 1) {
            int receivedCarry;
            MPI_Irecv(&receivedCarry,1,MPI_INT,rank-1,4,MPI_COMM_WORLD,&carryRequest);
            if (receivedCarry > 0) {
                passCarry(result,batchSize,receivedCarry);
                carry+=receivedCarry;
            }
        }
        if (rank < (P - 1)) {
            MPI_Isend(&carry, 1,MPI_INT, rank + 1, 4,MPI_COMM_WORLD,&carryRequest;
        }

        // if (rank == P - 1) {
        //     MPI_Send(&carry, 1,MPI_INT, 0, 3,MPI_COMM_WORLD);
        // }
        //
        // MPI_ISend(result, batchSize,MPI_INT, 0, 2,MPI_COMM_WORLD);

        delete[] firstNumber;
        delete[] secondNumber;
        delete[] result;

    }
}
    int AsyncCalculation::sum(const int *firstNumber, const int *secondNumber, int *result, const int size) {
        int carry = 0;
        for (int i = 0; i < size; i++) {
            result[i] = (firstNumber[i] + secondNumber[i] + carry) % 10;
            carry = (firstNumber[i] + secondNumber[i] + carry) / 10;
        }
        return carry;
    }

    void AsyncCalculation::passCarry(int *number, const int size, int &carry) {
        for (int i = 0; i < size; i++) {
            const int value = number[i] + carry;
            number[i] = value % 10;
            carry = value / 10;
        }
}

