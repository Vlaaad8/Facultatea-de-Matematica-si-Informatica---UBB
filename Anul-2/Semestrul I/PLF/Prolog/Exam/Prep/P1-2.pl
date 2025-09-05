cmmdc(A,0,A).
cmmdc(A,B,R):-B\=0,M is A mod B,cmmdc(B,M,R).

cmmmc(A,B,M):-cmmdc(A,B,R),M is A*B div R.

lista([A,B],R):-cmmmc(A,B,R).
lista([H|T],R):-lista(T,Temp),cmmmc(H,Temp,R).


%b
adaugare([],_,_,[]).

