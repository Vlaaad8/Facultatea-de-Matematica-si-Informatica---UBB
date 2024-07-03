#include "MyList.h"
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <ctype.h>
#include "medicamente.h"

medicament creator(int cod, char* nume, int concentratie, int cantitate) {
	/* Vom crea o instanta de tip medicament */
	medicament m;
	m.cod = cod;
	m.concentratie = concentratie;
	m.cantitate = cantitate;
	strcpy_s(m.nume, sizeof(m.nume), nume);

	return m;
}
bool validareCod(medicament pastila) {
	/* vom valida datele de intrare*/

	bool corect = true;
	if (pastila.cantitate < 0) {
		corect = false;
	}
	if (pastila.concentratie < 0 || pastila.concentratie>99) {
		corect = false;

	}

	if (pastila.cod < 0) {
		corect = false;

	}
	if (corect == false) return false;
	return true;
}

void testareValidare() {
	medicament m1 = { 1,"Aspacardin",10,-10 };
	medicament m2 = { 2,"Brufen",200,100 };
	medicament m3 = { -3,"Antrax",50,300 };
	medicament m4 = { 3,"Antrax",50,300 };
	assert(validareCod(m1) == false);
	assert(validareCod(m2) == false);
	assert(validareCod(m3) == false);
	assert(validareCod(m4) == true);

}