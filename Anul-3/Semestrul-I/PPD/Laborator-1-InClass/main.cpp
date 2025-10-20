#include <iostream>
#include <thread>
#include <chrono>
#include <cmath>

using namespace std;
using namespace std::chrono;

const int MAX_VAL= 1000000;
const int N = 1000000;
const int P=4;

int a[N];
int b[N];
int c[N];
int c_secv[N];



void hello(){
    cout << "Hello from thread" << endl;
}

void init(int v[], int len){
    for(int i = 0; i< len; i++){
        v[i]=rand() % MAX_VAL;
    }
}

void sum(const int a[],const int b[], int c[], int len){
    for(int i=0; i < len;i++){
        // c[i] = a[i] + b[i];
        c[i]=sqrt(a[i] * a[i] *a[i]) + b[i]*b[i];
    }
}

void sum_par(const int a[],const int b[], int c[], int start, int end){
    for(int i=start; i < end ;i++){
//        c[i]=a[i] + b[i];
        c[i]=sqrt(a[i] * a[i] *a[i]) + b[i]*b[i];
    }
}
void create_thr(int *a, int *b, int *c,int *c_secv) {
    int num_elem = N / P;
    int r = N % P;
    int start_idx = 0;
    int end_idx = num_elem;

    thread threads[P];
    auto start_time_par = high_resolution_clock::now();

    for(int thr = 0; thr < P; thr ++){
        if(r > 0){
            end_idx += 1;
            r -= 1 ;
        }
        //        cout<< "start: " << start_idx << " end: " << end_idx << endl;

        threads[thr] = thread(sum_par, a, b, c, start_idx, end_idx);


        start_idx = end_idx;
        end_idx = start_idx + num_elem;
    }

    for(int i = 0; i < P; i++){
        threads[i].join();
    }

    auto end_time_par = high_resolution_clock::now();
    duration<double, milli> par_time = end_time_par-start_time_par;
    cout  << "Parallel: " << par_time.count() << " ms" <<endl;

    //c[0] = 0;
    for(int i = 0; i < N; i++){
        if(c[i] != c_secv[i]){
            cout << "idx: " << i << " c: " << c[i] << " secv: " << c_secv[i] << endl;
        }
    }
}
int main() {
    auto start_time = high_resolution_clock::now();
//
//    thread hello_t(hello);
//    hello_t.join();
//    cout << "Hello thread" << endl;

    init(a, N);
    init(b, N);

    auto start_time_secv = high_resolution_clock::now();
    sum(a, b, c_secv, N);
    auto end_time_secv = high_resolution_clock::now();
    duration<double, milli> secv_time = end_time_secv - start_time_secv;
    cout<< "Secvential: " << secv_time.count() << "ms" << endl;

//    for(int i=0; i<5; i++){
//        cout<< "a: "<< a[i]<< " b: " << b[i] <<" c: " << c_secv[i] << endl;
//
//    }

    create_thr(a,b,c,c_secv);



    auto end_time = high_resolution_clock::now();
    duration<double, milli> delta_time = end_time-start_time;
    cout<< "Duration: " <<  delta_time.count() << " ms" << endl;

    int *a_dyn = new int[N];
    int *b_dyn = new int[N];
    int *c_dyn = new int[N];

    int *common = new int[N*3];

    for (int i = 0; i < N; i++) {
        a_dyn[i] = a[i];
        b_dyn[i] = b[i];
        common[i]=a[i];
        common[i+N] = b[i];
    }

    create_thr(a,b_dyn,c_dyn,c_secv);
    create_thr(common,common+N,c_dyn,c_secv);

    delete[] a_dyn;
    delete[] b_dyn;
    delete[] c_dyn;
    delete[] common;
    return 0;
}
