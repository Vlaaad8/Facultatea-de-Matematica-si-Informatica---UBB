cmake_minimum_required(VERSION 4.0)
project(Tema_3)
find_package(MPI REQUIRED)
include_directories(SYSTEM ${MPI_INCLUDE_PATH})
set(CMAKE_CXX_STANDARD 20)

add_executable(Tema_3 main.cpp
        source/GenerateNumber.cpp
        source/SequentialCalculation.cpp
        source/StandardCalculation.cpp
        header/StandardCalculation.h
        header/StandardCalculation.h
        source/ScatterCalculation.cpp
        header/ScatterCalculation.h
        source/ScatterCalculation.cpp
        source/AsyncCalculation.cpp
        header/AsyncCalculation.h
        source/AsyncCalculation.cpp)
target_link_libraries(Tema_3 ${MPI_LIBRARIES})