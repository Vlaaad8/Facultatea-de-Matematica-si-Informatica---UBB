#pragma once
#include "MyList.h"
#include "medicamente.h"
void adaugareService(MyList* l, int cod, char* nume, int concentratie, int cantitate);
void stergereService(MyList* l, char* nume);
void updateService(MyList* l, char* nume, char* numeNou, int concentratie);
void deleteService(MyList* l, char* nume);
void filtrareCrescDesc(MyList* l,char* tip);
void schimbMedicamente(medicament* pastila1, medicament* pastila2);
void testSchimbare();
void copiere(MyList* destinatie, MyList* sursa);
void testareCopiere();
void testareCantitate();
void filtrareCantitate(MyList* l, MyList* copie, int number);
void filtrareLitera(MyList* l, MyList* copie, char caracter);
void testFiltrareCaracter();
void testareStergere();
void testareUpdate();
void testareDelete();
int cmpStoc(medicament* m1, medicament* m2);
int cmpStocD(medicament* m1, medicament* m2);
int cmpNume(medicament* m1, medicament* m2);
int cmpNumeD(medicament* m1, medicament* m2);
MyList* sortStoc(MyList* l);
MyList* sortStocD(MyList* l);
MyList* sortNume(MyList* l);
MyList* sortNumeD(MyList* l);
void testSortStoc();
void testSortStocD();
void testSortNume();
void testSortNumeD();
void testCmpNumeD();
void testCmpStocD();
void testCmpNume();