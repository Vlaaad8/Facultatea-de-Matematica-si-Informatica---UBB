GO 
CREATE DATABASE Practic3
go

CREATE TABLE PresentationShop(
idShop INT PRIMARY KEY,
Nume varchar(50),
Oras varchar(50)
)

CREATE TABLE Woman(
idWoman INT PRIMARY KEY,
Nume varchar(50),
MaximCheltuiala varchar(50)
)


CREATE TABLE ShoeModel(
idModel INT PRIMARY KEY,
Nume varchar(50),
Sezon varchar(50)
)

CREATE TABLE Shoe(
idShoe INT PRIMARY KEY,
idModel INT FOREIGN KEY REFERENCES ShoeModel(idModel),
Pret INT
)

CREATE TABLE Stoc(
idShoe INT FOREIGN KEY REFERENCES Shoe(idShoe),
idShop INT FOREIGN KEY REFERENCES PresentationShop(idShop),
CONSTRAINT pk_Stoc PRIMARY KEY(idShoe,idShop),
Disponibilitate INT
)

CREATE TABLE Tranzactii(
idWoman INT FOREIGN KEY REFERENCES Woman(idWoman),
idShoe INT FOREIGN KEY REFERENCES Shoe(idShoe),
CONSTRAINT pk_Tranzactii PRIMARY KEY(idWoman,idShoe),
NumarPantofi INT,
Cheltuiala INT
)


-- Introducere valori în tabelul PresentationShop
INSERT INTO PresentationShop (idShop, Nume, Oras) VALUES
(1, 'Shop1', 'Bucuresti'),
(2, 'Shop2', 'Cluj-Napoca'),
(3, 'Shop3', 'Timisoara');

-- Introducere valori în tabelul Woman
INSERT INTO Woman (idWoman, Nume, MaximCheltuiala) VALUES
(1, 'Ana', '500'),
(2, 'Maria', '700'),
(3, 'Elena', '600');

-- Introducere valori în tabelul ShoeModel
INSERT INTO ShoeModel (idModel, Nume, Sezon) VALUES
(1, 'Model1', 'Vara'),
(2, 'Model2', 'Iarna'),
(3, 'Model3', 'Toamna');

-- Introducere valori în tabelul Shoe
INSERT INTO Shoe (idShoe, idModel, Pret) VALUES
(1, 1, 200),
(2, 2, 300),
(3, 3, 250);

-- Introducere valori în tabelul Stoc
INSERT INTO Stoc (idShoe, idShop, Disponibilitate) VALUES
(1, 1, 10),
(2, 2, 5),
(3, 3, 8);

-- Introducere valori în tabelul Tranzactii
INSERT INTO Tranzactii (idWoman, idShoe, NumarPantofi, Cheltuiala) VALUES
(1, 1, 2, 400),
(2, 2, 1, 300),
(3, 3, 3, 750);


GO
CREATE OR ALTER FUNCTION Validare(
	@idShoe INT,
	@idMagazin INT,
	@nrPapuci INT
)RETURNS INT AS BEGIN
	DECLARE @validator INT
	SELECT @validator = COUNT(*) FROM Stoc WHERE idShoe=@idShoe and idShop=@idMagazin;
	if (@validator > 0)
		RETURN 1
	RETURN 0
END
GO

GO
CREATE OR ALTER PROCEDURE Adaugare 
	@idShoe INT,
	@idShop INT,
	@nrPapuci INT
AS BEGIN
	DECLARE @validare INT
	SELECT @validare = dbo.Validare(@idShoe,@idShop,@nrPapuci)
	if(@validare = 1)
	BEGIN
		UPDATE Stoc SET Disponibilitate=@nrPapuci where idShoe=@idShoe AND idShop=@idShop
		SELECT * FROM Stoc
		PRINT('Am modificat stocul de marfa!')
	END

	IF(@validare = 0)
	BEGIN 
		INSERT INTO Stoc(idShoe,idShop,Disponibilitate) VALUES (@idShoe,@idShop,@nrPapuci)
		SELECT * FROM Stoc
		PRINT ('Am adaugat marfa noua!')
	END
END
GO

GO
CREATE OR ALTER FUNCTION CateMagazine(
	@idShoe Int
)RETURNS TABLE AS RETURN
	SELECT s.idShoe FROM Shoe s
	INNER JOIN Stoc t ON
	s.idShoe=t.idShoe
	GROUP BY s.idShoe 
	HAVING COUNT(s.idShoe) >= @idShoe
GO

SELECT * FROM CateMagazine(3)