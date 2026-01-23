//
// Created by vladb on 27/10/2025.
//

#ifndef TEMA_2_AF_READER_H
#define TEMA_2_AF_READER_H
#include "AF.h"

#include<fstream>
#include<string>
using namespace std;

class af_reader {

    public:
    static void read_from_file(const string &file_name,AF &af);
    static void read_from_command(AF &af);
};


#endif //TEMA_2_AF_READER_H