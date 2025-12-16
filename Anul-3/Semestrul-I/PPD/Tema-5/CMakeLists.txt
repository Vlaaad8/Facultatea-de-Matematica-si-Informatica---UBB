cmake_minimum_required(VERSION 4.0)
project(Tema_5)

set(CMAKE_CXX_STANDARD 20)

add_executable(Tema_5 main.cpp
        source/LinkedList.cpp
        header/LinkedList.h
        source/GenerateData.cpp
        header/GenerateData.h
        source/CalculateNotes.cpp
        header/CalculateNotes.h
        source/QueueContainer.cpp
        header/QueueContainer.h
        source/CalculateNotesParallel.cpp
        header/CalculateNotesParallel.h
        header/ThreadPool.h)

target_link_libraries(Tema_5 sqlite3)