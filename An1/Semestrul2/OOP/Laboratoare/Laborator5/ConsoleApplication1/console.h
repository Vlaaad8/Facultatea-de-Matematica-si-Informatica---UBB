#pragma once

#include "lista.h"

void UI_print_lista_masini(Lista* l);

void UI_adaugare_masina(Lista* l);

void UI_actualizare_masina(Lista* l);

void UI_inchiriere_masina(Lista* l);

void UI_returnare_masina(Lista* l);

void UI_filtrare_masina_categorie(Lista* l);

void UI_filtrare_masina_model(Lista* l);

void UI_sortare_lista_dupa_model(Lista* l);

void UI_sortare_lista_dupa_categorie(Lista* l);

void run();
