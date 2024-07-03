#include "mysort.h"

void sort(Lista* l, FunctieComparare cmpF)
{
	for (int i = 0; i < lungime(l); i++)
		for (int j = i+1; j < lungime(l); j++) {
			Masina masina1 = get_masina(l, i);
			Masina masina2 = get_masina(l, j);
			if (cmpF(&masina1, &masina2)>0) {
				TElem masina_aux = l->lista_masini[i];
				l->lista_masini[i] = l->lista_masini[j];
				l->lista_masini[j] = masina_aux;
			}
		}
}