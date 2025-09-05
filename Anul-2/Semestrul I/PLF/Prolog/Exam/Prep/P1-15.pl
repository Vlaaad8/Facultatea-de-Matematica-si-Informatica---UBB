min([H|T],M):-min(T,M1),(H<M1->M=H;M=M1).
min([X],X).
