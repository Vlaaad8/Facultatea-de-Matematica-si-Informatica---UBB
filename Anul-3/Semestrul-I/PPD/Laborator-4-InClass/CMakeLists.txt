cmake_minimum_required(VERSION 4.0)
project(Laborator_4_InClass)

set(CMAKE_CXX_STANDARD 20)

find_package(OpenMP REQUIRED)

add_executable(Laborator_4_InClass main.cpp)

target_link_libraries(Laborator_4_InClass PRIVATE OpenMP::OpenMP_CXX)