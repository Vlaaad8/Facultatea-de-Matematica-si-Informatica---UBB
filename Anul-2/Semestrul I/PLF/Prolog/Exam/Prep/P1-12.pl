sublista([],0,0,[]).
sublista([_|T],M,N,Rez):- M>0,M1 is M-1,sublista(T,M1,N,Rez).
sublista([H|T],0,N,[H|Rez]):-N>0,N1 is N-1,sublista(T,0,N1,Rez).
sublista([_|T],0,0,Rez):-sublista(T,0,0,Rez).

