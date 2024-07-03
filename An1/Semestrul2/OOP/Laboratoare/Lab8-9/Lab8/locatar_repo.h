#pragma once
#include "locatar.h"
#include <vector>
#include <map>
#include <string>
#include<fstream>
#include "exception.h"
using namespace std;
class FloatException : public std::exception {
public:
    FloatException(float value) : value(value) {}

    const char* what() const noexcept override {
        return "Probabilitatea este";
    }

    float getValue() const {
        return value;
    }

private:
    float value;
};


class AbstractRepo
{
public:
    AbstractRepo() = default;
    virtual void store(const Locatar& locatar) = 0;
    virtual void store2(const Locatar& locatar) = 0;
    virtual void modify(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) = 0;
    virtual vector<Locatar> get_all() const = 0;
    virtual Locatar find(int apartament) = 0;
    virtual void destroy(int apartament, const string& nume_proprietar)=0;
    virtual ~AbstractRepo() = default;
};


class MapRepo : public AbstractRepo
{
private:
    map<size_t, Locatar> dict = {};
    float prob;
public:
    MapRepo(const float& prob) : prob{ prob } {};

    void store(const Locatar& locatar) override {
        dict.insert(pair<size_t, Locatar>(locatar.get_apartament(), locatar));
        if (prob > 0 and prob < 1)
        throw FloatException(prob);}
    virtual vector<Locatar> get_all() const override {
        vector<Locatar> list = {};
        for (auto& pair : dict)
            list.push_back(pair.second);
        return list;
    }

    void store2(const Locatar& locatar) override {
        dict.insert(pair<size_t, Locatar>(locatar.get_apartament(), locatar));
        //throw FloatException(prob);
    }
    void modify(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) override {
        for (auto& pair :dict ) {
            if (pair.first == apartament) {
                if (pair.second.get_apartament() == apartament) {
                    pair.second.set_nume_proprietar(nume_proprietar);
                    pair.second.set_suprafata(suprafata);
                    pair.second.set_tip_apartament(tip_apartament);
                    if(prob>0 and prob<1)
                    throw FloatException(prob);}}}}


    Locatar find(int apartament) override{
        for (const auto& pair : dict) {
            if (pair.first == apartament) {
                return pair.second;
            }
        }
        throw repo_exception("No matching locatar found");}


    void destroy(int apartament, const string& nume_proprietar2) override {
        auto it = dict.find(apartament);
        if (it != dict.end() && it->second.get_nume_proprietar() == nume_proprietar2) {
            dict.erase(it);
        }
        throw  FloatException(prob);}
    ~MapRepo() override = default;

};
class locatar_repo: public AbstractRepo
{
private:
    std::vector<Locatar> locatari;
public:
    locatar_repo(const locatar_repo& repo) = delete;
    locatar_repo() = default;
    void store(const Locatar& locatar) override;
    std::vector<Locatar> get_all() const override;
    void destroy(int apartament, const string& nume_proprietar) override;
    void modify(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) override;
	void store2(const Locatar& locatar) override;
    Locatar find(int apartament) override;
    ~locatar_repo() = default;
};


