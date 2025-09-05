%Model de flux (i,i,O)
%Secv(L,N,L)
%L-lista
%N-int
secventa([H1,H2|T],0,R):-H1<0,H2<0,secventa([H2|T],1,R).
secventa([H1,H2|T],1,R):-H1<0,H2<0,secventa([H2|T],1,R).
secventa([H1,H2|T],1,R):-H1<0,H2>0,secventa([H2|T],0,R).
secventa([H1,H2|T],0,[H1|R]):-secventa([H2|T],0,R),!.
secventa([_],1,[]).
secventa([X],0,[X]):-number(X).


%duplicaPar(L,R)
%L-lista
%R-lista cu rezultat
%Model de flux:(i,o)
duplicaPar([H1|T],[H1,H1|Rez]):-H1 mod 2 =:=0,duplicaPar(T,Rez).
duplicaPar([H1|T],[H1|Rez]):-H1 mod 2 =:=1,duplicaPar(T,Rez).
duplicaPar([],[]).

%L-Lista
%R-rezutlat
%Model de flux (i,0)
functie(L,R):-secventa(L,0,M),duplicaPar(M,R).
