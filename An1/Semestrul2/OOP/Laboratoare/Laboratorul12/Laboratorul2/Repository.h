#pragma once
#include "Domain.h"
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
    virtual void destroy(int apartament, const string& nume_proprietar) = 0;
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
        dict.insert(pair<size_t, Locatar>(locatar.getApartament(), locatar));
        throw FloatException(prob);
    }
    virtual vector<Locatar> get_all() const override {
        vector<Locatar> list = {};
        for (auto& pair : dict)
            list.push_back(pair.second);
        return list;
    }

    void store2(const Locatar& locatar) override {
        dict.insert(pair<size_t, Locatar>(locatar.getApartament(), locatar));
        //throw FloatException(prob);
    }
    void modify(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) override {
        for (auto& pair : dict) {
            if (pair.first == apartament) {
                if (pair.second.getApartament() == apartament) {
                    pair.second.setProprietar(nume_proprietar);
                    pair.second.setSuprafata(suprafata);
                    pair.second.setTip(tip_apartament);
                    throw FloatException(prob);
                }
            }
        }
    }


    Locatar find(int apartament) override {
        for (const auto& pair : dict) {
            if (pair.first == apartament) {
                return pair.second;
            }
        }
        throw repo_exception("No matching locatar found");
    }


    void destroy(int apartament, const string& nume_proprietar2) override {
        auto it = dict.find(apartament);
        if (it != dict.end() && it->second.getProprietar() == nume_proprietar2) {
            dict.erase(it);
        }
        throw  FloatException(prob);
    }
    ~MapRepo() override = default;

};
class Repository : public AbstractRepo
{
private:
    std::vector<Locatar> locatari;
public:
    Repository(const Repository& repo) = delete;
    Repository() = default;
    void store(const Locatar& locatar) override;
    std::vector<Locatar> get_all() const override;
    void destroy(int apartament, const string& nume_proprietar) override;
    void modify(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) override;
    void store2(const Locatar& locatar) override;
    Locatar find(int apartament) override;
    ~Repository() = default;
};


