replace(X,[X|T],E,[E|Rez]):-replace(X,T,E,Rez).
replace(X,[H|T],E,[H|Rez]):-X\=H,replace(X,T,E,Rez).
replace(_,[],_,[]).
