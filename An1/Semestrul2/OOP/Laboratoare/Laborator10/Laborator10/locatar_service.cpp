#include <vector>
#include "locatar_service.h"
#include "locatar.h"
#include "validator.h"
#include <algorithm>
#include <random>
#include <map>
#include <chrono>
#include <fstream>
#include "undo.h"
#include <ios>
#include "exception.h"
#pragma warning( disable : 4244 )

void locatar_service::add(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) {
    //Adauga un locatar daca este valid
    Locatar locatar;
    locatar = Locatar::creeaza_locatar(locatar, apartament, nume_proprietar, suprafata, tip_apartament);
    validator::validate_locatar(locatar);
    undoStack.push_back(std::make_unique<UndoAdauga>(repo, locatar));
    try {
        repo.store(locatar);
        //undoStack.push_back(std::make_unique<UndoAdauga>(repo, locatar));
    }
    catch (const  FloatException& prob) {
        cout << "Probabilitatea este: " << prob.getValue() << endl;
        //undoStack.push_back(make_unique<UndoAdauga>(repo, locatar));
    }
}

std::vector<Locatar> locatar_service::get_all() const {
    //Returneaza toti locatarii
    return repo.get_all();
}


void locatar_service::sterge(int apartament, const string& nume_proprietar) {
    //Sterge un locatar
    Locatar locatar = repo.find(apartament);
    undoStack.push_back(std::make_unique<UndoSterge>(repo, locatar));
    try {
        repo.destroy(apartament, nume_proprietar);
    }
    catch (const FloatException& prob) {
        cout << "Probabilitatea este: " << prob.getValue() << endl;
    }
}

void locatar_service::modifica(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) {
    //Modifica un locatar daca este valid
    Locatar oldLocatar = repo.find(apartament);
    Locatar locatar;
    locatar = Locatar::creeaza_locatar(locatar, apartament, nume_proprietar, suprafata, tip_apartament);
    validator::validate_locatar(locatar);
    undoStack.push_back(std::make_unique<UndoModifica>(repo, oldLocatar, locatar));
    try {
        repo.modify(apartament, nume_proprietar, suprafata, tip_apartament);
    }
    catch (const FloatException& prob) {
        cout << "Probabilitatea este: " << prob.getValue() << endl;
    }
}

Locatar locatar_service::cauta(int apartament) {
    const std::vector<Locatar>& locatari = repo.get_all();
    for (const auto& locatar : locatari) {
        if (locatar.get_apartament() == apartament) {
            return locatar;
        }
    }
    // If no matching locatar is found, throw an exception or return a default value
    throw repo_exception("No matching locatar found");}

std::vector<Locatar> locatar_service::filtrare_tip(const string& tip_apartament) {
    // Filtreaza locatarii dupa tipul apartamentului
    const std::vector<Locatar>& locatari = repo.get_all();
    std::vector<Locatar> locatari_filtrate;

    std::copy_if(locatari.begin(), locatari.end(), std::back_inserter(locatari_filtrate),
        [&tip_apartament](const Locatar& locatar) {
            return locatar.get_tip_apartament() == tip_apartament;
        });

    return locatari_filtrate;
}

std::vector<Locatar> locatar_service::filtrare_suprafata(int suprafata) {
    // Filtreaza locatarii dupa suprafata
    const std::vector<Locatar>& locatari = repo.get_all();
    std::vector<Locatar> locatari_filtrate;

    std::copy_if(locatari.begin(), locatari.end(), std::back_inserter(locatari_filtrate),
        [suprafata](const Locatar& locatar) {
            return locatar.get_suprafata() == suprafata;
        });

    return locatari_filtrate;
}

std::vector<Locatar> locatar_service::sortare_nume_proprietar() {
    //Sorteaza locatarii dupa numele proprietarului
    std::vector<Locatar> locatari = repo.get_all();

    std::sort(locatari.begin(), locatari.end(), [](const Locatar& a, const Locatar& b) {
        return a.get_nume_proprietar() < b.get_nume_proprietar();
        });

    return locatari;
}


