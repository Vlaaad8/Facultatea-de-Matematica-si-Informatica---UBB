cmake_minimum_required(VERSION 4.0)
project(C__)

set(CMAKE_CXX_STANDARD 20)

add_executable(C__ main.cpp
        source/MatrixGenerator.cpp
        header/MatrixGenerator.h
        source/ReadFromFile.cpp
        header/ReadFromFile.h
        source/ConvolutionS.cpp
        header/ConvolutionS.h
        source/ConvolutionH.cpp
        header/ConvolutionH.h
        source/ConvolutionV.cpp
        header/ConvolutionV.h
        source/EstimateTime.cpp
        header/EstimateTime.h
)

add_executable(StaticMain
        static.cpp
)

add_executable(StaticBig
        staticBig.cpp
)
