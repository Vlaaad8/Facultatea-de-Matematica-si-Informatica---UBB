#include "MyList.h"
#include "medicamente.h"
#include "customSort.h"


void adaugareService(MyList* l, int cod, char* nume, int concentratie, int cantitate) {
	medicament pastila = creator(cod, nume, concentratie, cantitate);
	adaugareRepo(l, pastila);
}

void stergereService(MyList* l, char* nume) {
	medicament pastila = creator(1, nume, 1, 1);
	stergereRepo(l, pastila);
}

void updateService(MyList* l, char* nume, char* numeNou, int concentratie){ 
	medicament pastila = creator(0, nume, concentratie, 0);
	updateRepo(l, pastila, numeNou);
}

void deleteService(MyList* l, char* nume) {
	medicament pastila = creator(0, nume, 1, 0);
	deleteRepo(l, pastila);

}
void schimbMedicamente(medicament* pastila1, medicament* pastila2) {
	int clonaConcentratie = pastila1->concentratie;
	int clonaCantitate = pastila1->cantitate;
	int clonaCod = pastila1->cod;
	char clonaNume[30];
	strcpy_s(clonaNume, sizeof(pastila1->nume), pastila1->nume);
	pastila1->concentratie = pastila2->concentratie;
	pastila1->cod = pastila2->cod;
	pastila1->cantitate = pastila2->cantitate;
	strcpy_s(pastila1->nume, sizeof(pastila2->nume), pastila2->nume);
	pastila2->cod = clonaCod;
	pastila2->cantitate = clonaCantitate;
	pastila2->concentratie = clonaConcentratie;
	strcpy_s(pastila2->nume, sizeof(clonaNume), clonaNume);

}

void filtrareCrescDesc(MyList* l, char* tip) {
	if (strcmp(tip, "crescator") == 0) {
		for (int i = 0; i < l->lenght - 1; i++) {
			for (int j = i + 1; j < l->lenght; j++)
				if (strcmp(l->lista[i].nume, l->lista[j].nume) > 0)
					schimbMedicamente(&(l->lista[i]), &(l->lista[j]));
				else if (strcmp(l->lista[i].nume, l->lista[j].nume) == 0) {
					if (l->lista[i].cantitate > l->lista[j].cantitate)
						schimbMedicamente(&(l->lista[i]), &(l->lista[j]));


				}
		}
	}
	else if (strcmp(tip, "descrescator") == 0) {
		for (int i = 0; i < l->lenght - 1; i++) {
			for (int j = i + 1; j < l->lenght; j++)
				if (strcmp(l->lista[i].nume, l->lista[j].nume) < 0)
					schimbMedicamente(&(l->lista[i]), &(l->lista[j]));
				else if (strcmp(l->lista[i].nume, l->lista[j].nume) == 0) {
					if (l->lista[i].cantitate < l->lista[j].cantitate)
						schimbMedicamente(&(l->lista[i]), &(l->lista[j]));
				}
		}
	}
	}
void filtrareCantitate(MyList* l,MyList* copie, int number) {
		for (int i = 0; i < l->lenght; i++) {
				if (l->lista[i].cantitate < number)
				adaugareRepo(copie, l->lista[i]);

		}
	
}
void filtrareLitera(MyList* l, MyList* copie, char caracter) {
	for (int i = 0; i < l->lenght; i++) {
		if (l->lista[i].nume[0] == caracter)
			adaugareRepo(copie, l->lista[i]);
	}

}
void testSchimbare() {
	medicament m1 = { 1, "Aspacardin", 100, 20 };
	medicament m2 = { 2, "Aspacardin2", 1000, 200 };
	schimbMedicamente(&m1, &m2);
	assert(m1.cod == 2);
	assert(m1.cantitate == 200);
	assert(m1.concentratie == 1000);
	assert(strcmp(m1.nume,"Aspacardin2")==0);
	assert(m2.cod == 1);
	assert(m2.cantitate == 20);
	assert(m2.concentratie == 100);
	assert(strcmp(m2.nume,"Aspacardin")==0);
}

