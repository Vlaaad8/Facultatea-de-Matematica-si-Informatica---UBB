#include "Service.h"
#include <vector>
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

void Service::add(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) {
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
    notify();
}

std::vector<Locatar> Service::get_all() const {
    //Returneaza toti locatarii
    return repo.get_all();
}


void Service::sterge(int apartament, const string& nume_proprietar) {
    //Sterge un locatar
    Locatar locatar = repo.find(apartament);
    undoStack.push_back(std::make_unique<UndoSterge>(repo, locatar));
    try {
        repo.destroy(apartament, nume_proprietar);
    }
    catch (const FloatException& prob) {
        cout << "Probabilitatea este: " << prob.getValue() << endl;
    }
    notify();
}

void Service::modifica(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament) {
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
    notify();
}

Locatar Service::cauta(int apartament) {
    const std::vector<Locatar>& locatari = repo.get_all();
    for (const auto& locatar : locatari) {
        if (locatar.getApartament() == apartament) {
            return locatar;
        }
    }
    // If no matching locatar is found, throw an exception or return a default value
    throw repo_exception("No matching locatar found");
}

std::vector<Locatar> Service::filtrare_tip(const string& tip_apartament) {
    // Filtreaza locatarii dupa tipul apartamentului
    const std::vector<Locatar>& locatari = repo.get_all();
    std::vector<Locatar> locatari_filtrate;

    std::copy_if(locatari.begin(), locatari.end(), std::back_inserter(locatari_filtrate),
        [&tip_apartament](const Locatar& locatar) {
            return locatar.getTip() == tip_apartament;
        });

    return locatari_filtrate;
}

std::vector<Locatar> Service::filtrare_suprafata(int suprafata) {
    // Filtreaza locatarii dupa suprafata
    const std::vector<Locatar>& locatari = repo.get_all();
    std::vector<Locatar> locatari_filtrate;

    std::copy_if(locatari.begin(), locatari.end(), std::back_inserter(locatari_filtrate),
        [suprafata](const Locatar& locatar) {
            return locatar.getSuprafata() == suprafata;
        });

    return locatari_filtrate;
}

std::vector<Locatar> Service::sortare_nume_proprietar() {
    //Sorteaza locatarii dupa numele proprietarului
    std::vector<Locatar> locatari = repo.get_all();

    std::sort(locatari.begin(), locatari.end(), [](const Locatar& a, const Locatar& b) {
        return a.getProprietar() < b.getProprietar();
        });

    return locatari;
}


std::vector<Locatar> Service::sortare_suprafata() {
    //Sorteaza locatarii dupa suprafata
    std::vector<Locatar> locatari = repo.get_all();

    std::sort(locatari.begin(), locatari.end(),
        [](const Locatar& a, const Locatar& b) {
            return a.getSuprafata() < b.getSuprafata();
        });

    return locatari;
}

std::vector<Locatar> Service::sortare_tip_suprafata() {
    //Sorteaza locatarii dupa tipul apartamentului si suprafata
    std::vector<Locatar> locatari = repo.get_all();

    std::sort(locatari.begin(), locatari.end(),
        [](const Locatar& a, const Locatar& b) {
            if (a.getTip() == b.getTip()) {
                return a.getSuprafata() < b.getSuprafata();
            }
            return a.getTip() < b.getTip();
        });

    return locatari;
}

/// <summary>
/// 
/// </summary>
/// <param name="apartment"></param>
/// <param name="nume"></param>
/// <param name="supr"></param>
/// <param name="tip"></param>
void Service::add_notification(int apartment, string& nume, int supr, string& tip) {
    //Adauga un apartament in lista de notificari
    Locatar l;
    l = Locatar::creeaza_locatar(l, apartment, nume, supr, tip);
    //auto it = std::find(notification_list.begin(), notification_list.end(), l);
    //if (it == notification_list.end()) {
        //notification_list.push_back(l);
    //}
    notification_list.push_back(l);
    notify();
}

void Service::clear_notifications() {
    //Sterge toate notificarile
    notification_list.clear();
    notify();
}

const std::vector<Locatar>& Service::get_notifications() const {
    //Returneaza lista de notificari
    return notification_list;
}

void Service::generate_notifications() {
    //Genereaza apartamente random pentru notificari
    std::vector<Locatar> v = repo.get_all();

    std::mt19937 mt{ std::random_device{}() };
    std::uniform_int_distribution<> dist(0, v.size() - 1);
    int rndNr = dist(mt);// numar aleator intre [0,size-1]

    //std::cout << "Debug: Value of x is " << rndNr << std::endl;
    auto seed = std::chrono::system_clock::now().time_since_epoch().count();
    std::shuffle(v.begin(), v.end(), std::default_random_engine(seed)); //amesteca vectorul v

    for (int i = 0; i < rndNr; i++) {
        // Check if the notification already exists
        auto it = std::find(notification_list.begin(), notification_list.end(), v[i]);

        // If the notification does not exist, add it to the list
        if (it == notification_list.end()) {
            notification_list.push_back(v[i]);
        }
    }
    notify();
}


std::map<std::string, int> Service::raport_tip_apartament() {
    //Genereaza un raport pentru tipul de apartamente
    std::map<std::string, int> raport;
    const std::vector<Locatar>& locatari = repo.get_all();

    for (const auto& locatar : locatari) {
        raport[locatar.getTip()]++;
    }

    return raport;
}


void Service::undo() {
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
    notify();
}
void Service::exportHTML(const string& fileName)
{
    ofstream out;
    out.open(fileName, ios::trunc);
    if (!out.is_open()) throw repo_exception("Fisierul nu a putut fi deschis!\n");

    out << "<html><body>" << endl << "<table border=\"1\" style=\"width:100 % \">" << endl;
    generate_notifications();
    vector<Locatar> apartment = get_notifications();
    for (const auto& ap : apartment) out << "<tr>" << endl << "<td>" << ap.getApartament() << "</td>" << "<td>" << ap.getProprietar() <<
        "</td>" << "<td>" << ap.getSuprafata() << "</td>" << "<td>" << ap.getTip() << "</td>" << endl << "</tr>" << endl;
    out << "</table>" << endl << "</body></html>" << endl;
    out.close();
}
void Service::exportCSV(const string& fileName)
{
    ofstream out;
    out.open(fileName, ios::trunc);
    //generate_notifications();
    vector<Locatar> ap = get_notifications();
    for (const auto& apartment : ap) out << apartment.getApartament() << " " << apartment.getProprietar() << " " << apartment.getSuprafata() << " " << apartment.getTip() << '\n';
    out.close();
}

void Service::exportFILE(const string& fileName)
{
    ofstream out;
    out.open(fileName, ios::trunc);
    for (const auto& apartment : get_all())
        out << apartment.getApartament() << " " << apartment.getProprietar() << " " << apartment.getSuprafata() << " " << apartment.getTip() << '\n';
    out.close();
}

void Service::importFILE(const string& fileName)
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
size_t Service::sizeNotifications() {
    return notification_list.size();
}