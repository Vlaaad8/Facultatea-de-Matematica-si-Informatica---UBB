#define _CTRDBG_MAP_ALLOC
#include <stdio.h>
#include <crtdbg.h>

#include "console.h"
#include "car_rent_service.h"


void test_all() {
	// se apeleaza functiile de testare a tututor
	// functionalitatilor aplicatiei
	test_creare_distrugere_masina();
	test_creare_lista();
	test_gestionare_lista();
	test_serv_adaugare_masina();
	test_serv_actualizare_masina();
	test_serv_cautare_masina();
	test_serv_inchiriere_returnare_masina();
	test_serv_filtrare_lista_masini();
	test_serv_sortare_lista();
}

int main() {
	// pornire aplicatie
	test_all();
	//run();
	_CrtDumpMemoryLeaks();
	return 0;
}