#include "omp.h"
void main() {
    int i, t, N = 12;
    int a[N], b[N}, c[N];
        for (i=0; i<N; i++) a[i] = b[i] = 3;
omp_set_num_threads(3);
#pragma omp parallel shared(a,b,c) private(i,t) firstprivate(N)
#pragma omp single
t = omp_get_thread_num();
#pragma omp sections
{
#pragma omp section
    {
        for (i=0; i<N/3; i++) {
            c[i] = a[i] + b[i] + t;
        }
    }
#pragma omp section
{
    for (i=N/3; i<(N/3)*2; i++) {
        c[i] = a[i] + b[i] + t;
    }
}
#pragma omp section
{
    for (i=(N/3)*2; i<N; i++) {
        c[i] = a[i] + b[i] + t;
    }
}
}
}
