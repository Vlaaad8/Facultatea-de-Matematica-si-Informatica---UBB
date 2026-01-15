#include "kernels.h"

int main() {
    srand(time(NULL));

    const int N = 10;
    const int M = 10;

    generateMatrix("matrix.txt", N, M);
    generateKernel("kernel.txt", k);

    int *matrixCPU;
    int *kernelCPU;

    readMatrix("matrix.txt", matrixCPU);
    readMatrix("kernel.txt", kernelCPU);

    int *matrixGPU;
    int *kernelGPU;
    cudaMalloc(&matrixGPU, N * M * sizeof(int));
    cudaMalloc(&kernelGPU, k * k * sizeof(int));

    cudaMemcpy(kernelGPU, kernelCPU, k * k * sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(matrixGPU, matrixCPU, N * M * sizeof(int), cudaMemcpyHostToDevice);

    dim3 threadsPerBlock(blockDimension, blockDimension);
    int gridX = (M + threadsPerBlock.x - 1) / threadsPerBlock.x;
    int gridY = (N + threadsPerBlock.y - 1) / threadsPerBlock.y;
    dim3 gridBlock(gridX, gridY);

    int* outputSequential = new int [N*M];

    auto startCPU = std::chrono::high_resolution_clock::now();
    calculateSequential(matrixCPU, outputSequential, N, M, k, kernelCPU);
    auto endCPU = std::chrono::high_resolution_clock::now();
    chrono::duration<double, std::milli> diffCPU = endCPU - startCPU;

    writeMatrix("resultSequential.txt", N, M, outputSequential);

    int sizeBufferRows = 2 * gridY * M;
    int sizeBufferColumns = 2 * gridX * N;
    int* bufferColumns;
    int* bufferRows;
    int* bufferCorners;

    cudaMalloc(&bufferCorners, 4 * gridX * gridY * sizeof(int));
    cudaMalloc(&bufferColumns, sizeBufferColumns * sizeof(int));
    cudaMalloc(&bufferRows, sizeBufferRows * sizeof(int));

    cudaEvent_t startGPU, stopGPU;
    cudaEventCreate(&startGPU);
    cudaEventCreate(&stopGPU);

    cudaEventRecord(startGPU);

    saveBordersKernel<<<gridBlock, threadsPerBlock>>>(matrixGPU, bufferColumns, bufferRows, bufferCorners, N, M, gridX);
    calculateKernel<<<gridBlock, threadsPerBlock>>>(matrixGPU, N, M, kernelGPU, bufferColumns, bufferRows, bufferCorners, gridX);

    cudaEventRecord(stopGPU);
    cudaEventSynchronize(stopGPU);

    float diffGPU = 0;
    cudaEventElapsedTime(&diffGPU, startGPU, stopGPU);

    cudaMemcpy(matrixCPU, matrixGPU, N * M * sizeof(int), cudaMemcpyDeviceToHost);
    writeMatrix("result.txt", N, M, matrixCPU);

    cout << "\n================ REZULTATE TIMP ================" << endl;
    cout << "Timp Secvential (CPU): " << diffCPU.count() << " ms" << endl;
    cout << "Timp CUDA (GPU):       " << diffGPU << " ms" << endl;
    cout << "Speedup:               " << diffCPU.count() / diffGPU << "x" << endl;
    cout << "================================================\n" << endl;

    filesAreEqual("resultSequential.txt", "result.txt");

    cudaEventDestroy(startGPU);
    cudaEventDestroy(stopGPU);
    cudaFree(matrixGPU);
    cudaFree(kernelGPU);
    cudaFree(bufferColumns);
    cudaFree(bufferRows);
    cudaFree(bufferCorners);
    delete[] matrixCPU;
    delete[] kernelCPU;
    delete[] outputSequential;

    return 0;
}