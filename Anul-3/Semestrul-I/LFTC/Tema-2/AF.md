## Specificare input program

#### <stare> ::= Stari:  <listaStari>
#### <listaStari> ::=  <nod> | <nod> , <listaStari>
#### <nod> ::= <litera> <cifra>

#### <alfabet> ::= Alfabet: <listaAlfabet>
#### <listaAlfabet> ::= <element> | <element> , <listaAlfabet>
#### <element> ::= <cifra> | <litera>
#### <cifra> ::= 0 | 1 ... | 9
#### <litera> ::= a | b | c | d | e | f | x

#### <stareInitiala> ::= StareInitiala: <nod>

#### <stariFinale> ::= StariFinale: <listaStari>

#### <tranzitii> ::= Tranzitii: <listaTranziti>
#### <listaTranzitii> ::= <tranzitie> | <tranzitie> , <listaTranzitii>
#### <tranzitie> ::= <nod> -> <cifra> -> <nod>