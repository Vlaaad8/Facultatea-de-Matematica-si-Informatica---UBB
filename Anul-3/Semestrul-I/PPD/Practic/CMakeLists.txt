cmake_minimum_required(VERSION 4.0)
project(Practic)
find_package(MPI REQUIRED)
include_directories(SYSTEM ${MPI_INCLUDE_PATH})
set(CMAKE_CXX_STANDARD 20)

add_executable(Practic main.cpp
        Generate.cpp
        Generate.h)
target_link_libraries(Practic ${MPI_LIBRARIES})