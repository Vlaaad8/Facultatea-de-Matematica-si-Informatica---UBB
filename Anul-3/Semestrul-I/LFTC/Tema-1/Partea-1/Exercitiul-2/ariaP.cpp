#include <iostream>
using namespace std;

int main() {
    float pi, raza, aria, perimetru;
    cout << "Introduceti raza cercului:";
    cin >> raza;
    pi = 3.14;
    perimetru = 2 * pi * raza;
    aria = pi * raza * raza;
    cout << "Aria cercului este: ";
    cout << aria;
    cout << " Perimetru este: ";
    cout << perimetru;
}
