consecutive([H1,H2|T],Rez,0):-H2=:=(H1+1),consecutive([H2|T],Rez,1),!.
consecutive([H1,H2|T],Rez,1):-H2=:=(H1+1),consecutive([H2|T],Rez,1),!.
consecutive([H1,H2|T],Rez,1):-H2\=(H1+1),consecutive([H2|T],Rez,0),!.
consecutive([H1,H2|T],[H1|Rez],0):-H2\=(H1+1),consecutive([H2|T],Rez,0),!.

consecutive([_],[],1).
consecutive([X],[X],0).
