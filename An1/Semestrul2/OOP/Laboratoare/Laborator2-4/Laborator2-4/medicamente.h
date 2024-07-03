#pragma once
#include "MyList.h"
#include <string.h>
#include <stdbool.h>
#include <assert.h>
#include <ctype.h>

typedef struct
{/* ne definim tipul de daca medicament, cu caracteristicile sale*/
	int cod;
	char nume[30];
	int concentratie;
	int cantitate;
}medicament;

medicament creator(int cod, char* nume, int concentratie, int cantitate);
bool validareCod(medicament pastila);
void testareValidare();