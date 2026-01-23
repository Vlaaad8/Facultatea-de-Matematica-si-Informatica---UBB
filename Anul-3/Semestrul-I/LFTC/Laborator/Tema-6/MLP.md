## LFTC - Laborator 1
### Limbajul de programare ales: C++

#### Elemente specifice limbajului
#### int ,main,float, include,iostream, using, namespace, std

### Specificare lexicala
````
<ID> ::= <litera> <restID>
<restID> ::= <litera> <restID> | <cifra> <restID> | e
<litera> ::= a | b ... z
<cifra> ::= 0 | 1 ... 9
<numar> ::= <cifra> | <cifra> <numar>
<numarFloat> ::= <numar> . <numar>
<const> ::= <numar> | <numarFloat> | <cuvant>

<operator> ::= + | -  
<operatorPrioritar> ::= * 
````
### Specificare sintaxa

````
<program> ::= #include <iostream> using namespace std ; int main ( ) { <declVar> <intrComp> }
<declVar> ::= <tip> <listaVar> | <tip> <listaVar> <declVar>
<listaVar> ::= <ID> ; | <ID> , <listaVar>

<tip> ::=  int | float 

<intrComp> ::= <intr> | <intr> <intrComp>
<intr> ::= <atribuire> | <intrCitire> | <intrAfisare>

<atribuire> ::= <ID> = <expresie> ;

<expresie> ::= <expresie> <operator> <termen> | <termen>

<termen> ::= <factor> | <termen> <operatorPrioritar> <factor> 
<factor> ::= <const> | <ID> | ( <expresie> ) 

<intrCitire> ::= cin >> <ID> ;
<intrAfisare> ::= cout << <ID> ;
````