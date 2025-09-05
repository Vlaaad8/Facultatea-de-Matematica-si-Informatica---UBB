cautare(X,[X|_]).
cautare(X,[H|T]):-X\=H,cautare(X,T).

concatenare([],A,A).
concatenare([H|T],L,[H|Rez]):- \+ cautare(H,L),concatenare(T,L,Rez).
concatenare([H|T],L,Rez):- cautare(H,L),concatenare(T,L,Rez).

max([X],X).
max([],0).
max([H|T],N):-max(T,N1),(N1>H->N=H;N=N1).

elimina(X,[X|T],T).
elimina(X,[H|T],[H|Rez]):-X\=H,elimina(X,T,Rez).

sortare([],[]).
sortare(L,[M|Rez]):-min(L,M),elimina(M,L,V),sortare(V,Rez).


interclasare(L,T,R):-concatenare(L,T,Temp),sortare(Temp,R).