std::vector<Locatar> locatar_service::sortare_suprafata() {
    //Sorteaza locatarii dupa suprafata
    std::vector<Locatar> locatari = repo.get_all();

    std::sort(locatari.begin(), locatari.end(),
        [](const Locatar& a, const Locatar& b) {
            return a.get_suprafata() < b.get_suprafata();
        });

    return locatari;
}

std::vector<Locatar> locatar_service::sortare_tip_suprafata() {
    //Sorteaza locatarii dupa tipul apartamentului si suprafata
    std::vector<Locatar> locatari = repo.get_all();

    std::sort(locatari.begin(), locatari.end(),
        [](const Locatar& a, const Locatar& b) {
            if (a.get_tip_apartament() == b.get_tip_apartament()) {
                return a.get_suprafata() < b.get_suprafata();
            }
            return a.get_tip_apartament() < b.get_tip_apartament();
        });

    return locatari;
}


void locatar_service::add_notification(int apartment) {
    //Adauga un apartament in lista de notificari
    auto it = std::find(notification_list.begin(), notification_list.end(), apartment);
    if (it == notification_list.end()) {
        notification_list.push_back(apartment);
    }
}

void locatar_service::clear_notifications() {
    //Sterge toate notificarile
    notification_list.clear();
}

const std::vector<int>& locatar_service::get_notifications() const {
    //Returneaza lista de notificari
    return notification_list;
}

void locatar_service::generate_notifications(int count) {
    //Genereaza apartamente random pentru notificari
    std::vector<int> apartments;
    for (int i = 1; i <= 1000; ++i) {
        apartments.push_back(i);
    }

    std::random_device rd;
    std::mt19937 mt(rd());

    std::shuffle(apartments.begin(), apartments.end(), mt);

    int added = 0;
    for (int i = 0; i < apartments.size() && added < count; ++i) {
        if (std::find(notification_list.begin(), notification_list.end(), apartments[i]) == notification_list.end()) {
            add_notification(apartments[i]);
            added++;
        }
    }
}


std::map<std::string, int> locatar_service::raport_tip_apartament() {
    //Genereaza un raport pentru tipul de apartamente
    std::map<std::string, int> raport;
    const std::vector<Locatar>& locatari = repo.get_all();

    for (const auto& locatar : locatari) {
        raport[locatar.get_tip_apartament()]++;
    }

    return raport;
}


void locatar_service::undo() {
    if (undoStack.empty())
        throw repo_exception("Nu mai exista operatii la care sa executam undo!\n");

    try
    {
        undoStack.back()->doUndo();
    }
    catch (const FloatException& prob)
    {
        cout << "Probabilitatea este: " << prob.getValue() << endl;
        undoStack.pop_back();
    }
}
void locatar_service::exportHTML(const string& fileName)
{
    ofstream out;
    out.open(fileName, ios::trunc);
    if (!out.is_open()) throw repo_exception("Fisierul nu a putut fi deschis!\n");
    out << "<html><body>" << endl << "<table border=\"1\" style=\"width:100 % \">" << endl;
    for (const auto& apartment : get_notifications()) out << "<tr>" << endl << "<td>" << apartment << "</td>" << endl << "</tr>" << endl;
    out << "</table>" << endl << "</body></html>" << endl;
    out.close();
}
void locatar_service::exportCSV(const string& fileName)
{
    ofstream out;
    out.open(fileName, ios::trunc);
    for (const auto& apartment : get_notifications()) out << apartment << '\n';
    out.close();
}

void locatar_service::exportFILE(const string& fileName)
{
    ofstream out;
    out.open(fileName, ios::trunc);
    for (const auto& apartment : get_all())
        out << apartment.get_apartament() << " " << apartment.get_nume_proprietar() << " " << apartment.get_suprafata() << " " << apartment.get_tip_apartament() << '\n';
    out.close();
}

void locatar_service::importFILE(const string& fileName)
{
    ifstream in;
    in.open(fileName);
    int apartament;
    string nume_proprietar;
    int suprafata;
    string tip_apartament;

    while (in >> apartament >> nume_proprietar >> suprafata >> tip_apartament) {
        Locatar locatar;
        locatar = Locatar::creeaza_locatar(locatar, apartament, nume_proprietar, suprafata, tip_apartament);
        repo.store2(locatar);
    }

    in.close();
}

