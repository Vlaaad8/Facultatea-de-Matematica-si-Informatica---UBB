#include "car_rent_service.h"
#include "mysort.h"

#include <assert.h>
#include <string.h>
#include <stdlib.h>

int serv_adaugare_masina(Lista *l, char* nr_inmatr, char* model, char* categorie) {
	// realizeaza operatiunea de adaugare a unei masini 
	// in memorie
	Masina m = creare_masina(nr_inmatr, model, categorie);
	int val = validare_masina(m);
	if (val == 0) {
		// validarea s-a efectut cu succes
		int poz = cautare_masina(l, nr_inmatr);
		if (poz == -1)
			adaugare_masina(l, m);
		else return -2;
	}
	return val;
}

int serv_cautare_masina(Lista* l, char* nr_inmatr) {
	// realizeaza operatiunea de cautare a unei masini
	// in memorie dupa numarul de inmatriculare
	int poz = cautare_masina(l, nr_inmatr);
	return poz;
}	

int serv_actualizare_masina(Lista* l, int poz, char* nr_inmatr, char* model, char* categorie){
	// realizeaza operatiunea de actualizare a unei masini
	// in memorie
	Masina m = creare_masina(nr_inmatr, model, categorie);
	int val = validare_masina(m);
	if (val == 0) {
		// validarea s-a efectut cu succes
		actualizare_masina(l, poz, m);
	}
	return val;
}

int serv_inchiriere_masina(Lista* l, char* nr_inmatr) {
	// realizeaza operatiunea de inchiriere masina
	int poz = cautare_masina(l, nr_inmatr);
	if (poz != -1) {
		// masina exista in memorie
		int inchiriere = get_info_inchiriere(l, poz);
		if (inchiriere == 1) {
			inchiriere_masina(l, poz);
		}
		else return -1;
	}
	else return -2;
	return 0;
}

int serv_returnare_masina(Lista* l, char* nr_inmatr) {
	// realizeaza operatiunea de returnare masina
	int poz = cautare_masina(l, nr_inmatr);
	if (poz != -1) {
		// masina exista in memorie
		int inchiriere = get_info_inchiriere(l, poz);
		if (inchiriere == 0) {
			returnare_masina(l, poz);
		}
		else return -1;
	}
	else return -2;
	return 0;
}

int serv_filtrare_masini_categorie(Lista* l,Lista* l_filtrat, char* categorie) {
	// realizeaza operatiunea de filtrare lista dupa categorie
	for (int i = 0; i < lungime(l); i++)
		if (strcmp(l->lista_masini[i].categorie, categorie) == 0)
			adaugare_masina(l_filtrat, get_masina(l,i));
	return (lungime(l_filtrat) == 0);
}

int serv_filtrare_masini_model(Lista* l, Lista* l_filtrat, char* model) {
	// realizeaza operatiunea de filtrare lista dupa model
	for (int i = 0; i < lungime(l); i++)
		if (strcmp(l->lista_masini[i].model, model) == 0)
			adaugare_masina(l_filtrat, get_masina(l, i));
	return (lungime(l_filtrat) == 0);
}

int cmp_model_cresc(Masina* m1, Masina* m2) {
	return strcmp(m1->model, m2->model);
}

int cmp_model_descresc(Masina* m1, Masina* m2) {
	return -(strcmp(m1->model, m2->model));
}

int cmp_categorie_cresc(Masina* m1, Masina* m2) {
	return strcmp(m1->categorie, m2->categorie);
}

int cmp_categorie_descresc(Masina* m1, Masina* m2) {
	return -(strcmp(m1->categorie, m2->categorie));
}
int cmp_numar_inmatriculare(Masina* m1, Masina* m2) {
	return strcmp(m1->nr_inmatr, m2->nr_inmatr);
}

int serv_sortare_lista_dupa_model(Lista* l,int mod) {
	// realizeaza sortarea listei de masini dupa model
	
	if (mod == 1) {
		sort(l, cmp_model_cresc);
		return 0;
	}
	if (mod == 2) {
		sort(l, cmp_model_descresc);
		return 0;
	}
	return 1;
}

int serv_sortare_lista_dupa_categorie(Lista* l, int mod) {
	// realizeaza soartarea listei de masini dupa categorie
	if (mod == 1) {
		sort(l, cmp_categorie_cresc);
		return 0;
	}
	if (mod == 2) {
		sort(l, cmp_categorie_descresc);
		return 0;
	}
	return 1;
}

void test_serv_adaugare_masina() {
	// se testeaza functionalitatea de adaugare 
	// a unei masini
	Lista* l = creare_lista();
	int val1 = serv_adaugare_masina(l, "CJ34RAV", "Audi Q8", "suv");
	assert(val1 == 0);
	assert(strcmp(l->lista_masini[0].nr_inmatr, "CJ34RAV") == 0);
	val1 = serv_adaugare_masina(l, "", "Audi Q8", "suv");
	assert(val1 == 1);
	assert(lungime(l) == 1);
	val1 = serv_adaugare_masina(l, "CJ34RAV", "", "suv");
	assert(val1 == 2);
	assert(lungime(l) == 1);
	val1 = serv_adaugare_masina(l, "CJ34RAV", "Audi Q8", "");
	assert(val1 == 3);
	assert(lungime(l) == 1);
	val1 = serv_adaugare_masina(l, "CJ34RAV", "Dacia Logan", "mini");
	assert(val1 == -2);
	assert(lungime(l) == 1);
	dealocare_lista(l);
}

