#include <mpi.h>
#include <iostream>

#include "header/GenerateNumber.h"
#include "header/SequentialCalculation.h"
#include "header/StandardCalculation.h"

int main(int argc, char **argv) {
    // GenerateNumber::generateNumber("firstNumber.txt", 10);
    // GenerateNumber::generateNumber("secondNumber.txt", 3);
    int *firstNumber = GenerateNumber::readNumber("firstNumber.txt");
    int *secondNumber = GenerateNumber::readNumber("secondNumber.txt");

    SequentialCalculation calculation(firstNumber, secondNumber, 11, 1);
    int *number = calculation.calculate();
    GenerateNumber::writeNumber("result.txt", number, 11);

    StandardCalculation calculator(6,11);
    calculator.run();
}
