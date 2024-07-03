#include "customSort.h"
#include "medicamenteService.h"

/*
* Sorteaza o lista data
* @param l: lista care se sorteaza
* @param cmpF: functia (relatia) dupa care se sorteaza
*
* post: lista l este sortata
*/
void sort(MyList* l, CompareFct cmpF) {
    int i, j;
    for (i = 0; i < l->lenght - 1; i++) {
        for (j = i + 1; j < l->lenght; j++) {
            if (cmpF(&(l->lista[i]), &(l->lista[j])) > 0) {
                // Swap elements l->lista[i] and l->lista[j]
                medicament temp = l->lista[i];
                l->lista[i] = l->lista[j];
                l->lista[j] = temp;
            }
        }
    }
}
void testSort() {
    // Create a test list
    MyList testList = emptylist();
    adaugareService(&testList, 1, "Med1", 10, 50);
    adaugareService(&testList, 2, "Med2", 5, 30);
    adaugareService(&testList, 3, "Med3", 15, 20);

    // Define a comparison function (e.g., cmpStoc for sorting by stoc)


    // Sort the test list using the sort function
    sort(&testList, cmpStoc);

    // Assert that the resulting list is sorted
    assert(testList.lista[0].cantitate == 20); // First element should have stoc 5
    assert(testList.lista[1].cantitate == 30); // First element should have stoc 5
    assert(testList.lista[2].cantitate == 50); // First element should have stoc 5

    // Clean up
    destroyList(&testList);
}