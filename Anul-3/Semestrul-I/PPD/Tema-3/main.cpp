#include <mpi.h>
#include <iostream>
#include <chrono>
#include "header/AsyncCalculation.h"
#include "header/GenerateNumber.h"
#include "header/ScatterCalculation.h"
#include "header/SequentialCalculation.h"
#include "header/StandardCalculation.h"
#include <fstream>

#include "header/StandardClassicCalculation.h"

using namespace std::chrono;
using namespace std;

bool filesAreEqual(const string &file_name1, const string &file_name2) {
    ifstream f1(file_name1);
    ifstream f2(file_name2);
    if (!f1.is_open() || !f2.is_open()) {
        cout << "Error in opening files" << endl;
        return false;
    }
    f1.seekg(0, ios::end);
    f2.seekg(0, ios::end);
    if (f1.tellg() != f2.tellg()) {
        cout << "Not equal"<<endl;
        return false;
    }
    f1.seekg(0, ios::beg);
    f2.seekg(0, ios::beg);

    char c1,c2;
    while (f1.get(c1) && f2.get(c2)) {
        if (c1 != c2) {
            return false;
        }
    }
    return true;
}


int main(int argc, char **argv) {
    if (argc < 4) {
        cerr << "Insert minimum three arguments" << endl;
        return 1;
    }
    const int N1 = atoi(argv[1]);
    const int N2 = atoi(argv[2]);
    const int option = atoi(argv[3]);

    const int N_MAX = N1 > N2 ? N1 : N2;

    MPI_Init(&argc, &argv);
    int P, rank;

    MPI_Comm_size(MPI_COMM_WORLD, &P);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    if (rank == 0 && option == 0) {
        // GenerateNumber::generateNumber("numbers/firstNumber.txt", N1);
        // GenerateNumber::generateNumber("numbers/secondNumber.txt", N2);
    }

    MPI_Barrier(MPI_COMM_WORLD);

    SequentialCalculation calculatorS(N1, N2);
    StandardCalculation calculatorSt(P, N_MAX);
    StandardClassicCalculation calculatorStC(P, N_MAX);
    ScatterCalculation calculatorSc(P, N_MAX);
    AsyncCalculation calculatorA(P, N_MAX);

    MPI_Barrier(MPI_COMM_WORLD);
    double start_time = MPI_Wtime();
    switch (option) {
        case 0:
            if (rank == 0) {
                calculatorS.run();
            }
            break;
        case 1:
            calculatorSt.run();
            break;
        case 2:
            calculatorSc.run();
            break;
        case 3:
            calculatorA.run();
            break;
        case 4:
            calculatorStC.run();
            break;


        default:
            if (rank == 0) {
                printf("Wrong option: %d\n",option);
            }
    }

    MPI_Barrier(MPI_COMM_WORLD);
    double end_time = MPI_Wtime();

    if (rank == 0) {
        cout<< (end_time - start_time)<< endl;
    }

    MPI_Finalize();
    return 0;
}
