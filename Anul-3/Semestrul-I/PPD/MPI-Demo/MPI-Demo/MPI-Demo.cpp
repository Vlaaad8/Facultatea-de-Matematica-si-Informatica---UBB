#include <iostream>
#include <mpi.h>
#include <string>

using namespace std;

void hello_mpi(int rank, int p) {
    const int mesg_len = 19; // [%d] hello\n
    if (rank == 0) {
        // main
        char* buf = new char[mesg_len * (p - 1)+1];
        buf[mesg_len * (p - 1)] = '\0';

        for (int pid = 1; pid < p; pid++) {
            MPI_Recv(buf + mesg_len * (pid - 1),mesg_len,MPI_CHAR,pid,0,MPI_COMM_WORLD,MPI_STATUS_IGNORE);
        }
        printf("%s\n", buf);

        delete[] buf;
    }
    else {
        //worker
        char* buf = new char[mesg_len + 1];
        snprintf(buf, mesg_len + 1, "[%10d] Hello\n", rank);
        MPI_Send(buf, mesg_len, MPI_CHAR, 0, 0,MPI_COMM_WORLD);
    }
}

void hello_mpi_async(int rank, int p) {
    const int mesg_len = 19; // [%d] hello\n
    if (rank == 0) {
        // main
        char* buf = new char[mesg_len * (p - 1)+1];
        buf[mesg_len * (p - 1)] = '\0';
        MPI_Request* reqs = new MPI_Request[p - 1];

        for (int pid = 1; pid < p; pid++) {
            MPI_Irecv(buf + mesg_len * (pid - 1), mesg_len, MPI_CHAR, pid, 0, MPI_COMM_WORLD, &reqs[pid - 1]);
        }

        //int ok = false;
        //int* flags = new int[p - 1];
        //while (!ok) 
        //{
        //    ok = true;
        //    for (int pid = 1; pid < p; pid++) {
        //        MPI_Test(&reqs[pid - 1], &flags[pid - 1], MPI_STATUSES_IGNORE);
        //        if (flags[pid - 1] != 0) {
        //            printf("%d finished\n", pid);
        //        }
        //        else {
        //            ok = false;
        //        }
        //    }
        //}

        MPI_Waitall(p - 1, reqs, MPI_STATUSES_IGNORE);

        for (int pid = 1; pid < p; pid++) {
            int idx;
            MPI_Waitany(p - 1, reqs, &idx, MPI_STATUSES_IGNORE);
                printf("%d finished\n", idx);
        }
        printf("%s\n", buf);

        delete[] buf;
        delete[] reqs;
    }
    else {
        //worker
        char* buf = new char[mesg_len + 1];
        MPI_Request request;
        snprintf(buf, mesg_len + 1, "[%10d] Hello\n", rank);
        MPI_Isend(buf, mesg_len, MPI_CHAR, 0, 0, MPI_COMM_WORLD,&request);

        MPI_Wait(&request, MPI_STATUSES_IGNORE);
    }
}


void init(int* vec, int len) {
    for (int i = 0; i < len; i++) {
        vec[i] = rand() % 100;
    }
}

void validate(int* a,int* b,int* c, int len) {
    for (int i = 0; i < len; i++) {
        if (c[i] = a[i] + b[i]) {
            printf("[%d] %d != %d + %d\n", i, c[i], a[i], b[i]);
            return;
        }
    }
}

void sum_vec(int rank, int p) {
    const int N = 1000;
    if (rank == 0) {
        int* a = new int[N];
        int* b = new int[N];
        int* c = new int[N];
        init(a, N);
        init(b, N);
        

        int dim = N/(p - 1);
        int r = N % (p - 1);
        int start = 0;
        int end = dim;

        for (int pid = 1; pid < p; p++) {
            if (r > 0) {
                end += 1;
                r -= 1;
            }
            MPI_Send(a + start, end - start, MPI_INT, pid, 0, MPI_COMM_WORLD);
            MPI_Send(b + start, end - start, MPI_INT, pid, 0, MPI_COMM_WORLD);


            //async
            /*MPI_Irecv(c + start, end - start, MPI_INT, pid, 0, MPI_COMM_WORLD, &reqs[pid - 1]);*/
          

            start = end;
            end += dim;

        }

        //Sync
        r = N % (p - 1);
        start = 0;
        end = dim;
        for (int pid = 1; pid < p; p++) {
            if (r > 0) {
                end += 1;
                r -= 1;
            }
            MPI_Recv(c + start, end - start, MPI_INT, pid, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);
            start = end;
            end += dim;
        }

        //Async

        MPI_Request* reqs = new MPI_Request[p - 1];
        MPI_Waitall(p - 1, reqs, MPI_STATUSES_IGNORE);

        for (int pid = 1; pid < p; pid++) {

        }
        validate(a, b, c, N);
        delete[] a;
        delete[] b;
        delete[] c;

    }
    else {

        int dim = N / (p - 1);
        int r = N % (p - 1);
        int recv_dim = dim + ((rank - 1) < r);

        int* a_loc = new int[recv_dim];
        int* b_loc = new int[recv_dim];
        int* c_loc = new int[recv_dim];


        MPI_Recv(a_loc, recv_dim, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);
        MPI_Recv(b_loc, recv_dim, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUSES_IGNORE);

        for (int i =0; i < recv_dim; i++) {
            c_loc[i] = a_loc[i] + b_loc[i];
        }

        MPI_Send(c_loc, recv_dim, MPI_INT, 0, 0, MPI_COMM_WORLD);


        delete[] a_loc;
        delete[] b_loc;
        delete[] c_loc;
    }

}

