#include "locatar.h"

int Locatar::get_apartament() const
{
    //Returneaza apartamentul
    return apartament;
}

int Locatar::get_suprafata() const
{
    //Returneaza suprafata
    return suprafata;
}

string Locatar::get_nume_proprietar() const
{
    //Returneaza numele proprietarului
    return nume_proprietar;
}

string Locatar::get_tip_apartament() const
{
    //Returneaza tipul apartamentului
    return tip_apartament;
}

void Locatar::set_apartament(int apartament2)
{
    //Seteaza apartamentul
    this->apartament = apartament2;
}

void Locatar::set_suprafata(int suprafata2)
{
    //Seteaza suprafata
    this->suprafata = suprafata2;
}

void Locatar::set_nume_proprietar(const string& nume_proprietar2)
{
    //Seteaza numele proprietarului
    this->nume_proprietar = nume_proprietar2;
}

void Locatar::set_tip_apartament(const string& tip_apartament2)
{
    //Seteaza tipul apartamentului
    this->tip_apartament = tip_apartament2;
}

Locatar Locatar::creeaza_locatar(Locatar locatar, int apartament2, const string& nume_proprietar2, int suprafata2, const string& tip_apartament2)
{
    // Creeaza un locatar
    locatar.set_apartament(apartament2);
    locatar.set_nume_proprietar(nume_proprietar2);
    locatar.set_suprafata(suprafata2);
    locatar.set_tip_apartament(tip_apartament2);
    return locatar;
}