void testFiltrare() {
	MyList lista = emptylist();
	adaugareService(&lista,10, "Nurofen", 10, 20);
	adaugareService(&lista, 10, "Aspacardin", 100, 2000);
	adaugareService(&lista, 20, "Aspacardin", 10, 30);
	filtrareCrescDesc(&lista,"crescator");
	assert(strcmp(lista.lista[0].nume, "Aspacardin") == 0);
	assert(lista.lista[0].cantitate == 30);
	assert(strcmp(lista.lista[1].nume, "Aspacardin") == 0);
	assert(lista.lista[1].cantitate == 2000);
	assert(strcmp(lista.lista[2].nume, "Nurofen") == 0);

	filtrareCrescDesc(&lista, "descrescator");
	assert(strcmp(lista.lista[2].nume, "Aspacardin") == 0);
	assert(lista.lista[2].cantitate == 30);
	assert(strcmp(lista.lista[1].nume, "Aspacardin") == 0);
	assert(lista.lista[1].cantitate == 2000);
	assert(strcmp(lista.lista[0].nume, "Nurofen") == 0);
	destroyList(&lista);
}

void copiere(MyList* destinatie, MyList* sursa) {
	destinatie->lenght = sursa->lenght;
	for (int i = 0; i < destinatie->lenght;i++) {
		destinatie->lista[i].cod = sursa->lista[i].cod;
		destinatie->lista[i].cantitate = sursa->lista[i].cantitate;
		destinatie->lista[i].concentratie = sursa->lista[i].concentratie;
		strcpy_s(destinatie->lista[i].nume, sizeof(sursa->lista[i].nume), sursa->lista[i].nume);
	}

}

void testareCopiere() {
	MyList sursa = emptylist();
	MyList destinatie = emptylist();
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };
	adaugareRepo(&sursa, m1);
	adaugareRepo(&sursa, m2);
	copiere(&destinatie, &sursa);
	assert(destinatie.lenght == 2);
	assert(strcmp(destinatie.lista[0].nume,"Aspacardin") == 0);
	assert(strcmp(destinatie.lista[1].nume, "Brufen") == 0);
	destroyList(&sursa);
	destroyList(&destinatie);


}
void testareCantitate() {
	MyList sursa = emptylist();
	MyList destinatie = emptylist();
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };
	adaugareRepo(&sursa, m1);
	adaugareRepo(&sursa, m2);
	filtrareCantitate(&sursa,&destinatie,190);
	assert(destinatie.lenght == 1);
	assert(strcmp(destinatie.lista[0].nume, "Brufen") == 0);
	destroyList(&sursa);
	destroyList(&destinatie);


}

void testFiltrareCaracter() {
	MyList sursa = emptylist();
	MyList destinatie = emptylist();
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };
	medicament m3 = { 3,"Antrax",50,300 };
	adaugareRepo(&sursa, m1);
	adaugareRepo(&sursa, m2);
	adaugareRepo(&sursa, m3);
	filtrareLitera(&sursa, &destinatie, 'A');
	assert(destinatie.lenght == 2);
	assert(strcmp(destinatie.lista[0].nume, "Aspacardin") == 0);
	assert(strcmp(destinatie.lista[1].nume, "Antrax") == 0);
	destroyList(&sursa);
	destroyList(&destinatie);
}

void testareStergere() {
	MyList sursa = emptylist();
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };
	medicament m3 = { 3,"Antrax",50,300 };
	adaugareRepo(&sursa, m1);
	adaugareRepo(&sursa, m2);
	adaugareRepo(&sursa, m3);
	stergereService(&sursa, "Antrax");
	assert(sursa.lista[2].cantitate == 0);
	destroyList(&sursa);

}
void testareDelete() {
	MyList sursa = emptylist();
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };
	medicament m3 = { 3,"Antrax",50,300 };
	adaugareRepo(&sursa, m1);
	adaugareRepo(&sursa, m2);
	adaugareRepo(&sursa, m3);
	deleteService(&sursa, "Brufen");
	assert(sursa.lenght == 2);
	assert(strcmp(sursa.lista[0].nume, "Aspacardin") == 0);
	assert(strcmp(sursa.lista[1].nume, "Antrax") == 0);
	destroyList(&sursa);
}

