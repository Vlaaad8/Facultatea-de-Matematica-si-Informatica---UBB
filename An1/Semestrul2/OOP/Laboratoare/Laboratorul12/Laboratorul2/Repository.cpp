#include "Repository.h"
#include "exception.h"
#include <algorithm>

void Repository::store(const Locatar& locatar) {
    //Adauga un locatar in lista de locatari
    for (const Locatar& l : locatari)
        if (l.getApartament() == locatar.getApartament() && l.getProprietar() == locatar.getProprietar())
            throw repo_exception("Locatar deja existent!");
    locatari.push_back(locatar);
    //saveToFile("locatari.txt");
}

std::vector<Locatar> Repository::get_all() const {
    //Returneaza lista de locatari
    return locatari;
}

void Repository::destroy(int apartament, const string& nume_proprietar) {
    // Sterge un locatar din lista de locatari
    auto it = std::find_if(locatari.begin(), locatari.end(), [&](const Locatar& l) {
        return l.getApartament() == apartament && l.getProprietar() == nume_proprietar;
        });

    if (it != locatari.end()) {
        locatari.erase(it);
        //saveToFile("locatari.txt");
    }
    else {
        throw repo_exception("Locatar inexistent!");
    }
}





void Repository::modify(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) {
    //Modifica un locatar din lista de locatari
    for (auto& locatar : locatari) {
        if (locatar.getApartament() == apartament) {
            if (locatar.getApartament() == apartament) {
                locatar.setProprietar(nume_proprietar);
                locatar.setSuprafata(suprafata);
                locatar.setTip(tip_apartament);
                return;
            }
        }
    }
    //saveToFile("locatari.txt");
    throw repo_exception("Locatar inexistent!");
    //saveToFile("locatari.txt");
}


Locatar Repository::find(int apartament)
{
    auto it = std::find_if(locatari.begin(), locatari.end(), [apartament](const Locatar& locatar) {
        return locatar.getApartament() == apartament;
        });

    if (it != locatari.end()) {
        return *it;
    }
    else {
        throw repo_exception("Locatar inexistent!");
    }
}

void Repository::store2(const Locatar& locatar) {
    //Adauga un locatar in lista de locatari
    locatari.push_back(locatar);
    //saveToFile("locatari.txt");
}