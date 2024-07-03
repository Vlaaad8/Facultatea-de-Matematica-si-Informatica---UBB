#pragma once
#include "locatar_service.h"
class UI
{
    locatar_service& service;
public:
    explicit UI(locatar_service& service) :service{ service } {

    }
    void run();

    [[noreturn]] void manage_notifications();
    void afiseaza_nr_notificari();
    void export_to_csv(const string& filename);
    void export_to_html(const string& filename);
    void export_notifications();
    void meniu();
};