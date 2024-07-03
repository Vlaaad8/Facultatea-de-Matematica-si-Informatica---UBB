#pragma once

#include "lista.h"

int serv_adaugare_masina(Lista *l, char* nr_inmatr, char* model, char* categorie);

int serv_cautare_masina(Lista* l, char* nr_inmatr);

int serv_actualizare_masina(Lista* l, int poz, char* nr_inmatr, char* model, char* categorie);

int serv_inchiriere_masina(Lista* l, char* nr_inmatr);

int serv_returnare_masina(Lista* l, char* nr_inmatr);

int serv_filtrare_masini_categorie(Lista* l, Lista* l_filtrat, char* categorie);

int serv_filtrare_masini_model(Lista* l, Lista* l_filtrat, char* model);

int serv_sortare_lista_dupa_model(Lista* l, int mod);

int serv_sortare_lista_dupa_categorie(Lista* l, int mod);

int cmp_model_cresc(Masina* m1, Masina* m2);

int cmp_model_descresc(Masina* m1, Masina* m2);

int cmp_categorie_cresc(Masina* m1, Masina* m2);

int cmp_categorie_descresc(Masina* m1, Masina* m2);

void test_serv_adaugare_masina();

void test_serv_actualizare_masina();

void test_serv_cautare_masina();

void test_serv_inchiriere_returnare_masina();

void test_serv_filtrare_lista_masini();

void test_serv_sortare_lista();