
#ifndef C___MATRIXGENERATOR_H
#define C___MATRIXGENERATOR_H
#include <string>
using namespace std;

class MatrixGenerator {
private:
    string matrix_file;
    string  filter_file;
    int rows;
    int cols;
    int k;

public:

    MatrixGenerator(const string &matrix_file, const string &filter_file,int n,int m,int k);


    void generateMatrix() const;
    void generateFilter() const;
    static void writeMatrixToFile(int** matrix, const string &matrix_file,const int rows,const int cols);
};


#endif
