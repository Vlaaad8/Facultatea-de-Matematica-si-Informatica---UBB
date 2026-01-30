#include <iostream>
#include <omp.h>
#include <thread>

using namespace std;

const int MAX = 100;
const int MAX1 = 100;
const int MAX2 = 100;
int a[MAX], b[MAX], c[MAX], d[MAX], p[MAX];
int mat1[MAX1][MAX2], mat2[MAX1][MAX2], mat3[MAX1][MAX2], mat4[MAX1][MAX2];
long sum = 0;

void calculate_sum(int a[], int b[], int c[]) {
    for (int i = 0; i < MAX; i++) {
        c[i] = a[i] + b[i];
    }
}

void initialize(int v[]) {
    for (int i = 0; i < MAX; i++) {
        v[i] = rand() % 100;
    }
}

void initialize_matrix(int *v[100]) {
    for (int i = 0; i < MAX1; i++) {
        for (int j = 0; j < MAX2; j++) {
            v[i][j] = rand() % 100;
        }
    }
}

int main() {
#pragma omp parallel num_threads(8)
    {
#pragma omp for schedule(dynamic,3) nowait
        {
            for (int i = 0; i < MAX; i++) {
                a[i] = rand() % 100;
                b[i] = rand() % 100;
            }
        }
#pragma omp for reduction(+:sum)
        for (int i = 0; i < MAX; i++) {
            sum += a[i] * b[i];
        }
#pragma omp sections
        {
#pragma omp section
            {
                calculate_sum(a, b, c);
                std::cout << "Hello din thread-ul " << omp_get_thread_num() << endl;
            }
#pragma omp section
            {
                for (int i = 0; i < MAX; i++) {
                    sum += a[i] * b[i];
                }
                std::cout << "Hello din thread-ul " << omp_get_thread_num() << endl;
            }
#pragma omp section
            {
                for (int i = 0; i < MAX; i++) {
                    sum += a[i] * b[i];
                }
                std::cout << "Hello din thread-ul " << omp_get_thread_num() << endl;
            }
        }
    }
}

int main4() {
    for (int i = 0; i < MAX1; i++) {
        for (int j = 0; j < MAX2; j++) {
            mat1[i][j] = rand() % 100;
        }
    }
    for (int i = 0; i < MAX1; i++) {
        for (int j = 0; j < MAX2; j++) {
            mat2[i][j] = rand() % 100;
        }
    }
    auto start = omp_get_wtime();
    for (int i = 0; i < MAX1; i++) {
        for (int j = 0; j < MAX2; j++) {
            mat4[i][j] = mat1[i][j] + mat2[i][j];
        }
    }
    auto end = omp_get_wtime();
    auto startP = omp_get_wtime();
    cout << end - start << endl;
#pragma omp parallel num_threads(8)
    {
#pragma omp for collapse(2)
        for (int i = 0; i < MAX1; i++) {
            for (int j = 0; j < MAX2; j++) {
                mat3[i][j] = mat1[i][j] + mat2[i][j];
            }
        }
    }
}

int main2() {
    initialize(a);
    initialize(b);
    long sum_loc = 0;
    auto start = omp_get_wtime();
    calculate_sum(a, b, c);
    auto end = omp_get_wtime();

    auto startP = omp_get_wtime();
#pragma omp parallel num_threads(4) firstprivate(sum_loc)
    {
#pragma omp for schedule(dynamic, 3) nowait
        for (int i = 0; i < MAX; i++) {
            // #pragma omp critical
            // cout<<"Thread "<<omp_get_thread_num()<<" cu i= "<<i<<endl;
            d[i] = a[i] + b[i];
        }

#pragma omp for schedule(dynamic,3) nowait
        for (int i = 0; i < MAX; i++) {
            p[i] = a[i] * d[i];
        }

#pragma omp for
        for (int i = 0; i < MAX; i++) {
            sum_loc = sum_loc + a[i] * b[i];
        }
#pragma omp critical
        sum += sum_loc;
    }
    auto endP = omp_get_wtime();

    auto timeS = end - start;
    auto timeP = endP - startP;

    int diff = 0;

#pragma omp parallel for
    for (int i = 0; i < MAX; i++) {
        if (d[i] != c[i]) {
            diff++;
        }
    }

    cout << "Timp Secvential " << timeS << endl;
    cout << "Timp Parallel " << timeP << endl;
    cout << "Diferente " << diff << endl;
    cout << "Suma " << sum << endl;
}

int main1() {
    omp_set_num_threads(8);
#pragma omp parallel // num_threads(4)
    {
        int id = omp_get_thread_num();
        int total = omp_get_num_threads();
#pragma omp critical

        std::cout << "Salut din thread-ul " << id << " from " << total << std::endl;
    }
    std::cout << "Hello din thread-ul " << omp_get_thread_num() << " from " << omp_get_num_threads() << std::endl;
    cout << this_thread::get_id() << endl;
    return 0;
}
