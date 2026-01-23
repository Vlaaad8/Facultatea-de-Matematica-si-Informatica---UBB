#pragma once
class SequentialCalculation
{
private:
	int N1;
	int N2;
public:
	SequentialCalculation(int n1, int n2)
		: N1(n1),
		  N2(n2) {
	}

	void run();

};

