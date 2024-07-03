#include <stdio.h>

#include "masina.h"
#include "lista.h"
#include "car_rent_service.h"

void UI_print_lista_masini(Lista* l) {
	// interfata pentru functionalitatea de afisare
	// a listei de masini
	printf("\nLista masini : \n");
	for (int i = 0; i < lungime(l); i++)
		printf("%d.%s   %s   %s\n", i + 1, l->lista_masini[i].nr_inmatr, l->lista_masini[i].model, l->lista_masini[i].categorie);
	printf("\n");
}
void UI_adaugare_masina(Lista* l) {
	// interfata pentru functionalitatea de adaugare masina
	printf("\n");
	char nr_inmatriculare[20], model[20], categorie[20];
	printf("Introduceti numarul de inmatriculare : ");
	scanf_s("%s", nr_inmatriculare,20);
	printf("Introduceti modelul masinii : ");
	scanf_s("%s", model,20);
	printf("Introduceti categoria masinii : ");
	scanf_s("%s", categorie,20);
	int eroare = serv_adaugare_masina(l, nr_inmatriculare, model, categorie);
	if (eroare == 0)
		printf("\nMasina adugata cu succes!\n");
	else if (eroare == -2)
		printf("\nExista deja o masina cu numarul de inmatriculare %s\n", nr_inmatriculare);
	else printf("\nEroare adaugare masina!\n");
	printf("\n");
}

void UI_actualizare_masina(Lista* l) {
	// interfata pentru actualizare masina
	printf("\n");
	char nr_inmatr[20], model[20], categorie[20];
	printf("Introduceti numarul de inmatriculare a masinii : ");
	scanf_s("%s", nr_inmatr,20);
	int poz = serv_cautare_masina(l, nr_inmatr);
	if (poz != -1) {
		printf("Introduceti numarul actual de inmatriculare : ");
		scanf_s("%s", nr_inmatr,20);
		printf("Introduceti modelul actual al masinii : ");
		scanf_s("%s", model,20);
		printf("Introduceti categoria actuala a masinii : ");
		scanf_s("%s", categorie,20);
		int val = serv_actualizare_masina(l, poz, nr_inmatr, model, categorie);
		if (val == 0)
			printf("\nMasina actualizata cu succes!\n");
		else printf("\nEroare actualizare masina!\n");
	}
	else printf("\nMasina neexistenta in memorie!\n");
	printf("\n");
}

void UI_inchiriere_masina(Lista* l) {
	// interfata pentru functionalitatea de inchiriere masina
	char nr_inmatr[20];
	printf("\n");
	printf("Introduceti numarul de inmatriculare a masinii : ");
	scanf_s("%s", nr_inmatr,20);
	int error = serv_inchiriere_masina(l, nr_inmatr);
	if (error == 0)
		printf("\nMasina inchiriata cu succes!\n");
	else if (error == -1)
		printf("\nMasina este deja inchiriata!\n");
	else if(error == -2)
		printf("\nMasina neexistenta in memorie!\n");
	printf("\n");
}

void UI_returnare_masina(Lista* l) {
	// interfata pentru functionalitatea de returnare masina
	char nr_inmatr[20];
	printf("\n");
	printf("Introduceti numarul de inmatriculare a masinii: ");
	scanf_s("%s", nr_inmatr,20);
	int error = serv_returnare_masina(l, nr_inmatr);
	if (error == 0)
		printf("\nMasina returnata cu succes!\n");
	else if (error == -1)
		printf("\nMasina este deja returnata!\n");
	else if(error == -2)
		printf("\nMasina neexistenta in memorie!\n");
	printf("\n");
}

void UI_filtrare_masina_categorie(Lista* l) {
	// interfata pentru filtrare masini dupa categorie
	char categorie[20];
	printf("\n");
	printf("Introduceti categoria de masini : ");
	scanf_s("%s", categorie,20);
	Lista* l_filtrat = creare_lista();
	int error = serv_filtrare_masini_categorie(l, l_filtrat, categorie);
	if (!error)
		UI_print_lista_masini(l_filtrat);
	else printf("\nNu exista masini cu categoria %s\n", categorie);
	dealocare_lista(l_filtrat);
}

void UI_filtrare_masina_model(Lista* l) {
	// interfata pentru filtrare masini dupa model
	char model[20];
	printf("\n");
	printf("Introduceti modelul de masini : ");
	scanf_s("%s", model,20);
	Lista* l_filtrat = creare_lista();
	int error = serv_filtrare_masini_model(l, l_filtrat, model);
	if (!error)
		UI_print_lista_masini(l_filtrat);
	else printf("\nNu exista masini cu modelul %s\n", model);
	dealocare_lista(l_filtrat);
}

void UI_sortare_lista_dupa_model(Lista* l) {
	// interfata pentru sortare lista de masini dupa model
	int mod;
	printf("\nAlegeti modul de sortare\n1. Crescator\n2. Descrescator\nIntroduceti optiunea : ");
	scanf_s("%d", &mod);
	int error = serv_sortare_lista_dupa_model(l, mod);
	if (!error)
		printf("\nSortarea s-a realizat cu succes!\n");
	else printf("\nCeva nu a mers bine!\n");
	printf("\n");
}

void UI_sortare_lista_dupa_categorie(Lista* l) {
	// interfata pentru sortare lista de masini dupa categorie
	int mod;
	printf("\nAlegeti modul de sortare\n1. Crescator\n2. Descrescator\nIntroduceti optiunea : ");
	scanf_s("%d", &mod);
	int error = serv_sortare_lista_dupa_categorie(l, mod);
	if (!error)
		printf("\nSortarea s-a realizat cu succes!\n");
	else printf("\nCeva nu a mers bine!\n");
	printf("\n");
}

void run() {
	// se porneste consola
	Lista* l = creare_lista();
	int ruleaza = 1;
	while (ruleaza) {
		printf("Meniu:\n1. Afisare lista masini\n2. Adaugare masina\n3. Actualizare masina\n4. Inchiriere masina\n5. Returnare masina\n");
		printf("6. Filtrare masini dupa categorie\n7. Filtrare masini dupa model\n8. Sortare lista dupa model\n9. Sortare lista dupa categorie\n10. Exit\nIntroduceti optiunea : ");
		int comanda;
		scanf_s("%d", &comanda);
		switch (comanda) {
		case 1:
			UI_print_lista_masini(l);
			break;
		case 2:
			UI_adaugare_masina(l);
			break;
		case 3:
			UI_actualizare_masina(l);
			break;
		case 4:
			UI_inchiriere_masina(l);
			break;
		case 5:
			UI_returnare_masina(l);
			break;
		case 6:
			UI_filtrare_masina_categorie(l);
			break;
		case 7:
			UI_filtrare_masina_model(l);
			break;
		case 8:
			UI_sortare_lista_dupa_model(l);
			break;
		case 9:
			UI_sortare_lista_dupa_categorie(l);
			break;
		case 10:
			ruleaza = 0;
			break;
		default:
			printf("Comanda invalida!\n");
		}
	}
	dealocare_lista(l);
}