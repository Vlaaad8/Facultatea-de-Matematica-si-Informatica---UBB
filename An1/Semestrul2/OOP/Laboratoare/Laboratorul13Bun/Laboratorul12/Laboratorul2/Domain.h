#pragma once
#include <string>
#include <iostream>
using std::string;
using std::cout;

class Locatar
{
private:
    int apartament;
    string nume_proprietar;
    int suprafata;
    string tip_apartament;
public:
    bool operator==(const Locatar& other) const {
        // Compare the relevant fields
        return this->apartament == other.apartament &&
            this->nume_proprietar == other.nume_proprietar &&
            this->suprafata == other.suprafata &&
            this->tip_apartament == other.tip_apartament;
    }
    [[nodiscard]] int getApartament() const;
    [[nodiscard]] int getSuprafata() const;
    [[nodiscard]] string getProprietar() const;
    [[nodiscard]] string getTip() const;
    void setApartament(int apartament2);
    void setSuprafata(int suprafata2);
    void setProprietar(const string& nume_proprietar2);
    void setTip(const string& tip_apartament2);
    static Locatar creeaza_locatar(Locatar locatar, int apartament2, const string& nume_proprietar2, int suprafata2, const string& tip_apartament2);
    Locatar(const Locatar& locatar) = default;
    Locatar() : apartament(0), nume_proprietar(), suprafata(0), tip_apartament() { cout << "*\n"; }
};