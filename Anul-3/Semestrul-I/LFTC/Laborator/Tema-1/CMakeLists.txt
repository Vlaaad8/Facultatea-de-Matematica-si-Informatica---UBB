cmake_minimum_required(VERSION 3.22)
project(Tema_1)

set(CMAKE_CXX_STANDARD 20)

# ------------------------------
# Partea 1 - Exercitiul 2
# ------------------------------
add_executable(ariaP Partea-1/Exercitiul-2/ariaP.cpp
        Partea-2/source/atom_generator.cpp
        Partea-2/source/validator.cpp
        Partea-2/header/validator.h)
add_executable(cmmdc Partea-1/Exercitiul-2/cmmdc.cpp)
add_executable(nSum Partea-1/Exercitiul-2/nSum.cpp)

# ------------------------------
# Partea 1 - Exercitiul 3
# ------------------------------
add_executable(errorSame Partea-1/Exercitiul-3/errorSame.cpp)
add_executable(onlyMLP Partea-1/Exercitiul-3/onlyMLP.cpp)
add_executable(inClass Partea-1/Exercitiul-3/inClass.cpp)

# ------------------------------
# Partea 2
# ------------------------------

# Programul createTS - are doar funcția de generare a Tabelului de Simboluri
add_executable(createTS
        Partea-2/source/createTS.cpp
        Partea-2/source/HashTable.cpp     # <— trebuie adăugat aici
        Partea-2/source/atom_generator.cpp
        Partea-2/source/validator.cpp
)


# Programul constantTable - folosește și headerul createTS.h
add_executable(constantTable
        Partea-2/constantTable.cpp
        Partea-2/header/createTS.h
        Partea-2/source/HashTable.cpp
        Partea-2/header/HashTable.h
        Partea-2/source/atom_generator.cpp
        Partea-2/header/atom_generator.h
)

# Programul identificatorAtom - folosește și funcțiile din createTS.cpp
add_executable(identificatorAtom
        Partea-2/source/createTS.cpp
)
