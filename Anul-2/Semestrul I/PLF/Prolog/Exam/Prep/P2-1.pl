inverseaza([],Aux,Aux).
inverseaza([H|T],Aux,R):-inverseaza(T,[H|Aux],R).

inv(L,R):-inverseaza(L,[],R).


suma([H|T],[H1|T1],F,[N|R]):-M is (H1+H+F),M>9,N is M mod 10,suma(T,T1,1,R).
suma([H|T],[H1|T1],F,[M|R]):-M is (H1+H+F),M<10,suma(T,T1,0,R).
suma([],[H1|T1],F,[N|R]):-M is (H1+F),M>9,N is M mod 10,suma([],T1,1,R).
suma([],[H1|T1],F,[M|R]):-M is (H1+F),M<10,suma([],T1,0,R).
suma([H|T],[],F,[N|R]):-M is (H+F),M>9,N is M mod 10,suma(T,[],1,R).
suma([H|T],[],F,[M|R]):-M is (H+F),M<10,suma(T,[],0,R).
suma([],[],1,[1]).
suma([],[],0,[]).

sumaF(L,M,Rez):-inv(L,R),inv(M,R1),suma(R,R1,0,Tmp),inv(Tmp,Rez).

