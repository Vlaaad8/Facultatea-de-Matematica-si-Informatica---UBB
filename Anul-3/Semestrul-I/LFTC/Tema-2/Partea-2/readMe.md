## LFTC - Laborator 1
### Limbajul de programare ales: C++

#### Elemente specifice limbajului
#### int ,main, string,float, include,iostream, using, namespace, std,

### Specificare lexicala

#### <ID> ::= <litera> <restID>
#### <restID> ::= <litera> <restID> | <cifra> <restID> | e
#### <litera> ::= a | b ... z
#### <cifra> ::= 0 | 1 ... 9
#### <numar> ::= <cifra> | <cifra> <numar>
#### <numarFloat> ::= <numar> . <numar>
#### <cuvant> ::= " <litera> | <litera> <cuvant> "
#### <const> ::= <numar> | <numarFloat> | <cuvant>

#### <operator> ::= + | - | / | * | %
#### <operatorRelational> ::= == | < | > | !=

### Specificare sintaxa 

#### <program> ::= #include <iostream> using namespace std ; int main ( ) { <declVar\> <intrComp\> }
#### <declVar> ::= <tip> <listaVar> | <tip> <listaVar> <declVar>
#### <listaVar> ::= <ID> ; | <ID> , <listaVar>
#### <tip> ::=  int | float | string |<tipCustom>
#### <tipCustom> ::= struct <ID> { <declVar> }
####  <intrComp> ::= <intr> | <intr> <intrComp>
#### <intr> ::= <atribuire> | <intrIf> | <intrWhile> | <intrCitire> | <intrAfisare>
#### <atribuire> ::= <ID> = <operand> ; | <ID> = <expresie> ;
#### <expresie> ::= <operand> <operator> <operand> | <operand> <operator> <expresie>
#### <operand> ::= <const> | <ID>
#### <intrIf> ::= if ( <conditie> ) { <intrComp> }
####                 | if ( <conditie> ) { <intrComp> } else  { <intrComp> }
#### <intrWhile> ::= while ( <conditie> ) {" <intrComp> }
#### <conditie> ::= <operand> | <operand> <operatorRelational> <operand>
#### <intrCitire> ::= cin >> <ID> ;
#### <intrAfisare> ::= cout << <ID> ; | cout << <const> ;