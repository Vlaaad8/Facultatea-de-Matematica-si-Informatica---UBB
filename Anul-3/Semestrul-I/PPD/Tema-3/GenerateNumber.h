#pragma once
#include <iostream>
#include <string>
using namespace std;
class GenerateNumber
{
private :
	int numberOfDigits;
public :
	GenerateNumber(int number);
	void generateNumber(string fileName);
	int* readNumber(string filenName);
};

