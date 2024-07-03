#include "locatar_service.h"
#include "locatar.h"
#include "exception.h"
#include "locatar_repo.h"
#include "validator.h"
#include <cassert>
#include <vector>
#include <set>


void test_locatar()
//Testeaza clasa Locatar
{
    Locatar l;
    l = Locatar::creeaza_locatar(l, 1, "Popescu", 100, "apartament");
    assert(l.get_apartament() == 1);
    assert(l.get_nume_proprietar() == "Popescu");
    assert(l.get_suprafata() == 100);
    assert(l.get_tip_apartament() == "apartament");
    printf("Testele pentru clasa Locatar au trecut cu succes!\n");
}

void test_validator()
//Testeaza clasa validator
{
    Locatar l;
    l = Locatar::creeaza_locatar(l, 1, "Popescu", 100, "apartament");
    assert(l.get_apartament() == 1);
    assert(l.get_nume_proprietar() == "Popescu");
    assert(l.get_suprafata() == 100);
    assert(l.get_tip_apartament() == "apartament");
    validator::validate_locatar(l);
    l = Locatar::creeaza_locatar(l, 1, "", 100, "apartament");
    try {
        validator::validate_locatar(l);
        //assert(false);
    }
    catch (validator_exception& ex) {
        assert(ex.get_msg() == "Numele proprietarului nu poate fi vid.");
    }

    l = Locatar::creeaza_locatar(l, 1, "Popescu", 0, "apartament");
    try {
        validator::validate_locatar(l);
        //assert(false);
    }
    catch (validator_exception& ex) {
        assert(ex.get_msg() == "Suprafata trebuie sa fie un numar pozitiv nenul.");
    }

    l = Locatar::creeaza_locatar(l, 0, "Popescu", 100, "apartament");
    try {
        validator::validate_locatar(l);
        //assert(false);
    }
    catch (validator_exception& ex) {
        assert(ex.get_msg() == "Numarul apartamentului trebuie sa fie un numar pozitiv nenul.");
    }

    l = Locatar::creeaza_locatar(l, 1, "Popescu", 100, "");
    try {
        validator::validate_locatar(l);
        //assert(false);
    }
    catch (validator_exception& ex) {
        assert(ex.get_msg() == "Tipul apartamentului nu poate fi vid.");
    }
    printf("Testele pentru clasa Validator au trecut cu succes!\n");
}

void test_repo()
//Testeaza clasa Repo
{
    locatar_repo repo;
    Locatar l;
    Locatar l1;
    Locatar l2;
    l = Locatar::creeaza_locatar(l, 10, "Pop", 100, "super");
    repo.store(l);
    l1 = Locatar::creeaza_locatar(l, 100, "Popi", 1002, "super");
    repo.store(l1);
    l2 = Locatar::creeaza_locatar(l, 10, "Pop", 100, "super");
    try {
        repo.store(l2);
    }
    catch (repo_exception& msg) {
        assert(msg.get_msg() == "Locatar deja existent!");
    }
    const vector<Locatar>& locatari = repo.get_all();
    assert(locatari.size() == 2);

    repo.modify(10, "Pop", 10000, "cool");
    const vector<Locatar>& locatari2 = repo.get_all();
    assert(locatari2[0].get_suprafata() == 10000);
    assert(locatari2[0].get_tip_apartament() == "cool");

    try {
        repo.modify(20, "Vlad", 100, "super");
    }
    catch (repo_exception& msg) {
        assert(msg.get_msg() == "Locatar inexistent!");

    }
    Locatar l4 = repo.find(100);
    assert(l4.get_nume_proprietar() == l1.get_nume_proprietar());
    try {
        repo.find(12);
    }
    catch (repo_exception& msg)
    {
        assert(msg.get_msg() == "Locatar inexistent!");

    }
    repo.destroy(100, "Popi");
    const vector<Locatar>& locatari3 = repo.get_all();
    assert(locatari3.size() == 1);
    try {
        repo.destroy(12, "Balahura");
    }
    catch (repo_exception& msg) {
        assert(msg.get_msg() == "Locatar inexistent!");
    }
    MapRepo repoMap{ 0.2F };
    try {
        repoMap.store(l);
    }
    catch (FloatException& prob) {
        assert(prob.getValue() == 0.2F);
    }
    const vector<Locatar>& locatari4 = repoMap.get_all();
    assert(locatari4.size() == 1);
    try {
        repoMap.modify(10, "Pop", 1000, "supeeer");
    }
    catch (FloatException& prob) {
        assert(prob.getValue() == 0.2F);
    }
    try {
        repoMap.find(10000);
    }
    catch (repo_exception& msg) {
        assert(msg.get_msg() == "No matching locatar found");
    }
    Locatar m;
    m = repoMap.find(10);
    assert(m.get_nume_proprietar() == "Pop");
    try {
        repoMap.destroy(10, "Pop");
    }
    catch (FloatException& prob) {
        assert(prob.getValue() == 0.2F);
        assert(strcmp(prob.what(), "Probabilitatea este") == 0);;
    }

    cout << "Testele pentru clasa Repo au trecut cu succes!\n";
}
void test_store2() {
    locatar_repo repo;
    Locatar locatar;
    locatar = Locatar::creeaza_locatar(locatar, 1, "Popescu", 100, "apartament");
    repo.store2(locatar);

    // Check if the locatar is in the repository
    auto locatari = repo.get_all();
    auto it = std::find_if(locatari.begin(), locatari.end(), [&](const Locatar& l) {
        return l.get_apartament() == locatar.get_apartament() && l.get_nume_proprietar() == locatar.get_nume_proprietar();
        });

    assert(it != locatari.end());
}




