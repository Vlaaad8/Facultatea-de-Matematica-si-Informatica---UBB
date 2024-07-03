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
    [[nodiscard]] int get_apartament() const;
    [[nodiscard]] int get_suprafata() const;
    [[nodiscard]] string get_nume_proprietar() const;
    [[nodiscard]] string get_tip_apartament() const;
    void set_apartament(int apartament2);
    void set_suprafata(int suprafata2);
    void set_nume_proprietar(const string& nume_proprietar2);
    void set_tip_apartament(const string& tip_apartament2);
    static Locatar creeaza_locatar(Locatar locatar, int apartament2, const string& nume_proprietar2, int suprafata2, const string& tip_apartament2);
    Locatar(const Locatar& locatar) = default;
    Locatar() : apartament(0), nume_proprietar(), suprafata(0), tip_apartament() { cout << "*\n"; }
};