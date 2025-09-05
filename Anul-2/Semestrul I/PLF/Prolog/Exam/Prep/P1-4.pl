substituie([],_,_,[]).
substituie([H|T],H,L,[L|Rez]):-substituie(T,H,L,Rez).
substituie([H|T],X,L,[H|Rez]):-H\=X,substituie(T,X,L,Rez).

remove([_|T],0,T).
remove([H|T],N,[H|Rez]):-
	N>0,N1 is N-1,remove(T,N1,Rez).