void testareUpdate() {
	MyList sursa = emptylist();
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };
	medicament m3 = { 3,"Antrax",50,300 };
	adaugareRepo(&sursa, m1);
	adaugareRepo(&sursa, m2);
	adaugareRepo(&sursa, m3);
	updateService(&sursa, "Brufen", "Nurofen", 30);
	assert(strcmp(sursa.lista[1].nume, "Nurofen") == 0);
	assert(sursa.lista[1].concentratie == 30);
	destroyList(&sursa);
}
int cmpStoc(medicament* m1, medicament* m2) {
	if (m1->cantitate == m2->cantitate)
		return 0;
	if (m1->cantitate > m2->cantitate)
		return 1;
	else
		return -1;
}

int cmpStocD(medicament* m1, medicament* m2) {
	if (m1->cantitate == m2->cantitate)
		return 0;
	if (m1->cantitate < m2->cantitate)
		return 1;
	else
		return -1;
}

int cmpNume(medicament* m1, medicament* m2) {
	return strcmp(m1->nume, m2->nume);
}

int cmpNumeD(medicament* m1, medicament* m2) {
	if (strcmp(m1->nume, m2->nume) > 0)
		return -1;
	if (strcmp(m1->nume, m2->nume) < 0)
		return 1;
	else
		return 0;
}

MyList* sortStoc(MyList* l) {
	sort(l, cmpStoc);
	return l;
}

MyList* sortStocD(MyList* l) {
	sort(l, cmpStocD);
	return l;
}

MyList* sortNume(MyList* l) {
	sort(l, cmpNume);
	return l;
}
MyList* sortNumeD(MyList* l) {
	sort(l, cmpNumeD);
	return l;
}

void testSortStoc() {
	MyList l = emptylist();
	// Add some medicament objects to the list

	// Perform sorting
	MyList* sortedList = sortStoc(&l);

	// Assert statements to check if sorting is correct
	for (int i = 0; i < sortedList->lenght - 1; i++) {
		assert(cmpStoc(&(sortedList->lista[i]), &(sortedList->lista[i + 1])) <= 0);
		//}

		// Cleanup
	}
	destroyList(&l);
}

	void testSortStocD() {
		MyList l = emptylist();
		// Add some medicament objects to the list

		// Perform sorting
		MyList* sortedList = sortStocD(&l);

		// Assert statements to check if sorting is correct
		for (int i = 0; i < sortedList->lenght - 1; i++) {
			assert(cmpStocD(&(sortedList->lista[i]), &(sortedList->lista[i + 1])) <= 0);
		}

		// Cleanup
		destroyList(&l);
	}


void testSortNume() {
	// Create a list and add some medicament objects to it
	MyList l = emptylist();
	// Add some medicament objects to the list

	// Perform sorting
	sortNume(&l);

	// Assert statements to check if sorting is correct
	for (int i = 0; i < l.lenght - 1; i++) {
		assert(cmpNume(&(l.lista[i]), &(l.lista[i + 1])) <= 0);
	}

	// Cleanup
	destroyList(&l);
}

void testSortNumeD() {
	MyList l = emptylist();
	// Add some medicament objects to the list

	// Perform sorting
	MyList* sortedList = sortNumeD(&l);

	// Assert statements to check if sorting is correct
	for (int i = 0; i < sortedList->lenght - 1; i++) {
		assert(cmpNumeD(&(sortedList->lista[i]), &(sortedList->lista[i + 1])) <= 0);
	}

	// Cleanup
	destroyList(&l);
}

void testCmpStocD() {
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };

	// Test cases
	assert(cmpStocD(&m1, &m2) == -1); // m1 should be greater than m2
	assert(cmpStocD(&m2, &m1) == 1); // m2 should be less than m1

}

// Test case for cmpNume function
void testCmpNume() {
	// Create sample medicaments
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };

	// Test cases
	assert(cmpNume(&m1, &m2) < 0); // m1 should be less than m2
	assert(cmpNume(&m2, &m1) > 0); // m2 should be greater than m1

}

// Test case for cmpNumeD function
void testCmpNumeD() {
	// Create sample medicaments
	medicament m1 = { 1,"Aspacardin",10,200 };
	medicament m2 = { 2,"Brufen",20,100 };

	// Test cases
	assert(cmpNumeD(&m1, &m2) > 0); // m1 should be greater than m2
	assert(cmpNumeD(&m2, &m1) < 0); // m2 should be less than m1


}