#include "masina.h"

#include <assert.h>
#include <string.h>

Masina creare_masina(char* nr_inmatr, char* model, char* categorie) {
	// se creaza si returneaza obiectul masina cu specificatiile
	// date prin parametrii
	Masina rez;
	strcpy_s(rez.nr_inmatr, sizeof(rez.nr_inmatr), nr_inmatr);
	strcpy_s(rez.model, sizeof(rez.model), model);
	strcpy_s(rez.categorie, sizeof(rez.categorie), categorie);
	rez.inchiriere = 1;
	return rez;
}

void distrugere_masina(Masina* rez) {
	// se dealoca din memorie obiectul masina dat ca parametru
	rez->nr_inmatr[0] = '\0';
	rez->model[0] = '\0';
	rez->categorie[0] = '\0';
	rez->inchiriere = 0;
}

int validare_masina(Masina rez) {
	// se valideaza specificatiile obiectului masina dat ca parametru
	if (strlen(rez.nr_inmatr) == 0)
		return 1;
	if (strlen(rez.model) == 0)
		return 2;
	if (strlen(rez.categorie) == 0)
		return 3;
	return 0;
}

void test_creare_distrugere_masina() {
	// se testeaza crearea obiectului masina
	Masina m = creare_masina("CJ34RAV", "Audi Q8", "suv");
	assert(strcmp(m.nr_inmatr, "CJ34RAV") == 0);
	assert(strcmp(m.model, "Audi Q8") == 0);
	assert(strcmp(m.categorie, "suv") == 0);
	assert(m.inchiriere == 1);

	// se testeaza distrugerea obiectului masina
	distrugere_masina(&m);
	assert(m.nr_inmatr[0] == '\0');
	assert(m.model[0] == '\0');
	assert(m.categorie[0] == '\0');
	assert(m.inchiriere == 0);
}
