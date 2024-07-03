#include <iostream>
#ifndef EXCEPTION_H
#define EXCEPTION_H

class repo_exception {
    std::string msg;
public:
    explicit repo_exception(std::string msg) :msg{ std::move(msg) } {}
    std::string get_msg() {
        return msg;
    }
};

class validator_exception {
    std::string msg;
public:
    explicit validator_exception(std::string msg) :msg{ std::move(msg) } {}
    std::string get_msg() {
        return msg;
    }
};


#endif // EXCEPTION_H

