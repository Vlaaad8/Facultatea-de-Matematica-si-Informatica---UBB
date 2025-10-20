#include <iostream>

using namespace std;

int main() {
    int n,x,s;
    s=0;

    cout<<"Introduceti numarul de numere:";
    cin>>n;

    while(n>0) {
        cout<<"Introduceti un numar:";
        cin>>x;
        s=s+x;
        n=n-1;
    }
    cout<<s;
}