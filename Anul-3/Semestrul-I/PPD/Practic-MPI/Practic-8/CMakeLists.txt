cmake_minimum_required(VERSION 4.0)
project(Practic_8)
find_package(MPI REQUIRED)
include_directories(SYSTEM ${MPI_INCLUDE_PATH})
set(CMAKE_CXX_STANDARD 20)

add_executable(Practic_8 main.cpp)
target_link_libraries(Practic_8 ${MPI_LIBRARIES})