void test_service()
//Testeaza clasa Service
{
    MapRepo repo{ 0.2F };
    locatar_service service{ repo };
    service.add(1, "nume1", 11, "apartament");
    const auto& locatari = service.get_all();
    assert(locatari.size() == 1);
    service.add(1, "nume1", 22, "apartament");
    service.add(2, "nume2", 33, "apartament");
    service.add(3, "nume3", 44, "apartament");
    service.sterge(1, "nume1");
    assert(service.get_all().size() == 2);
    try {
        service.sterge(1, "nume");
        //assert(false);
    }
    catch (repo_exception&) {
        assert(true);
    }
    service.modifica(2, "popan", 100, "apartament");
    assert(service.get_all()[0].get_suprafata() == 100);

    try {
        service.modifica(1, "nume", 100, "apartament");
        //assert(false);
    }
    catch (repo_exception&) {
        assert(true);
    }

    Locatar locatar = service.cauta(2);
    assert(locatar.get_nume_proprietar() == "popan");
    try {
        service.cauta(1);
        //assert(false);
    }
    catch (repo_exception&) {
        assert(true);
    }

    service.add(4, "nume4", 55, "apartament");

    std::vector<Locatar> locatari_filtrate = service.filtrare_suprafata(55);
    assert(locatari_filtrate.size() == 1);
    assert(locatari_filtrate[0].get_suprafata() == 55);

    std::vector<Locatar> locatari_filtrate2 = service.filtrare_suprafata(100);
    assert(locatari_filtrate2.size() == 1);
    assert(locatari_filtrate2[0].get_suprafata() == 100);

    std::vector<Locatar> locatari_filtrate3 = service.filtrare_tip("apartament");
    assert(locatari_filtrate3.size() == 3);

    std::vector<Locatar> locatari_filtrate4 = service.filtrare_tip("garsoniera");
    assert(locatari_filtrate4.empty() == 1);

    std::vector<Locatar> locatari_sortare = service.sortare_nume_proprietar();
    assert(locatari_sortare.size() == 3);
    assert(locatari_sortare[0].get_nume_proprietar() == "nume3");
    assert(locatari_sortare[1].get_nume_proprietar() == "nume4");
    assert(locatari_sortare[2].get_nume_proprietar() == "popan");
    service.add(5, "nume5", 1, "apartament");
    std::vector<Locatar> locatari_sortare2 = service.sortare_suprafata();
    assert(locatari_sortare2.size() == 4);
    assert(locatari_sortare2[0].get_suprafata() == 1);
    assert(locatari_sortare2[1].get_suprafata() == 44);
    assert(locatari_sortare2[2].get_suprafata() == 55);
    assert(locatari_sortare2[3].get_suprafata() == 100);

    service.add(6, "nume6", 67, "aaa");

    std::vector<Locatar> locatari_sortare3 = service.sortare_tip_suprafata();

    assert(locatari_sortare3.size() == 5);
    assert(locatari_sortare3[0].get_suprafata() == 67);
    assert(locatari_sortare3[1].get_suprafata() == 1);
    assert(locatari_sortare3[2].get_suprafata() == 44);
    assert(locatari_sortare3[3].get_suprafata() == 55);
    assert(locatari_sortare3[4].get_suprafata() == 100);

    std::map<std::string, int> raport = service.raport_tip_apartament();
    assert(raport["apartament"] == 4);
    assert(raport["aaa"] == 1);

    printf("Testele pentru clasa Service au trecut cu succes!\n");
}

