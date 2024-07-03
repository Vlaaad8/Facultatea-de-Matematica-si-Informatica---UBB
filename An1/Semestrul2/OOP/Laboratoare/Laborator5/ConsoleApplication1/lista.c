#include "lista.h"

#include <assert.h>
#include <string.h>
#include <stdlib.h>

Lista* creare_lista() {
	// creaza o lista vida de masini
	Lista *l = (Lista*)malloc(sizeof(Lista));
	if (l != NULL) {
		l->lista_masini = (TElem*)malloc(10 * sizeof(TElem));
		l->capacitate = 10;
		l->nr_masini = 0;

	}
	return l;
}

void distrugere_lista(Lista* l) {
	// se distruge lista de masini
	l->nr_masini = 0;
}

void dealocare_lista(Lista* l) {
	// se dealoca din memorie lista de masini
	free(l->lista_masini);
	free(l);
}

void redimensionare_lista(Lista* l) {
	// se redimensioneaza lista de masini
	int capacitate_noua = 2 * l->capacitate;
	TElem* lista_noua = (TElem*)malloc(capacitate_noua * sizeof(TElem));
	for (int i = 0; i < l->nr_masini; i++)
		lista_noua[i] = l->lista_masini[i];
	free(l->lista_masini);
	l->lista_masini = lista_noua;
	l->capacitate = capacitate_noua;
}

void adaugare_masina(Lista* l, TElem masina) {
	// se adauga la lista o masina noua data prin parametru
	if (l->capacitate == l->nr_masini)
		redimensionare_lista(l);
	l->lista_masini[l->nr_masini] = masina;
	l->nr_masini++;
}

int lungime(Lista* l) {
	// se returneaza lungimea listei de masini
	return l->nr_masini;
}

TElem get_masina(Lista* l, int poz) {
	// se returneaza masina din lista de masini
	// de pe o pozitie data prin parametru
	return l->lista_masini[poz];
}

int cautare_masina(Lista* l, char* nr_inmatr) {
	// se returneaza pozitia din lista masina 
	// care are numarul de inmatriculare indicat
	// altfel returneaza -1
	for (int i = 0; i < lungime(l); i++) {
		if (strcmp(l->lista_masini[i].nr_inmatr, nr_inmatr) == 0)
			return i;
	}
	return -1;
}

void actualizare_masina(Lista* l, int poz, TElem masina) {
	// se actualizeaza detaliile masinii
	l->lista_masini[poz] = masina;
}

int get_info_inchiriere(Lista* l, int poz) {
	// se returneaza 1 daca masina este disponibila
	// de inchiriat sau 0 daca este deja inchiriata
	return l->lista_masini[poz].inchiriere;
}

void inchiriere_masina(Lista* l, int poz) {
	// se inchiriaza masina de pe pozitia poz
	// din lista de masini
	l->lista_masini[poz].inchiriere = 0;
}

void returnare_masina(Lista* l, int poz) {
	// se returneaza masina de pe pozitia poz
	// din lista de masini
	l->lista_masini[poz].inchiriere = 1;
}

void test_creare_lista() {
	// se testeaza functionalitatea de creare 
	// a listei de masini
	Lista* l = creare_lista();
	assert(l->nr_masini == 0);
	dealocare_lista(l);
}

void test_gestionare_lista() {
	// se testeaza functionalitatile necesare pentru 
	// gestionarea listei de masini
	Lista* l = creare_lista();

	// test lungime lista
	assert(lungime(l) == 0);

	// test aduagare masina
	Masina m = creare_masina("CJ34RAV", "Audi Q8", "suv");
	adaugare_masina(l, m);
	assert(lungime(l) == 1);
	Masina m1 = creare_masina("CJ12RAV", "BMW M8", "sport");
	adaugare_masina(l, m1);

	// test get masina
	assert(lungime(l) == 2);
	Masina m2 = get_masina(l, 1);
	assert(strcmp(m2.nr_inmatr, "CJ12RAV") == 0);
	
	// test cautare masina dupa nr_inmatriculare
	int poz = cautare_masina(l, "CJ13RAV");
	assert(poz == -1);
	poz = cautare_masina(l, "CJ12RAV");
	assert(poz == 1);

	// test actualizare masina existenta
	Masina m3 = creare_masina("CJ13RAV", "BMW M8", "sport");
	actualizare_masina(l, 1, m3);
	m2 = get_masina(l, 1);
	assert(strcmp(m2.nr_inmatr, "CJ13RAV") == 0);

	// test inchiriere masina
	int inchiriere = get_info_inchiriere(l, 0);
	assert(inchiriere == 1);
	inchiriere_masina(l, 0);
	inchiriere = get_info_inchiriere(l, 0);
	assert(inchiriere == 0);

	// test returnare masina
	returnare_masina(l, 0);
	inchiriere = get_info_inchiriere(l, 0);
	assert(inchiriere == 1);
	
	// test redimensionare lista
	adaugare_masina(l, creare_masina("BC03RAV", "AUDI", "MINI"));
	adaugare_masina(l, creare_masina("BC04RAV", "BMW", "MINI"));
	adaugare_masina(l, creare_masina("BC05RAV", "MAZDA", "MINI"));
	adaugare_masina(l, creare_masina("BC06RAV", "BMW", "MINI"));
	adaugare_masina(l, creare_masina("BC07RAV", "DACIA", "MINI"));
	adaugare_masina(l, creare_masina("BC08RAV", "PORSCHE", "MINI"));
	adaugare_masina(l, creare_masina("BC09RAV", "TRABANT", "MINI"));
	adaugare_masina(l, creare_masina("BC10RAV", "DACIA", "MINI"));
	adaugare_masina(l, creare_masina("BC11RAV", "AUDI", "MINI"));
	
	Masina m4 = get_masina(l, 10);
	assert(strcmp(m4.nr_inmatr, "BC11RAV") == 0);
	assert(strcmp(m4.model, "AUDI") == 0);
	assert(strcmp(m4.categorie, "MINI") == 0);
	assert(m4.inchiriere == 1);

	// test dealocare lista
	dealocare_lista(l);
}