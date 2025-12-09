cmake_minimum_required(VERSION 4.0)
project(Practic_2)
find_package(MPI REQUIRED)
include_directories(SYSTEM ${MPI_INCLUDE_PATH})
set(CMAKE_CXX_STANDARD 20)

add_executable(Practic_2 main.cpp
        GenerateNumber.cpp
        GenerateNumber.h)
target_link_libraries(Practic_2 ${MPI_LIBRARIES})