cmake_minimum_required(VERSION 3.10)
project(Tema_6 LANGUAGES C)

set(CMAKE_C_STANDARD 11)

set(BISON_SOURCE "${CMAKE_SOURCE_DIR}/parser.y")
set(FLEX_SOURCE "${CMAKE_SOURCE_DIR}/flexAnalyzer.l")

add_custom_command(
        OUTPUT ${CMAKE_CURRENT_BINARY_DIR}/parser.tab.c ${CMAKE_CURRENT_BINARY_DIR}/parser.tab.h
        COMMAND bison -d -o ${CMAKE_CURRENT_BINARY_DIR}/parser.tab.c ${BISON_SOURCE}
        DEPENDS ${BISON_SOURCE}
)


add_custom_command(
        OUTPUT ${CMAKE_CURRENT_BINARY_DIR}/lex.yy.c
        COMMAND flex -o ${CMAKE_CURRENT_BINARY_DIR}/lex.yy.c ${FLEX_SOURCE}
        DEPENDS ${FLEX_SOURCE} ${CMAKE_CURRENT_BINARY_DIR}/parser.tab.h
)


add_executable(Tema_6
        ${CMAKE_CURRENT_BINARY_DIR}/parser.tab.c
        ${CMAKE_CURRENT_BINARY_DIR}/lex.yy.c
        attribute.h
)

target_include_directories(Tema_6 PRIVATE ${CMAKE_CURRENT_BINARY_DIR} ${CMAKE_SOURCE_DIR})


target_link_libraries(Tema_6
        "C:/msys64/mingw64/lib/libfltlib.a"
)