#include "ui.h"
#include "validator.h"
#include "exception.h"
#include <iostream>
#include <limits>
#include <vector>
#include <fstream>
#include "locatar_service.h"

using std::cin;
using std::cout;



void afiseaza(const std::vector<Locatar>& locatari) {
    //Afiseaza locatarii
    if (locatari.empty()) {
        cout << "Nu exista locatari!\n";
        return;
    }
    for (const Locatar& locatar : locatari) {
        cout << locatar.get_apartament() << " " << locatar.get_nume_proprietar() << " " << locatar.get_suprafata() << " " << locatar.get_tip_apartament() << "\n";
    }
}



void UI::run() {
    //Ruleaza aplicatia
    while (true) {
        cout << "1. Adauga locatar\n2. Sterge locatar\n3. Modifica locatar\n4. Cauta apartament\n5. Filtrare apartamente\n6. Sortare apartamente\n7. Afiseaza locatari\n8. Raport\n9. Undo\n0. Exit\nIntrodu comanda: ";
        int cmd = 0;
        cin >> cmd;
        cin.clear();
        cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        if (cmd == 0) {
            //delete[] this;
            break;
        }
        if (cmd == 1) {
            int apartament;
            string nume_proprietar;
            int suprafata;
            string tip_apartament;
            cout << "Introdu apartament: ";
            cin >> apartament;
            cout << "Introdu nume proprietar: ";
            cin >> nume_proprietar;
            cout << "Introdu suprafata: ";
            cin >> suprafata;
            cout << "Introdu tip apartament: ";
            cin >> tip_apartament;
            try {
                service.add(apartament, nume_proprietar, suprafata, tip_apartament);
                cout << "Locatar adaugat cu succes!\n";
				service.exportFILE("locatari.txt");
            }
            catch (repo_exception& ex) {
                cout << ex.get_msg() << "\n";
            }
            catch (validator_exception& ex) {
                cout << ex.get_msg() << "\n";
            }

        }
        if (cmd == 2) {
            int apartament;
            string nume_proprietar;
            cout << "Introdu apartament: ";
            cin >> apartament;
            cout << "Introdu nume proprietar: ";
            cin >> nume_proprietar;
            try {
                service.sterge(apartament, nume_proprietar);
                cout << "Locatar sters cu succes!\n";
                service.exportFILE("locatari.txt");
            }
            catch (repo_exception& ex) {
                cout << ex.get_msg() << "\n";

            }

        }
        if (cmd == 3) {
            int apartament;
            string nume_proprietar;
            int suprafata;
            string tip_apartament;
            cout << "Introdu apartament: ";
            cin >> apartament;
            cout << "Introdu nume proprietar: ";
            cin >> nume_proprietar;
            cout << "Introdu suprafata: ";
            cin >> suprafata;
            cout << "Introdu tip apartament: ";
            cin >> tip_apartament;
            try {
                service.modifica(apartament, nume_proprietar, suprafata, tip_apartament);
                cout << "Locatar modificat cu succes!\n";
                service.exportFILE("locatari.txt");
            }
            catch (repo_exception& ex) {
                cout << ex.get_msg() << "\n";
            }
            catch (validator_exception& ex) {
                cout << ex.get_msg() << "\n";
            }

        }
        if (cmd == 4) {
            int apartament;
            cout << "Introdu apartament: ";
            cin >> apartament;
            try {
                Locatar locatar = service.cauta(apartament);
                cout << "Locatar gasit: " << locatar.get_nume_proprietar() << std::endl;
            }
            catch (repo_exception& ex) {
                cout << ex.get_msg() << "\n";
            }

        }
        if (cmd == 5) {
            int optiune;
            cout << "1. Filtrare dupa tip\n2. Filtrare dupa suprafata\nIntrodu optiune: ";
            cin >> optiune;
            if (optiune == 1) {
                string tip_apartament;
                cout << "Introdu tip apartament: ";
                cin >> tip_apartament;
                std::vector<Locatar> locatari = service.filtrare_tip(tip_apartament);
                afiseaza(locatari);
            }
            else if (optiune == 2) {
                int suprafata;
                cout << "Introdu suprafata: ";
                cin >> suprafata;
                std::vector<Locatar> locatari = service.filtrare_suprafata(suprafata);
                afiseaza(locatari);
            }
            else {
                cout << "Optiune invalida!\n";
            }
        }
        if (cmd == 6) {
            int optiune;
            cout << "1. Sortare dupa nume proprietar\n2. Sortare dupa suprafata\n3. Sortare dupa tip apartament si suprafata\nIntrodu optiunea: ";
            cin >> optiune;
            if (optiune == 1) {
                std::vector<Locatar> locatari = service.sortare_nume_proprietar();
                afiseaza(locatari);
            }
            else if (optiune == 2) {
                std::vector<Locatar> locatari = service.sortare_suprafata();
                afiseaza(locatari);
            }
            else if (optiune == 3) {
                std::vector<Locatar> locatari = service.sortare_tip_suprafata();
                afiseaza(locatari);
            }
            else {
                cout << "Optiune invalida!\n";
            }
        }
        if (cmd == 7) {
            std::vector<Locatar> locatari = service.get_all();
            afiseaza(locatari);
        }
        if (cmd == 8) {
            std::map<std::string, int> raport = service.raport_tip_apartament();
            for (const auto& pair : raport) {
                const auto& tip = pair.first;
                const auto& count = pair.second;
                cout << tip << " " << count << "\n";
            }

        }
        if (cmd == 9) {
            try {
                service.undo();
                service.exportFILE("locatari.txt");
                cout << "Ultima actiune a fost anulata cu succes!\n";
            }
            catch (const std::exception& e) {
                cout << "Nu se poate face undo: " << e.what() << "\n";
            }
        }
    }
}

