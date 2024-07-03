#pragma once

#include "lista.h"

typedef int(*FunctieComparare)(Masina* m1, Masina* m2);

void sort(Lista* l, FunctieComparare cmpF);
