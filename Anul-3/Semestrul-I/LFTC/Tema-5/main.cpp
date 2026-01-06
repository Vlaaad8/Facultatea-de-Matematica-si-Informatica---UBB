#include <iostream>

#include "header/Gramatic.h"

using namespace std;

int main() {

    Gramatic gramatic;

    gramatic.initialize_gramatic("gramatic.txt");
    gramatic.show_gramatic();
}