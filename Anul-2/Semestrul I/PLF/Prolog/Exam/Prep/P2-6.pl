adauga([],[]).
adauga([H|T],R):-is_list(H),adauga(H,H1),adauga(T,T1),concateneaza(H1,T1,R).
adauga([H|T],[H|R]):-number(H),adauga(T,R).

concateneaza([],L,L).
concateneaza([H|T],L,[H|R]):-concateneaza(T,L,R).

schimba(X,[X|T],E,[E|R]):-schimba(X,T,E,R).
schimba(X,[H|T],E,[H|R]):-X\=H,schimba(X,T,E,R).
schimba(_,[],_,[]).

functie(X,L,El,R):-schimba(X,L,El,M),adauga(M,R).
