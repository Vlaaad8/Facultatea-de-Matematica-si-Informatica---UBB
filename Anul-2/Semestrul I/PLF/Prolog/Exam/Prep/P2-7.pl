inverseaza([],Acc,Acc).

inverseaza([H|T],M,Rez):-inverseaza(T,[H|M],Rez).

inv(L,R):-inverseaza(L,[],R).

produs([H|T],F,N,[El|Rez]):-M is (H*N+F),M>9,El is M mod 10,J is M div 10,produs(T,J,N,Rez).
produs([H|T],F,N,[M|Rez]):-M is (H*N+F),M<10,produs(T,0,N,Rez).
produs([],0,_,[]).
produs([],F,_,[F]):-F>0.

produsF(L,N,Rez):-inv(L,V),produs(V,0,N,Tmp),inv(Tmp,Rez).
