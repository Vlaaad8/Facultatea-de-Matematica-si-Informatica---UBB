#### <stare> ::= Stari:  <listaStari>
#### <listaStari> ::=  <nod> | <nod> , <listaStari>

#### <alfabet> ::= Alfabet: <listaAlfabet>
#### <listaAlfabet> ::= <cifra> | <cifra> , <cifra>

#### <stareInitiala> ::= StareInitiala: <nod>

#### <stariFinale> ::= StariFinale: <listaStari>

#### <tranzitii> ::= Tranzitii: <listaTranziti>
#### <listaTranzitii> ::= <tranzitie> | <tranzitie> , <listaTranzitii>
#### <tranzitie> ::= <nod> -> <cifra> -> <nod>

#### <nod> ::= <litera> <cifra>