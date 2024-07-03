#include "locatar_repo.h"
#include "locatar.h"
#include "exception.h"
#include <algorithm>

void locatar_repo::store(const Locatar& locatar) {
    //Adauga un locatar in lista de locatari
    for (const Locatar& l : locatari)
        if (l.get_apartament() == locatar.get_apartament() && l.get_nume_proprietar() == locatar.get_nume_proprietar())
            throw repo_exception("Locatar deja existent!");
    locatari.push_back(locatar);
    //saveToFile("locatari.txt");
}

std::vector<Locatar> locatar_repo::get_all() const{
    //Returneaza lista de locatari
    return locatari;
}

void locatar_repo::destroy(int apartament, const string& nume_proprietar) {
    // Sterge un locatar din lista de locatari
    auto it = std::find_if(locatari.begin(), locatari.end(), [&](const Locatar& l) {
        return l.get_apartament() == apartament && l.get_nume_proprietar() == nume_proprietar;
        });

    if (it != locatari.end()) {
        locatari.erase(it);
        //saveToFile("locatari.txt");
    }
    else {
        throw repo_exception("Locatar inexistent!");
    }
}





void locatar_repo::modify(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) {
    //Modifica un locatar din lista de locatari
    for (auto& locatar : locatari) {
        if (locatar.get_apartament() == apartament) {
            if (locatar.get_apartament() == apartament) {
                locatar.set_nume_proprietar(nume_proprietar);
                locatar.set_suprafata(suprafata);
                locatar.set_tip_apartament(tip_apartament);
                return;
            }
        }
    }
    //saveToFile("locatari.txt");
    throw repo_exception("Locatar inexistent!");
    //saveToFile("locatari.txt");
}


Locatar locatar_repo::find(int apartament)
{
    auto it = std::find_if(locatari.begin(), locatari.end(), [apartament](const Locatar& locatar) {
        return locatar.get_apartament() == apartament;
        });

    if (it != locatari.end()) {
        return *it;}
    else {
        throw repo_exception("Locatar inexistent!");}}

void locatar_repo::store2(const Locatar& locatar) {
	//Adauga un locatar in lista de locatari
	locatari.push_back(locatar);
	//saveToFile("locatari.txt");
}