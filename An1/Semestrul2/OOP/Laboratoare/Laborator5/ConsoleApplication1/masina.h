#pragma once

typedef struct {
	char nr_inmatr[20];
	char model[20];
	char categorie[20];
	int inchiriere;
} Masina;

Masina creare_masina(char* nr_inmatr, char* model, char* categorie);

void distrugere_masina(Masina* rez);

void test_creare_distrugere_masina();

int validare_masina(Masina);

