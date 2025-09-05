prim(N,D):-N>=D*D,N mod D >0,D1 is D+1,prim(N,D1).
prim(N,D):-N<D*D,N\=1.

primF(N):-prim(N,2).

double([H|T],[H,H|Rez]):-primF(H),double(T,Rez).
double([H|T],[H|Rez]):- \+ primF(H), double(T,Rez).
double([],[]).
