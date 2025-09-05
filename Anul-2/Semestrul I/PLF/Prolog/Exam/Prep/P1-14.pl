selecteaza([_|T],N,Rez):-N>0,N1 is N-1,selecteaza(T,N1,Rez).
selecteaza([H|_],0,H).
