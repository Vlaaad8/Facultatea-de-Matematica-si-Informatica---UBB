cmake_minimum_required(VERSION 4.0)
project(Tema_2)

set(CMAKE_CXX_STANDARD 20)

add_executable(Tema_2 main.cpp
        Partea-1/source/AF.cpp
        Partea-1/header/AF.h
        Partea-1/source/af_reader.cpp
        Partea-1/header/af_reader.h
       )


add_executable(CreateTable
        Partea-2/createTS.cpp
        Partea-2/header/createTS.h
        Partea-2/source/HashTable.cpp
        Partea-2/header/HashTable.h
        Partea-2/source/atom_generator.cpp
        Partea-2/header/atom_generator.h
        Partea-2/header/validator.h
        Partea-2/source/validator.cpp
        Partea-1/source/AF.cpp
        Partea-1/header/AF.h
        Partea-1/source/af_reader.cpp
        Partea-1/header/af_reader.h
)