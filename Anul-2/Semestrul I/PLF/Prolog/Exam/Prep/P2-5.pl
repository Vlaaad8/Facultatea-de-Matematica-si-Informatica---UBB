max([X],X).
max([H|T],N):-max(T,N1),(H>N1->N=H;N=N1).

pozitii([H|T],L,N,[N|Rez]):-N1 is N+1,max(L,M),H=:=M,pozitii(T,L,N1,Rez).
pozitii([H|T],L,N,Rez):-N1 is N+1,max(L,M),H\=M,pozitii(T,L,N1,Rez).
pozitii([],_,_,[]).

calcul(L,R):-pozitii(L,L,0,R).
