cmake_minimum_required(VERSION 4.0)
project(Practic_1)
find_package(MPI REQUIRED)
include_directories(SYSTEM ${MPI_INCLUDE_PATH})
set(CMAKE_CXX_STANDARD 20)

add_executable(Practic_1 main.cpp
        GenerateData.cpp
        GenerateData.h)
target_link_libraries(Practic_1 ${MPI_LIBRARIES})