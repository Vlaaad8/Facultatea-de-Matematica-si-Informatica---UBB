#include "Domain.h"

int Locatar::getApartament() const
{
    //Returneaza apartamentul
    return apartament;
}

int Locatar::getSuprafata() const
{
    //Returneaza suprafata
    return suprafata;
}

string Locatar::getProprietar() const
{
    //Returneaza numele proprietarului
    return nume_proprietar;
}

string Locatar::getTip() const
{
    //Returneaza tipul apartamentului
    return tip_apartament;
}

void Locatar::setApartament(int apartament2)
{
    //Seteaza apartamentul
    this->apartament = apartament2;
}

void Locatar::setSuprafata(int suprafata2)
{
    //Seteaza suprafata
    this->suprafata = suprafata2;
}

void Locatar::setProprietar(const string& nume_proprietar2)
{
    //Seteaza numele proprietarului
    this->nume_proprietar = nume_proprietar2;
}

void Locatar::setTip(const string& tip_apartament2)
{
    //Seteaza tipul apartamentului
    this->tip_apartament = tip_apartament2;
}

Locatar Locatar::creeaza_locatar(Locatar locatar, int apartament2, const string& nume_proprietar2, int suprafata2, const string& tip_apartament2)
{
    // Creeaza un locatar
    locatar.setApartament(apartament2);
    locatar.setProprietar(nume_proprietar2);
    locatar.setSuprafata(suprafata2);
    locatar.setTip(tip_apartament2);
    return locatar;
}
