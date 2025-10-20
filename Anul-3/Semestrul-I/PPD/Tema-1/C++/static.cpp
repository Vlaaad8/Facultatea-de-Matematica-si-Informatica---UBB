#include <iostream>
#include <fstream>
#include <thread>
#include <chrono>

using namespace std::chrono;
using namespace std;


const static int N = 10;
const static int M = 1000;
const static int K = 3;

using namespace std;

void writeMatrixToFile(int matrix[N][M], const string &matrix_file, const int rows, const int cols) {
    ofstream outM(matrix_file);
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            outM << matrix[i][j] << " ";
        }
        outM << endl;
    }
}

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

void readMatrix(const string &file_name, int matrix[N][M]) {
    ifstream in(file_name);
    int rows, cols;
    in >> rows >> cols;
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            in >> matrix[i][j];
        }
    }
}
void readfilter(const string &file_name, int matrix[K][K]) {
    ifstream in(file_name);
    int rows, cols;
    in >> rows >> cols;
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            in >> matrix[i][j];
        }
    }
}

int compute_element(int i, int j, int matrix[N][M], int filter[K][K]) {
    const int half = K / 2;
    int sum = 0;

    for (int a = -half; a <= half; a++) {
        for (int b = -half; b <= half; b++) {
            int x = i + a;
            int y = j + b;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x >= N) x = N - 1;
            if (y >= M) y = M - 1;
            sum += matrix[x][y] * filter[a + half][b + half];
        }
    }
    return sum;
}

void runS(int matrix[N][M], int filter[K][K],int new_matrix[N][M]) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            new_matrix[i][j] = compute_element(i, j, matrix, filter);
        }
    }
}
void calculate_convolutionV(int start_idx, int end_idx, int new_matrix[N][M],int matrix[N][M], int filter[K][K]) {
    for (int j = start_idx; j < end_idx; j++) {
        for (int i = 0; i < N; i++) {
            new_matrix[i][j] = compute_element(i, j,matrix,filter);
        }
    }
}
void calculate_convolutionH(int start_idx, int end_idx, int new_matrix[N][M],int matrix[N][M], int filter[K][K]) {
    for (int i = start_idx; i < end_idx; i++) {
        for (int j = 0; j < M; j++) {
            new_matrix[i][j] = compute_element(i, j,matrix,filter);
        }
    }
}

void runV(int P,int matrix[N][M],int filter[K][K],int new_matrix[N][M]) {
    thread threads[P];
    int rows_per_thread = M/ P;
    int extra = M % P;
    int start_idx = 0;

    for (int i = 0; i < P; i++) {
        int end_idx = start_idx + rows_per_thread;
        if (extra > 0) {
            extra--;
            end_idx++;
        }


        threads[i] = thread(calculate_convolutionV,start_idx, end_idx, new_matrix,matrix,filter);

        start_idx = end_idx;
    }


    for (int i = 0; i < P; i++) {
        threads[i].join();
    }


}
void runH(int P,int matrix[N][M],int filter[K][K],int new_matrix[N][M]) {
    thread threads[P];
    int rows_per_thread = N / P;
    int extra = N % P;
    int start_idx = 0;

    for (int i = 0; i < P; i++) {
        int end_idx = start_idx + rows_per_thread;
        if (extra > 0) {
            extra--;
            end_idx++;
        }


        threads[i] = thread(calculate_convolutionH,start_idx, end_idx, new_matrix,matrix,filter);

        start_idx = end_idx;
    }


    for (int i = 0; i < P; i++) {
        threads[i].join();
    }



}
double estimate_conv_S(int matrix[N][M],int filter[K][K]) {
    int new_matrix[N][M];
        auto start_time = high_resolution_clock::now();
        runS(matrix,filter,new_matrix);
        auto end_time = high_resolution_clock::now();
        duration<double, milli> round_time = end_time - start_time;
    writeMatrixToFile(new_matrix, "resultS-S.txt", N, M);
    return round_time.count();

}

double estimate_conv_H(const int threads,int matrix[N][M],int filter[K][K])  {
    int new_matrix[N][M];
        auto start_time = high_resolution_clock::now();
        runH(threads,matrix,filter,new_matrix);
        auto end_time = high_resolution_clock::now();
        duration<double, milli> round_time = end_time - start_time;

    writeMatrixToFile(new_matrix, "resultH-S.txt", N, M);
    return round_time.count();
}
double estimate_conv_V(const int threads,int matrix[N][M],int filter[K][K])  {
    int new_matrix[N][M];
        auto start_time = high_resolution_clock::now();
        runV(threads,matrix,filter,new_matrix);
        auto end_time = high_resolution_clock::now();
        duration<double, milli> round_time = end_time - start_time;


    writeMatrixToFile(new_matrix, "resultV-S.txt", N, M);
    return round_time.count();
}

int main(int argc, char* argv[]) {
    if(argc < 2) {
        cout << "Usage: program P N M K\n";
        return 1;
    }
    int P = atoi(argv[1]);

    int matrix[N][M];
    int filter[K][K];

    readMatrix("matrix.txt", matrix);
    readfilter("filter.txt", filter);

    if (filesAreEqual("resultH-S.txt","resultV-S.txt") && filesAreEqual("resultS-S.txt","resultH-S.txt")) {
        cout << "Tip Matrice N=" << N << "; M=" << M << endl;
        cout << "Tip filter: n=m=" << K << endl;
        cout << "Secvential: " << estimate_conv_S(matrix,filter) << "ms" << endl;
        cout << "Thread Vertical cu P=" << P << ": " << estimate_conv_V(P,matrix,filter) << "ms" << endl;
        cout << "Thread Orizontal cu P=" << P << ": " << estimate_conv_H(P,matrix,filter) << "ms" << endl;
    }
}