void sum_vec_mpi(int rank, int p) {
    const int SUM_LEN = 1000;
    int dim = SUM_LEN / p;

    int actual_sum_len = dim * p;

    int* a = nullptr;
    int* b = nullptr;
    int* c = nullptr;

    if (rank == 0) {
        a = new int[actual_sum_len];
        b = new int[actual_sum_len];
        c = new int[actual_sum_len];

        init(a, actual_sum_len);
        init(b, actual_sum_len);
        
    }

    int* a_loc = new int[dim];
    int* b_loc = new int[dim];
    int* c_loc = new int[dim];

    MPI_Scatter(a, dim, MPI_INT,a_loc, dim, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatter(b, dim, MPI_INT, b_loc, dim, MPI_INT, 0, MPI_COMM_WORLD);

    for (int i = 0; i < dim; i++) {
        c_loc[i] = a_loc[i] + b_loc[i];
    }

    MPI_Gather(c_loc, dim, MPI_INT, c, dim, MPI_INT, 0, MPI_COMM_WORLD);
    if (rank == 0) {
        validate(a, b, c, actual_sum_len);
    }
    delete[] a_loc;
    delete[] b_loc;
    delete[] c_loc;
}

void sum_vec_mpi_redist(int rank, int p) {
    const int SUM_LEN = 1000;
    int dim = SUM_LEN / p;
    int r = SUM_LEN % p;
    int start = 0;
    int end = dim;

    int* lengths = new int[p];
    int* displacements = new int[p];

    for (int pid = 0; pid < p; pid++) {
        if (r > 0) {
            end += 1;
            r -= 1;
        }
        lengths[pid] = end - start;
        displacements[pid] = start;
    }

    int* a = nullptr;
    int* b = nullptr;
    int* c = nullptr;

    if (rank == 0) {
        a = new int[SUM_LEN];
        b = new int[SUM_LEN];
        c = new int[SUM_LEN];

        init(a,SUM_LEN);
        init(b, SUM_LEN);

    }

    int* a_loc = new int[lengths[rank]];
    int* b_loc = new int[lengths[rank]];
    int* c_loc = new int[lengths[rank]];

    MPI_Scatter(a, lengths, displacements,MPI_INT, a_loc, lengths[rank], MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatter(b, lengths, displacements, MPI_INT, b_loc, lengths[rank], MPI_INT, 0, MPI_COMM_WORLD);

    for (int i = 0; i < lengths[rank]; i++) {
        c_loc[i] = a_loc[i] + b_loc[i];
    }

    MPI_Gather(c_loc, dim, MPI_INT, c, dim, MPI_INT, 0, MPI_COMM_WORLD);
    if (rank == 0) {
        validate(a, b, c, SUM_LEN);
    }
    delete[] a_loc;
    delete[] b_loc;
    delete[] c_loc;
}
int main(int argc, char* argv[])
{
    MPI_Init(&argc,&argv);
    
    if (argc < 2) {
        printf("Usage: %s [select]\n", argv[0]);
        exit(1);
    }

    int rank, p;

    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    printf("Hello from: %d/%d\n", rank, p);

    int select = stoi(argv[1]);
        //printf("%d\n", select);
    MPI_Barrier(MPI_COMM_WORLD);

        switch (select) {
        case 1:
            hello_mpi(rank, p);
            break;
        case 2:
            hello_mpi_async(rank, p);
            break;
        case 3:
            sum_vec(rank, p);
            break;
        case 4:
            sum_vec_mpi(rank, p);
            break;
        case 5:
            sum_vec_mpi_redist(rank, p);
            break;
        default:
            if (rank == 0) {
                printf("Unknown option %d\n", select);
            }
        }
    MPI_Finalize();
}

