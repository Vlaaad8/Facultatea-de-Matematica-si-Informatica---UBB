cmmdc(A,0,A).
cmmdc(A,B,R):-B>0,M is A mod B,cmmdc(B,M,R).

lista([X],X).
lista([H|T],R):-lista(T,Tmp),cmmdc(H,Tmp,R).
