cmake_minimum_required(VERSION 3.20)
project(Tema_CUDA LANGUAGES CXX CUDA)

set(CMAKE_CUDA_STANDARD 17)

add_executable(Tema_CUDA Project.cu)
