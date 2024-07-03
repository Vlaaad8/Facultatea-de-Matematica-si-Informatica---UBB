#pragma once

#include "masina.h"

typedef Masina TElem;

typedef struct {
	TElem* lista_masini;
	int nr_masini;
	int capacitate;
} Lista;

Lista* creare_lista();

void distrugere_lista(Lista* l);

void dealocare_lista(Lista* l);

void redimensionare_lista(Lista* l);

int lungime(Lista* l);

TElem get_masina(Lista* l, int poz);

void adaugare_masina(Lista* l, TElem masina);

int cautare_masina(Lista* l, char* nr_inmatr);

void actualizare_masina(Lista* l, int poz, TElem masina);

void test_creare_lista();

void test_gestionare_lista();

int get_info_inchiriere(Lista* l, int poz);

void inchiriere_masina(Lista* l, int poz);

void returnare_masina(Lista* l, int poz);