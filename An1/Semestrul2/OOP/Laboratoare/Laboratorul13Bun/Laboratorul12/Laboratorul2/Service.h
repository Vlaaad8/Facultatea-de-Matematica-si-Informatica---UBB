#pragma once
#include <string>
#include "Repository.h"
#include <map>
#include "undo.h"
#include <memory>
#include "observer.h"
using std::string;

class Service : public Observable
{
private:
    AbstractRepo& repo;
    std::vector<Locatar> notification_list;
    vector<unique_ptr<ActiuneUndo>> undoStack;
public:
    explicit Service(AbstractRepo& repo) :repo{ repo } {

    }
    //locatar_service(const locatar_service& service) = delete;
    //locatar_service() = default;
    void add(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament);

    std::vector<Locatar> get_all() const;

    void sterge(int apartament, const string& nume_proprietar);

    void modifica(int apartament, const string& nume_proprietar, int suprafata, const string& tip_apartament);

    Locatar cauta(int apartament);

    std::vector<Locatar> filtrare_tip(const string& tip_apartament);

    std::vector<Locatar> filtrare_suprafata(int suprafata);

    std::vector<Locatar> sortare_nume_proprietar();

    std::vector<Locatar> sortare_suprafata();

    std::vector<Locatar> sortare_tip_suprafata();

    void add_notification(int apartment, string& nume, int supr, string& tip);

    void clear_notifications();

    [[nodiscard]] const std::vector<Locatar>& get_notifications() const;

    void generate_notifications();

    std::map<std::string, int> raport_tip_apartament();

    void undo();

    void exportHTML(const string& fileName);
    void exportCSV(const string& fileName);
    void exportFILE(const string& fileName);
    void importFILE(const string& fileName);
    size_t sizeNotifications();

};