void test_serv_actualizare_masina() {
	// se testeaza functionalitatea de actualizare
	// a unei masini
	Lista* l = creare_lista();
	int val1 = serv_adaugare_masina(l, "CJ34RAV", "Audi Q8", "suv");
	assert(val1 == 0);
	val1 = serv_actualizare_masina(l, 0, "CJ36RAV", "Audi Q8", "suv");
	assert(val1 == 0);
	assert(strcmp(l->lista_masini[0].nr_inmatr, "CJ36RAV") == 0);
	dealocare_lista(l);
}

void test_serv_cautare_masina() {
	// se testeaza functionalitatea de cautare
	// a unei masini
	Lista* l = creare_lista();
	int val1 = serv_adaugare_masina(l, "CJ12RAV", "BMW M8", "sport");
	assert(val1 == 0);
	int poz = serv_cautare_masina(l, "CJ13RAV");
	assert(poz == -1);
	poz = serv_cautare_masina(l, "CJ12RAV");
	assert(poz == 0);
	dealocare_lista(l);
}

void test_serv_inchiriere_returnare_masina() {
	
	// test inchiriere masina
	Lista* l = creare_lista();
	int val1 = serv_adaugare_masina(l, "CJ12RAV", "BMW M8", "sport");
	int inchiriere = get_info_inchiriere(l, 0);
	assert(inchiriere == 1);
	val1 = serv_inchiriere_masina(l, "CJ12RAV");
	assert( val1 == 0);
	val1 = serv_inchiriere_masina(l, "CJ12RAV");
	assert(val1 == -1);
	val1 = serv_inchiriere_masina(l, "CJ13RAV");
	assert(val1 == -2);

	// test returnare masina
	val1 = serv_returnare_masina(l, "CJ12RAV");
	assert(val1 == 0);
	val1 = serv_returnare_masina(l, "CJ12RAV");
	assert(val1 == -1);
	val1 = serv_returnare_masina(l, "CJ13RAV");
	assert(val1 == -2);
	dealocare_lista(l);
}

void test_serv_filtrare_lista_masini() {
	// test filtrare lista dupa categorie
	Lista* l = creare_lista();
	Lista* l_filtrat = creare_lista();
	int val1 = serv_adaugare_masina(l, "CJ12RAV", "BMW M8", "sport");
	val1 = serv_adaugare_masina(l, "CJ34RAV", "Audi Q8", "suv");
	int error = serv_filtrare_masini_categorie(l, l_filtrat, "sport");
	assert(error == 0);
	assert(lungime(l_filtrat) == 1);
	assert(strcmp(l_filtrat->lista_masini[0].nr_inmatr, "CJ12RAV") == 0);
	distrugere_lista(l_filtrat);
	error = serv_filtrare_masini_categorie(l, l_filtrat, "mini");
	assert(error == 1);

	// test filtrare lista dupa model
	error = serv_filtrare_masini_model(l, l_filtrat, "BMW M8");
	assert(error == 0);
	assert(lungime(l_filtrat) == 1);
	assert(strcmp(l_filtrat->lista_masini[0].nr_inmatr, "CJ12RAV") == 0);
	distrugere_lista(l_filtrat);
	error = serv_filtrare_masini_model(l, l_filtrat, "DACIA LOGAN");
	assert(error == 1);
	dealocare_lista(l_filtrat);
	dealocare_lista(l);
}

void test_serv_sortare_lista() {
	// test sortare lista dupa model;
	Lista* l = creare_lista();
	adaugare_masina(l, creare_masina("BC09RAV", "TRABANT", "MINI"));
	adaugare_masina(l, creare_masina("BC07RAV", "DACIA", "BUS"));
	adaugare_masina(l, creare_masina("BC03RAV", "AUDI", "SPORT"));
	int error = serv_sortare_lista_dupa_model(l, 1);
	assert(error == 0);
	assert(strcmp(l->lista_masini[0].nr_inmatr, "BC03RAV") == 0);
	assert(strcmp(l->lista_masini[1].nr_inmatr, "BC07RAV") == 0);
	assert(strcmp(l->lista_masini[2].nr_inmatr, "BC09RAV") == 0);

	// test sortare lista descrescator dupa model
	error = serv_sortare_lista_dupa_model(l, 2);
	assert(error == 0);
	assert(strcmp(l->lista_masini[0].nr_inmatr, "BC09RAV") == 0);
	assert(strcmp(l->lista_masini[1].nr_inmatr, "BC07RAV") == 0);
	assert(strcmp(l->lista_masini[2].nr_inmatr, "BC03RAV") == 0);

	error = serv_sortare_lista_dupa_model(l, 3);
	assert(error == 1);

	// test sortare lista crescator dupa categorie
	error = serv_sortare_lista_dupa_categorie(l, 1);
	assert(error == 0);
	assert(strcmp(l->lista_masini[0].nr_inmatr, "BC07RAV") == 0);
	assert(strcmp(l->lista_masini[1].nr_inmatr, "BC09RAV") == 0);
	assert(strcmp(l->lista_masini[2].nr_inmatr, "BC03RAV") == 0);

	// test soartare lista descrescator dupa categorie
	error = serv_sortare_lista_dupa_categorie(l, 2);
	assert(error == 0);
	assert(strcmp(l->lista_masini[0].nr_inmatr, "BC03RAV") == 0);
	assert(strcmp(l->lista_masini[1].nr_inmatr, "BC09RAV") == 0);
	assert(strcmp(l->lista_masini[2].nr_inmatr, "BC07RAV") == 0);

	error = serv_sortare_lista_dupa_categorie(l, 3);
	assert(error == 1);

	dealocare_lista(l);
}