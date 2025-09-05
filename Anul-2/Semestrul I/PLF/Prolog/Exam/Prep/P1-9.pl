construieste(A,A,[A]).
construieste(M,N,[M|Rez]):-M<N,M1 is M+1,construieste(M1,N,Rez).
