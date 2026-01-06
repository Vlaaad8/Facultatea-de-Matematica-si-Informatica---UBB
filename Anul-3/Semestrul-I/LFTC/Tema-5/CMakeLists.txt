cmake_minimum_required(VERSION 4.0)
project(Tema_5)

set(CMAKE_CXX_STANDARD 20)

add_executable(Tema_5 main.cpp
        source/Gramatic.cpp
        header/Gramatic.h
        source/SyntacticAnalyzer.cpp
        header/SyntacticAnalyzer.h)
