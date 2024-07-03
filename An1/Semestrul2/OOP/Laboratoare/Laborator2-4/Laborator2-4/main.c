#pragma once
#define _CRTDBG_MAP_ALLOC
#include <crtdbg.h>
#include <stdio.h>
#include <string.h>
#include "MyList.h"
#include "medicamente.h"
#include "medicamenteService.h"
#include "customSort.h"
#define RED_TEXT "\033[1;31m"
#define GREEN_TEXT "\033[1;32m"
#define YELLOW_TEXT "\033[1;33m"
#define RESET_COLOR "\033[0m"

	void adaugareUI(MyList* l) {
		/* primim datele de intrare, verificam daca datele sunt valide, verificam daca medicamentul exista si apoi adaugam*/
		int cod, concentratie, cantitate;
		char nume[30];
		printf("Introduceti codul:");
		scanf_s("%d", &cod);
		printf("Introduceti numele medicamentului:");
		scanf_s("%s", nume, (unsigned)sizeof(nume));
		printf("Introduceti concentratia:");
		scanf_s("%d", &concentratie);
		printf("Introduceti cantitatea:");
		scanf_s("%d", &cantitate);
		adaugareService(l, cod,nume,concentratie,cantitate);

		
	}
	

	void afisareUI(MyList* l){
		/* afiseaza elementele din lista*/
		if (l->lenght == 0) printf("Baza de date este complet goala! Adaugati medicamente.\n");
		else {
			for (int i = 0; i < l->lenght; i++) {
				printf("%d ", l->lista[i].cod);
				printf("%s ", l->lista[i].nume);
				printf("%d ", l->lista[i].concentratie);
				printf("%d\n", l->lista[i].cantitate);
			}
		}
	}

	void updateUI(MyList* l) {
		/* schimba concentratia unui medicament, cat si denumirea*/
		char nume[100], numeNou[100];
		int concentratie;
		printf("Introduceti numele medicamentului pe care doriti sa il cautati:");
		scanf_s( "%s", nume, (unsigned)sizeof(nume));
		printf("Introduceti numele nou al medicamentului:");
		scanf_s("%s", numeNou, (unsigned)sizeof(nume));
		printf("Introduceti noua concentratie:");
		scanf_s("%d", &concentratie);
		updateService(l, nume, numeNou, concentratie);

	}
	void stergereUI(MyList* l) {
		/* modifica stocul unui medicament la 0
		primeste ca parametru lista*/
		char numeMedicament[100];
		printf("Introduceti numele medicamentului a carui stoc doriti sa il stergeti:");
		scanf_s("%s", numeMedicament, (unsigned)sizeof(numeMedicament));
		stergereService(l, numeMedicament);
	}

	void deleteSpec(MyList* l) {
		char numeMedicament[100];
		printf("Introduceti numele medicamentului pe care doriti il stergeti:");
		scanf_s("%s", numeMedicament, (unsigned)sizeof(numeMedicament));
		deleteService(l, numeMedicament);
	}

	void filtrareUICresc(MyList* l) {
		MyList lFiltred = emptylist();
		copiere(&lFiltred, l);
		lFiltred = *sortNume(&lFiltred);
		afisareUI(&lFiltred);
		destroyList(&lFiltred);
	
	}
	void filtrareUIDescresc(MyList* l) {
		MyList lFiltred = emptylist();
		copiere(&lFiltred, l);
		lFiltred = *sortNumeD(&lFiltred);
		afisareUI(&lFiltred);
		destroyList(&lFiltred);

	}
	void filtrareUICrescStoc(MyList* l) {
		MyList lFiltred = emptylist();
		copiere(&lFiltred, l);
		lFiltred = *sortStoc(&lFiltred);
		afisareUI(&lFiltred);
		destroyList(&lFiltred);

	}
	void filtrareUIDescrescStoc(MyList* l) {
		MyList lFiltred = emptylist();
		copiere(&lFiltred, l);
		lFiltred = *sortStocD(&lFiltred);
		afisareUI(&lFiltred);
		destroyList(&lFiltred);

	}

	void filtrareOptiuni(MyList* l) {
		char optiune[30];
		MyList lFiltred = emptylist();
		printf("Introduceti modalitatea de filtrare, dupa stoc/litera:");
		scanf_s("%29s", optiune, (unsigned)sizeof(optiune)); 

		if (strcmp(optiune, "stoc") == 0) {
			int cant;
			printf("Introduceti cantiatea dupa care doriti sa filtrati:");
			scanf_s("%d", &cant);

			while (getchar() != '\n');

			filtrareCantitate(l, &lFiltred, cant);
			afisareUI(&lFiltred);
		}
		else if (strcmp(optiune, "litera") == 0) {
			char c;
			printf("Introduceti caracterul:");
			scanf_s(" %c", &c,(unsigned)sizeof(c)); // Add a space before %c to consume any whitespace or newline characters

			// Clearing the input buffer
			while (getchar() != '\n');

			filtrareLitera(l, &lFiltred, c);
			afisareUI(&lFiltred);
		}
	}
	void testAll()
	{
		testSchimbare();
		testareCopiere();
		testFiltrare();
		testareCantitate();
		testFiltrareCaracter();
		testareStergere();
		testareUpdate();
		testareDelete();
		test_emptylist();
		testareValidare();
		testareExistenta();
		testAddRepo();
		testSortStoc();
		testSortStocD();
		testSortNume();
		testSortNumeD();
		testSort();
		testCmpNumeD();
		testCmpStocD();
		testCmpNume();
	}

	void optiuni() {
		printf("1.Adaugarea unui medicament\n");
		printf("2.Afisarea listei cu medicamente\n");
		printf("3.Modificarea unui medicament\n");
		printf("4.Stergerea stocului unui medicament\n");
		printf("5.Stergerea unui medicament din baza de date\n");
		printf("6.Filtrare crescator)\n");
		printf("7.Filtrare descrescator)\n");
		printf("8.Filtrare crescator stoc)\n");
		printf("9.Filtrare descrescator stoc)\n");
		printf("10.Vizualizare medicamente dupa cantitate, prima litera\n");
		printf("11.Exit\n");
	}
	
	void meniu() {
		MyList lista = emptylist();
		optiuni();
		int cmd, final = 0;
		while (!final) {
			printf(YELLOW_TEXT "Introduceti comanda:" RESET_COLOR);
			scanf_s("%d", &cmd);
			switch (cmd)
			{
			case 1:
			{
				adaugareUI(&lista);
				break;
			}
			case 2: {
				afisareUI(&lista);
				break;
			}
			case 3: {
				updateUI(&lista);
				afisareUI(&lista);
				break;
			}
			case 4:
			{ stergereUI(&lista);
			break;
			}
			case 5:
			{deleteSpec(&lista);
				break;
			}
			case 6:
			{
				filtrareUICresc(&lista);
				break;
			}
			case 7:
			{
				filtrareUIDescresc(&lista);
				break;
			}
			case 8:
			{
				filtrareUICrescStoc(&lista);
				break;
			}
			case 9:
			{
				filtrareUIDescrescStoc(&lista);
				break;
			}
			case 10:
			{
				filtrareOptiuni(&lista);
				break;
			}
			case 11: {
				final = 1;
				destroyList(&lista);
				break;
			}
			default:
			{
				printf(RED_TEXT "Comanda invalida!\n" RESET_COLOR);
				break;
			}
			}
		}
	}
	int main() {
		meniu();
		testAll();
		_CrtDumpMemoryLeaks();
		

	}