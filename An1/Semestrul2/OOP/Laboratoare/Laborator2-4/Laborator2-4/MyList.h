#pragma once
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include "medicamente.h"


typedef struct {
	medicament* lista;
	int lenght;
	int capacitate;
} MyList;


MyList emptylist();
medicament get(MyList* l, int poz);

void adaugareRepo(MyList* lista, medicament m);

void stergereRepo(MyList* lista, medicament pastila);

void updateRepo(MyList* list, medicament pastila, char* numeNou);
void deleteRepo(MyList* l, medicament pastila);
void test_emptylist();
void testFiltrare();
void testareExistenta();
void testAddRepo();
void destroyList(MyList* l);