#include "../header/SequentialCalculation.h"

#include "../header/GenerateNumber.h"
#include <fstream>
using namespace std;


void SequentialCalculation::run() {
    ofstream outSq("results/resultSequential.txt");
    int dim, minDim;
    if (N1 > N2) {
        dim = N1 + 1;
        minDim = N2;
    } else {
        dim = N2 + 1;
        minDim = N1;
    }
    int *numberOne = GenerateNumber::readNumber("numbers/firstNumber.txt");
    int *numberTwo = GenerateNumber::readNumber("numbers/secondNumber.txt");
    int *result = new int[dim];
    int carry = 0;
    for (int i = 0; i < minDim; i++) {
        result[i] = (numberOne[i] + numberTwo[i] + carry) % 10;
        carry = (numberOne[i] + numberTwo[i] + carry) / 10;
    }

    if (N1 > minDim) {
        for (int i = minDim; i < N1; i++) {
            result[i] = (numberOne[i] + carry) % 10;
            carry = (numberOne[i] + carry) / 10;
        }
        if (carry) {
            result[dim - 1] = carry;
        }
    } else if (N2 > minDim) {
        for (int i = minDim; i < N2; i++) {
            result[i] = (numberTwo[i] + carry) % 10;
            carry = (numberTwo[i] + carry) / 10;
        }
        if (carry) {
            result[dim - 1] = carry;
        }
    } else if (carry) {
        result[dim - 1] = carry;
    }
    for (int i = 0; i < dim - 1; i++) {
        outSq << result[i] << " ";
    }
    if (result[dim - 1] > 0) {
        outSq << result[dim - 1] ;
    }
    delete[] result;
}
