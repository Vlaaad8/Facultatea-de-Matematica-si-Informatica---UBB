cauta(X,[X|_]).
cauta(X,[H|T]):-X\=H,cauta(X,T).
multime([],[]).
multime([H|T],[H|Rez]):- multime(T,Rez), \+ cauta(H,Rez).
multime([H|T],Rez):- multime(T,Rez),cauta(H,Rez).
