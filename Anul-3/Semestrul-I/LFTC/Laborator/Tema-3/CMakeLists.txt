cmake_minimum_required(VERSION 3.10)

project(Tema_3 LANGUAGES C)
set(CMAKE_C_STANDARD 11)


add_custom_command(
        OUTPUT ${CMAKE_CURRENT_BINARY_DIR}/lex.yy.c
        COMMAND flex -o lex.yy.c ${CMAKE_SOURCE_DIR}/flexAnalyzer.l
        DEPENDS flexAnalyzer.l
)

add_executable(Tema_3
        ${CMAKE_CURRENT_BINARY_DIR}/lex.yy.c
)
target_link_libraries(Tema_3
        "C:/msys64/mingw64/lib/libfltlib.a"
)