void test_notification_features()
//Testeaza notificarile
{
    locatar_repo repo;
    locatar_service service(repo);

    service.add_notification(101);
    service.add_notification(102);
    service.add_notification(103);

    const auto& notifications = service.get_notifications();
    assert(notifications.size() == 3);

    service.add_notification(101);
    assert(notifications.size() == 3);

    service.clear_notifications();
    assert(notifications.empty());

    service.generate_notifications(100);
    auto notifications2 = service.get_notifications();
    assert(notifications.size() == 100);

    // Check for duplicates
    std::set<int> unique_notifications(notifications2.begin(), notifications2.end());
    assert(unique_notifications.size() == notifications2.size());

    service.clear_notifications();
    service.generate_notifications(1500);
    notifications2 = service.get_notifications();
    assert(notifications2.size() == 1000);

    unique_notifications = std::set<int>(notifications2.begin(), notifications2.end());
    assert(unique_notifications.size() == notifications2.size());

    cout << "Testele pentru notificari au trecut cu succes!\n";
}

void test_undo_operations() {
    // Testează operațiile de undo
    MapRepo repo{ 0.2F };
    locatar_service service(repo);

    // Adaugă un locatar
    service.add(1, "Popescu", 100, "apartament");
    assert(service.get_all().size() == 1);

    // Modifică un locatar
    service.modifica(1, "Popescu", 110, "studio");
    assert(service.get_all()[0].get_suprafata() == 110);
    assert(service.get_all()[0].get_tip_apartament() == "studio");

    // Aplică undo pentru modificare
    service.undo();
    assert(service.get_all()[0].get_suprafata() == 100);
    assert(service.get_all()[0].get_tip_apartament() == "apartament");

    // Șterge un locatar
    service.sterge(1, "Popescu");
    assert(service.get_all().empty());

    // Aplică undo pentru ștergere
    service.undo();
    assert(service.get_all().size() == 1);
    assert(service.get_all()[0].get_apartament() == 1);

    // Aplică undo pentru adăugare
    service.undo();
    assert(service.get_all().empty());
    // Verifică dacă un undo suplimentar aruncă o excepție
    try {
        service.undo();

    }
    catch (repo_exception& msg) {
        assert(msg.get_msg() == "Nu mai exista operatii la care sa executam undo!\n");
    }

    cout << "Testele pentru undo au trecut cu succes!\n";
}

#include <fstream>
#include <cassert>

void test_exportHTML() {
    locatar_repo repo;
    locatar_service service{ repo };
    // Add some data to service

    service.add(1, "nume1", 22, "apartament");
    service.add(2, "nume2", 33, "apartament");
    service.add(3, "nume3", 44, "apartament");
    service.exportHTML("test.html");

    // Check if the file exists
    ifstream file("test.html");
    assert(file.good());
}

void test_exportCSV() {
    locatar_repo repo;
    locatar_service service{ repo };
    // Add some data to service
    // ...
    service.add(1, "nume1", 22, "apartament");
    service.add(2, "nume2", 33, "apartament");
    service.add(3, "nume3", 44, "apartament");
    service.exportCSV("test.csv");

    // Check if the file exists
    ifstream file("test.csv");
    assert(file.good());
}

void test_exportFILE() {
    locatar_repo repo;
    locatar_service service{ repo };
    // Add some data to service
    // ...
    service.add(1, "nume1", 22, "apartament");
    service.add(2, "nume2", 33, "apartament");
    service.add(3, "nume3", 44, "apartament");
    service.exportFILE("test.txt");

    // Check if the file exists
    ifstream file("test.txt");
    assert(file.good());
}

void exporturi() {
    test_exportHTML();
    test_exportCSV();
    test_exportFILE();

    //return 0;
}


void ruleaza_toate_testele() {
    test_locatar();
    test_validator();
    test_repo();
    test_service();
    test_notification_features();
    test_undo_operations();
    exporturi();
    test_store2();
}