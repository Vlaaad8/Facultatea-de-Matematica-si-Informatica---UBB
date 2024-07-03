#include "MyList.h"
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <ctype.h>
#include "medicamente.h"
#include "medicamenteService.h"
#include <stdlib.h>

MyList emptylist() {
	MyList list;
	list.lenght = 0;
	list.capacitate = 5;
	list.lista = malloc(list.capacitate * sizeof(medicament));
	return list;
	
	}

void destroyList(MyList* l) {
	for (int i = 0; i < l->lenght; i++)
		deleteRepo(l, l->lista[i]);
	l->capacitate = 0;
	l->lenght = 0;
	free(l->lista);
}
void adaugareRepo(MyList* list, medicament m) {
if (list->lenght >= list->capacitate) {
    int newCap = list->capacitate * 2;
    medicament* aux_elems = malloc(sizeof(medicament) * newCap);
    if (aux_elems == NULL) {
        // Tratează eroarea de alocare a memoriei dacă este cazul
        return; // sau utilizează o altă strategie
    }
    for (int i = 0; i < list->lenght; i++) {
        aux_elems[i] = list->lista[i];
    }
    free(list->lista);
    list->lista = aux_elems;
    list->capacitate = newCap;
}
list->lista[list->lenght] = m;
list->lenght++; // corectat la "length"


}

void stergereRepo(MyList* lista, medicament pastila) {
	/* sterge stocul de la un anumit mesaj*/
	for (int i = 0; i < lista->lenght; i++) {
		if (strcmp(lista->lista[i].nume, pastila.nume) == 0) {
			lista->lista[i].cantitate = 0;
			break;
		}
	}
}

void updateRepo(MyList* list, medicament pastila,char* numeNou) {
	/*cautam daca medicamentul se afla in baza de date si apoi modificam concentratia daca il gasim*/
	for(int i=0;i<list->lenght;i++)
		if (strcmp(list->lista[i].nume, pastila.nume) == 0) {
			strcpy_s(list->lista[i].nume,sizeof(list->lista[i].nume), numeNou);
			list->lista[i].concentratie = pastila.concentratie;
			break;
		}
}


int verificaExistenta(MyList* l, medicament meds) {
	/*verificam in vector */
	for (int i = 0; i < l->lenght; i++) {
		if (strcmp(l->lista[i].nume, meds.nume) == 0)
			return i;
	}
		return -1;

	}
void deleteRepo(MyList* l, medicament pastila) {
	int index = -1;

	// Find the index of the medicament with the given name
	for (int i = 0; i < l->lenght; i++) {
		if (strcmp(l->lista[i].nume, pastila.nume) == 0) {
			index = i;
			break;
		}
	}

	if (index != -1) {
		// Shift elements to the left to overwrite the element to be deleted
		for (int j = index; j < l->lenght - 1; j++) {
			l->lista[j] = l->lista[j + 1];
		}

		// Decrement the length of the list
		l->lenght--;
	}
}


void test_emptylist() {
	// Test  empty list
	MyList list = emptylist();
	assert(list.lenght == 0); 
	destroyList(&list);
}

void testareExistenta() {
	MyList sursa = emptylist();
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };
	medicament m3 = { 3,"Antrax",50,300 };
	adaugareRepo(&sursa, m1);
	adaugareRepo(&sursa, m2);
	assert(verificaExistenta(&sursa, m1) == 0);
	assert(verificaExistenta(&sursa, m3) == -1);
	destroyList(&sursa);
}
void testAddRepo(){
	MyList sursa = emptylist();
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };
	medicament m3 = { 2,"Brufen",20,100 };
	medicament m4 = { 2,"Brufen",20,100 };
	medicament m5 = { 2,"Brufen",20,100 };
	medicament m6 = { 2,"Brufen",20,100 };
	adaugareRepo(&sursa, m1);
	adaugareRepo(&sursa, m3);
	adaugareRepo(&sursa, m2);
	adaugareRepo(&sursa, m4);
	adaugareRepo(&sursa, m5);
	adaugareRepo(&sursa, m6);
	assert(sursa.lenght == 6);
	destroyList(&sursa);
}