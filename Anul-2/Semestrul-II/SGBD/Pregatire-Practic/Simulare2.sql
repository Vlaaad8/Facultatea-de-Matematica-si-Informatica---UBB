create database Simualare
go

use Simualare 
go


CREATE TABLE Autor(
idAutor INT PRIMARY KEY IDENTITY,
Nume VARCHAR(50),
Varsta INT,
Tara VARCHAR(50))


CREATE TABLE Carte(
idCarte INT PRIMARY KEY IDENTITY,
Titlu VARCHAR(50),
DataLansarii DATE,
idAutor INT FOREIGN KEY REFERENCES Autor(idAutor))

INSERT INTO Autor(Nume,Varsta,Tara) VALUES('Balahura Vlad',30,'Romania')
INSERT INTO Autor(Nume,Varsta,Tara) VALUES('Balahura Vlad1',30,'Anglia')
INSERT INTO Autor(Nume,Varsta,Tara) VALUES('Balahura Vlad2',30,'SUA')
INSERT INTO Autor(Nume,Varsta,Tara) VALUES('Balahura Vlad23',30,'SUA')
SELECT * FROM Carte

INSERT INTO Carte(Titlu,DataLansarii,idAutor) VALUES('Frumoasa adormita',GETDATE(),1)
INSERT INTO Carte(Titlu,DataLansarii,idAutor) VALUES('Frumoasa adormita1',GETDATE(),2)
INSERT INTO Carte(Titlu,DataLansarii,idAutor) VALUES('Frumoasa adormita2',GETDATE(),3)
INSERT INTO Carte(Titlu,DataLansarii,idAutor) VALUES('Frumoasa adormita',GETDATE(),2)
INSERT INTO Carte(Titlu,DataLansarii,idAutor) VALUES('Frumoasa adormita',GETDATE(),10)

CREATE NONCLUSTERED INDEX index1 ON Carte(Titlu)

SELECT COUNT(idAutor) as 'Carti publicate',idAutor as 'ID Autor' from Carte group by idAutor



SELECT c.Titlu from Carte c INNER JOIN Autor a on c.idAutor=a.idAutor
SELECT c.Titlu from Carte c RIGHT OUTER JOIN Autor a on c.idAutor=a.idAutor
SELECT c.Titlu from Carte c FULL OUTER JOIN Autor a on c.idAutor=a.idAutor