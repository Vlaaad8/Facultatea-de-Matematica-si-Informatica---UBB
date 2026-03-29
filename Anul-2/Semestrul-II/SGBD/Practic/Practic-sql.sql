create database Practic
use Practic
go

create table Copil(
idCopil INT PRIMARY KEY IDENTITY,
Nume varchar(50),
Varsta INT,
idGrupa INT FOREIGN KEY REFERENCES Grupa(idGrupa))

CREATE TABLE Grupa(
idGrupa INT PRIMARY KEY IDENTITY,
Cod varchar(50),
Nume varchar(50),
LimbaPredare varchar(50),
Educatoare varchar(50))


insert into Grupa(Cod,Nume,LimbaPredare,Educatoare) VALUES('123','Mare','Romana','Paula Nastase')
insert into Grupa(Cod,Nume,LimbaPredare,Educatoare) VALUES('ahsu23','Mica','Engleza','Sofia Ana')
insert into Grupa(Cod,Nume,LimbaPredare,Educatoare) VALUES('41hsu23','Mijlocie','Maghiara','Bianca Roman')
insert into Grupa(Cod,Nume,LimbaPredare,Educatoare) VALUES('a4mmj3a','Pregatitoare','Romana','Adela Mos')
INSERT INTO Copil(Nume,Varsta,idGrupa) VALUES('Mirela',9,4)

SELECT * FROM Copil

--Scrieti un sql care returneaza toate grupele pentru care media varstei e de sub 4 ani
SELECT g.Nume as 'Numele Grupei',AVG(c.Varsta) as 'Media Varstei' from Grupa g INNER JOIN Copil c on c.idGrupa=g.idGrupa 
group by g.Nume having AVG(c.varsta)<4

--Scrieti un sql care returnaza codul grupei unde se afla cel mai mic copil ordonati crescator dupa grupa
SELECT g.Cod FROM Copil c JOIN Grupa g ON c.idGrupa = g.idGrupa WHERE c.Varsta = (SELECT MIN(Varsta)FROM Copil)
order by g.Cod

CREATE NONCLUSTERED INDEX idx_c ON Grupa(Nume)
SELECT Nume from Grupa
drop index Grupa.idx_c