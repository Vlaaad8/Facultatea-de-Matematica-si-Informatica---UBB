#include <mpi.h>
#include <iostream>

#include "header/GenerateNumber.h"
#include "header/ScatterCalculation.h"
#include "header/SequentialCalculation.h"
#include "header/StandardCalculation.h"

int main(int argc, char **argv) {
    if (argc < 3) {
        cerr << "Insert minimum three arguments";
        return 1;
    }
    int N1 = atoi(argv[1]);
    int N2 = atoi(argv[2]);

    int N_MAX;
    if (N1 > N2) {
        N_MAX = N1;
    } else {
        N_MAX = N2;
    }

    GenerateNumber::generateNumber("firstNumber.txt", N1);
    GenerateNumber::generateNumber("secondNumber.txt", N2);

    int *firstNumber = GenerateNumber::readNumber("firstNumber.txt");
    int *secondNumber = GenerateNumber::readNumber("secondNumber.txt");

    SequentialCalculation calculation(firstNumber, secondNumber, N1, N2);
    int *number = calculation.calculate();
    GenerateNumber::writeNumber("result.txt", number, N_MAX);

    MPI_Init(&argc, &argv);
    int P, rank;
    MPI_Comm_size(MPI_COMM_WORLD, &P);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    StandardCalculation calculator(P, N_MAX);
    calculator.run();

    ScatterCalculation calculatorS(P, N_MAX);
    calculatorS.run();
    MPI_Finalize();
}