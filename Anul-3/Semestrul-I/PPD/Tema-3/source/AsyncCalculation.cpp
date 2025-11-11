#include "../header/AsyncCalculation.h"
#include <fstream>
#include <mpi.h>
#include <vector>

#include "../header/GenerateNumber.h"
using namespace std;

void AsyncCalculation::run() {
    int rank;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    calculator(rank);
}

void AsyncCalculation::calculator(int rank) {
    ofstream outA("results/resultAsync.txt");
    if (rank == 0) {
        const int dimension = N_Max / (P - 1);
        int extra = N_Max % (P - 1);
        int startPoint = 0;
        auto requests = new MPI_Request[2 * (P - 1)];
        int *finalResult = new int[N_Max];

        for (int pid = 1; pid < P; pid++) {
            int endPoint = startPoint + dimension;
            if (extra > 0) {
                endPoint++;
                extra--;
            }
            int batchSize = endPoint - startPoint;

            int *firstNumber = GenerateNumber::readNumberBlock("numbers/firstNumber.txt", startPoint, batchSize);
            int *secondNumber = GenerateNumber::readNumberBlock("numbers/secondNumber.txt", startPoint, batchSize);

            MPI_Isend(firstNumber, batchSize,MPI_INT, pid, 0,MPI_COMM_WORLD, &requests[2 * (pid - 1)]);
            MPI_Isend(secondNumber, batchSize,MPI_INT, pid, 1,MPI_COMM_WORLD, &requests[2 * (pid - 1) + 1]);

            startPoint = endPoint;
        }

        MPI_Waitall(2 * P - 2, requests, MPI_STATUS_IGNORE);

        MPI_Request *responseRequest = new MPI_Request[P];
        int finalFlag = 0;
        startPoint = 0;
        extra = N_Max % (P - 1);
        for (int pid = 1; pid < P; pid++) {
            int endPoint = startPoint + dimension;
            if (extra > 0) {
                endPoint++;
                extra--;
            }
            int batchSize = endPoint - startPoint;

            MPI_Irecv(finalResult + startPoint, batchSize,MPI_INT, pid, 3,MPI_COMM_WORLD, &responseRequest[pid - 1]);

            if (pid == P - 1) {
                MPI_Irecv(&finalFlag, 1,MPI_INT, pid, 4,MPI_COMM_WORLD, &responseRequest[pid]);
            }
            startPoint = endPoint;
        }

        MPI_Waitall(P, responseRequest,MPI_STATUS_IGNORE);

        for (int i = 0; i < N_Max; i++) {
            outA << finalResult[i] << " ";
        }
        if (finalFlag > 0) {
            outA << finalFlag;
        }

        delete[] requests;
        delete[] finalResult;
        delete[] responseRequest;
    } else {
        MPI_Request receiveRequest[3];
        const int dimension = N_Max / (P - 1);
        const int extra = N_Max % (P - 1);
        const int batchSize = dimension + ((rank - 1) < extra);
        int numberOfWaits = 2;

        int *firstNumber = new int[batchSize];
        int *secondNumber = new int[batchSize];
        int *result = new int[batchSize];
        int receivedCarry;

        MPI_Irecv(firstNumber, batchSize,MPI_INT, 0, 0,MPI_COMM_WORLD, &receiveRequest[0]);
        MPI_Irecv(secondNumber, batchSize,MPI_INT, 0, 1,MPI_COMM_WORLD, &receiveRequest[1]);

        if (rank > 1) {
            MPI_Irecv(&receivedCarry, 1,MPI_INT, rank - 1, 2,MPI_COMM_WORLD, &receiveRequest[2]);
            numberOfWaits++;
        } else {
            receivedCarry = 0;
        }

        MPI_Waitall(numberOfWaits, receiveRequest,MPI_STATUSES_IGNORE);

        int carry = sum(firstNumber, secondNumber, result, batchSize);

        passCarry(result, batchSize, receivedCarry);
        carry += receivedCarry;

        MPI_Request sendRequest[2];

        if (rank < P - 1) {
            MPI_Isend(&carry, 1,MPI_INT, rank + 1, 2,MPI_COMM_WORLD, &sendRequest[0]);
        } else {
            MPI_Isend(&carry, 1,MPI_INT, 0, 4,MPI_COMM_WORLD, &sendRequest[0]);
        }

        MPI_Isend(result, batchSize,MPI_INT, 0, 3,MPI_COMM_WORLD, &sendRequest[1]);

        MPI_Waitall(2, sendRequest,MPI_STATUSES_IGNORE);

        delete[] result;
        delete[] firstNumber;
        delete[] secondNumber;
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
