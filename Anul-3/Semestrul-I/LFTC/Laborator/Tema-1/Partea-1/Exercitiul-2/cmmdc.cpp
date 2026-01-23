#include <iostream>
using namespace std;

int main() {
    int a ,b;
    cout<<"Introduceti variabila A:";
    cin>>a;
    cout<<"Introduceti variabila B:";
    cin>>b;

    while (a != b) {
        if (a > b) {
            a = a - b;
        } else {
            b = b - a;
        }
    }
    cout<<a;
}