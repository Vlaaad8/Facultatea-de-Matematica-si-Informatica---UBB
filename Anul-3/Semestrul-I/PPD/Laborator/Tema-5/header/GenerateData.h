//
// Created by vladb on 17/11/2025.
//

#ifndef TEMA_4_GENERATEDATA_H
#define TEMA_4_GENERATEDATA_H
#include <string>
using namespace std;

class GenerateData {
public:
    static void generateData(const string& fileName,int numberOfStudents,int minNotes);
    static void generateDataBase(const string& tableName,int numberOfStudents,int minNotes);
};


#endif //TEMA_4_GENERATEDATA_H