void UI::afiseaza_nr_notificari() {
    const auto& notifications = service.get_notifications();
    cout << "Numarul de apartamente din lista de notificari: " << notifications.size() << "\n";
}

void UI::export_notifications() {
    if (service.get_notifications().empty()) {
        cout << "Lista de notificari este goala!\n";
        return;
    }

    cout << "Introdu numele fisierului: ";
    string filename;
    cin >> filename;

    cout << "Selecteaza formatul fisierului (1 pentru CSV, 2 pentru HTML): ";
    int format;
    cin >> format;

    if (format == 1) {
		service.exportCSV(filename);
    }
    else if (format == 2) {
		service.exportHTML(filename);
    }
    else {
        cout << "Format invalid!\n";
    }
}


[[noreturn]] void UI::manage_notifications() {
    bool gasit = true;
    while (gasit) {
        cout << "\n1. Adauga notificare\n2. Afiseaza notificari\n3. Sterge notificari\n4. Genereaza apartamente random\n5.Exporta in fisier\n6.Mergi la meniul normal\n7.Exit\nAlegeti o optiune: ";
        int option;
        cin >> option;
        switch (option) {
        case 1:
            cout << "Introdu numarul apartamentului: ";
            int apartment;
            cin >> apartment;
            service.add_notification(apartment);
            cout << "Apartment adaugat la lista de notificari\n";
            break;
        case 2:
            cout << "Lista de notificari: \n";
            for (int apt : service.get_notifications()) {
                cout << "Apartment " << apt << "\n";
            }
            break;
        case 3:
            service.clear_notifications();
            cout << "Lista de notificari a fost stearsa.\n";
            break;
        case 4:
            cout << "Introdu numarul apartamentelor de generat:  ";
            int count;
            cin >> count;
            service.generate_notifications(count);
            cout << "Apartmentele au fost generate.\n";
            break;
        case 5:
            export_notifications();
            break;
        case 6:
            run();
            cout<<"\nRevenim la meniul de notificari\n";
            break;
        case 7:
            gasit = false;
            break;
            
        default:
            cout << "Optiune invalida!\n";
            break;
        }
        afiseaza_nr_notificari();
    }
}

void UI::meniu() {
    int optiune;
    printf("1. Program normal\n2. Lista notificari\n");
    cout << "Introduceti o optiune:";
    std::cin >> optiune;
    if (optiune == 1)
    {run();
     cout << "Intram in meniul normal";
    }
    else if (optiune == 2)
    {manage_notifications();

    }
    else
        printf("Optiune invalida!\n");
}