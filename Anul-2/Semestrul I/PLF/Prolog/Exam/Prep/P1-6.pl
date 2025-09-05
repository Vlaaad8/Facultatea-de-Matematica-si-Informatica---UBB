numara(_,[],0).
numara(X,[X|T],N):-numara(X,T,N1),N is N1+1.
numara(X,[H|T],N):-X\=H,numara(X,T,N).

elimina([],_,[]).
elimina([H|T],L,[H|Rez]):-numara(H,L,N),N<2,elimina(T,L,Rez).
elimina([H|T],L,Rez):-numara(H,L,N),N>1,elimina(T,L,Rez).

eliminaF(L,Rez):-elimina(L,L,Rez).


maximum([X],X).
maximum([],0).
maximum([H|T],N):-maximum(T,N1),(H>N1->N=H;N=N1).
