%Cazul de oprire, atunci cand lista mea este goala
sum([],0).
%Cazul de continuare a recursivitatii.
sum([X|T],S):-sum(T,S1),S is S1+X.
