#include "GenerateNumber.h"
#include <fstream>
using namespace std;



 GenerateNumber::GenerateNumber(int number) {
	this->numberOfDigits = number;
}
void GenerateNumber::generateNumber(string fileName) {
	int* table = new int[numberOfDigits];
	for (int i = 0; i < numberOfDigits - 1; i++) {
		table[i] = rand() % 10;
	}
	table[numberOfDigits-1] = (rand() % 9) + 1;

	ofstream out(fileName);
	for (int i = 0; i < numberOfDigits; i++) {
		out << table[i] << " ";
	}
	delete[] table;
}

int* GenerateNumber::readNumber(string fileName) {
	ifstream in(fileName);
	if (!in) {
		cerr << "Number file could not be opened" << endl;
	}
	int* table = new int[numberOfDigits];
	int digit;
	for (int i = 0; i < numberOfDigits; i++) {
		in >> digit;
		table[i] = digit;
		
	}
	return table;
}