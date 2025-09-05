create database Anticariat
go
use Anticariat
go

CREATE TABLE Colectionar(
idColectionar INT PRIMARY KEY,
Nume varchar(50),
Prenume varchar(50),
Varsta INT,
NrPieseMobilier INT
)

CREATE TABLE Muzeu(
idMuzeu INT PRIMARY KEY,
Denumire varchar(50),
AnDatare DATE,
Oras varchar(50),
Judet varchar(50),
Tara varchar(50)
)


CREATE TABLE Artist(
idArtist INT PRIMARY KEY,
Nume varchar(50),
Prenume varchar(50),
Gen varchar(50),
AnulNasterii INT
)

CREATE TABLE PiesaMobilier(
idColectionar INT FOREIGN KEY REFERENCES Colectionar(idColectionar),
idArtist INT FOREIGN KEY REFERENCES Artist(idArtist),
CONSTRAINT pk_PiesaMobilier PRIMARY KEY(idColectionar,idArtist),
Denumire varchar(50),
AnDatare INT,
Valoare INT
)

CREATE TABLE Expozitie(
idArtist INT FOREIGN KEY REFERENCES Artist(idArtist),
idMuzeu INT FOREIGN KEY REFERENCES Muzeu(idMuzeu),
CONSTRAINT pk_Expozitie PRIMARY KEY(idArtist,idMuzeu),
DataExpunere DATE,
Pret INT)


INSERT INTO Colectionar (idColectionar, Nume, Prenume, Varsta, NrPieseMobilier) VALUES
(1, 'Popescu', 'Ion', 45, 10),
(2, 'Balahura', 'Maria', 37, 7),
(3, 'Bivol', 'George', 50, 15);


INSERT INTO Muzeu (idMuzeu, Denumire, AnDatare, Oras, Judet, Tara) VALUES
(1, 'A', '2040-01-01', 'Bucuresti', 'Bucuresti', 'Romania'),
(2, 'C', '2017-01-01', 'Suceava', 'Adunati', 'Romania'),
(3, 'C', '1901-01-01', 'Cluj-Napoca', 'Cluj-Napoca', 'Romania');



INSERT INTO Artist (idArtist, Nume, Prenume, Gen, AnulNasterii) VALUES
(1, 'Picasso', 'Pablo', 'M', 2001),
(2, 'Van Gogh', 'Vincent', 'M', 2003),
(3, 'Dali', 'Salvador', 'M', 1994)


INSERT INTO PiesaMobilier (idColectionar, idArtist, Denumire, AnDatare, Valoare) VALUES
(1, 1, 'Scaun', 2000, 25000),
(2, 2, 'Masa', 2015, 1750),
(3, 3, 'Canapea', 2030, 1000);

INSERT INTO Expozitie (idArtist, idMuzeu, DataExpunere, Pret) VALUES
(1, 1, '2023-10-01', 1100),
(2, 2, '2023-11-15', 1500),
(3, 3, '2023-12-20', 5200);


INSERT INTO Expozitie (idArtist, idMuzeu, DataExpunere, Pret) VALUES
(3,2,'2024-10-19',109)

GO
CREATE OR ALTER FUNCTION Afisare(
	@numar INT
)RETURNS TABLE AS RETURN
	SELECT p.Denumire from PiesaMobilier p INNER JOIN
	Expozitie e ON p.idArtist=e.idArtist 
	GROUP BY p.Denumire 
	HAVING COUNT(e.idArtist)>=@numar;
GO

GO
CREATE OR ALTER FUNCTION ValidareExponat(
	@idArtist INT,
	@idMuzeu INT,
	@dataExpunere DATE,
	@pret INT
)RETURNS INT AS BEGIN
	declare @validare INT
	SELECT @validare = COUNT(*) from Expozitie where idArtist=@idArtist and idMuzeu=@idMuzeu
	if @validare > 0
		RETURN 0
	RETURN 1
END
GO


GO
CREATE OR ALTER PROCEDURE AdaugaExponat
	@idArtist INT,
	@idMuzeu INT,
	@dataExpunere DATE,
	@pret INT

AS BEGIN
	DECLARE @validare INT
	SELECT @validare = dbo.ValidareExponat(@idArtist,@idMuzeu,@dataExpunere,@pret)
	IF (@validare = 0)
	BEGIN
		UPDATE Expozitie
			set DataExpunere=@dataExpunere, Pret=@pret 
			where idArtist=@idArtist and idMuzeu=@idMuzeu
		SELECT * FROM Expozitie
		print('Am modificat datele un exponat!')
	END

	IF(@validare = 1)
	BEGIN
		INSERT INTO Expozitie(idArtist,idMuzeu,DataExpunere,Pret) VALUES
		(@idArtist,@idMuzeu,@dataExpunere,@pret)
		SELECT * FROM Expozitie
		print('Am adaugat un nou exponat!')
	END
END
GO


EXEC AdaugaExponat 2,3,'2020-02-12',1
SELECT * FROM Expozitie


GO
CREATE OR ALTER FUNCTION ArtistiCuNrMinimExpuneri()
RETURNS TABLE
AS
RETURN
    WITH ArtistiMinExpuneri AS (
        SELECT idArtist, COUNT(*) AS NrExpuneri
        FROM Expozitie
        GROUP BY idArtist
        HAVING COUNT(*) = (
            SELECT MIN(NrExpuneri)
            FROM (
                SELECT COUNT(*) AS NrExpuneri
                FROM Expozitie
                GROUP BY idArtist
            ) AS ExpuneriArtisti
        )
    )
    SELECT 
        p.Denumire AS DenumirePiesa,
        p.AnDatare,
        p.Valoare
    FROM ArtistiMinExpuneri a
    INNER JOIN PiesaMobilier p ON a.idArtist = p.idArtist;
GO

SELECT * FROM ArtistiCuNrMinimExpuneri()