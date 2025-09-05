use Fabrica
go

--Tabela pe care o vom folosi pentru a stoca istoricul actiunilor executate
CREATE TABLE LogManager(
Lid INT PRIMARY KEY IDENTITY,
TypeOperation VARCHAR(50),
TableOperation VARCHAR(50),
ExecutionDate DATETIME)


--Functii de validare pentru datele de intrare
--Conventie de validare:
-- 0: datele nu sunt valide
-- 1: datele sunt valide

GO
CREATE OR ALTER FUNCTION validateNume(@nume VARCHAR(50))
RETURNS INT 
AS 
BEGIN
	IF @nume IS NULL
		RETURN 0;
	IF len(@nume)<2
		RETURN 0;
	RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validateTip(@tip varchar(50))
RETURNS INT
AS 
BEGIN
	IF @tip IS NULL
		RETURN 0;
	RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validatePret(@pret int)
RETURNS INT 
AS 
BEGIN
	IF @pret<0
		RETURN 0;
	RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validateIDFabrica(@idFabrica int)
RETURNS INT
AS 
BEGIN
	IF @idFabrica IS NULL
		RETURN 0;
	IF (SELECT COUNT(*) FROM FabricaMedicamente where @idFabrica=FabricaMedicamente.IDFabrica) < 1
		RETURN 0;
	RETURN 1;
END
GO

GO 
CREATE OR ALTER FUNCTION validateVarsta(@varsta int)
RETURNS INT
AS
BEGIN
	IF @varsta IS NULL
		RETURN 0;
	IF @varsta <0 or @varsta>125
		RETURN 0;
	RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validateGen(@gen varchar(50))
RETURNS INT
AS
BEGIN
	IF @gen='barbat' or @gen='femeie'
		RETURN 1
	RETURN 0
END
GO

GO
CREATE OR ALTER FUNCTION validateJudet(@judet varchar(50))
RETURNS INT
AS
BEGIN
	IF @judet IS NULL
		RETURN 0
	IF len(@judet)<2
		RETURN 0;
	RETURN 1;
END
GO

GO
CREATE OR ALTER FUNCTION validateDescriere(@descriere varchar(50))
RETURNS INT
AS
BEGIN
	IF @descriere IS NULL
		RETURN 0
	IF len(@descriere)<2
		RETURN 0;
	RETURN 1;
END
GO	

CREATE OR ALTER FUNCTION validateIdMedicament(@idM int)
RETURNS INT
AS 
BEGIN
	IF (SELECT COUNT(*) FROM Medicament where @idM=IDMedicament) < 1
		RETURN 1;
	RETURN 0;
END
GO

CREATE OR ALTER FUNCTION validateIdClient(@idM int)
RETURNS INT
AS 
BEGIN
	IF (SELECT COUNT(*) FROM Client where @idM=IDClient) < 1
		RETURN 1;
	RETURN 0;
END
GO

GO
CREATE OR ALTER FUNCTION validateClient(
	@nume varchar(50),
	@varsta int,
	@gen varchar(50),
	@judet VARCHAR(50))
RETURNS VARCHAR(200)
AS
BEGIN
	DECLARE @error VARCHAR(200)
	SET @error=''

	IF (dbo.validateNume(@nume) = 0)
		SET @error= @error+ 'Nume Invalid '

	IF (dbo.validateVarsta(@varsta) = 0)
		SET @error= @error+ 'Varsta Invalid '
	IF (dbo.validateGen(@gen) = 0)
		SET @error= @error+ 'Gen Invalid '		
	IF (dbo.validateJudet(@judet) = 0)
		SET @error= @error+ 'Judet Invalid '
	RETURN @error
END
GO

GO
CREATE OR ALTER FUNCTION validateMedicament(
	@nume varchar(50),
	@tip VARCHAR(50),
	@pret INT,
	@idFabrica INT)
RETURNS VARCHAR(200)
AS
BEGIN
	DECLARE @error varchar(200)
	SET @error=''

	IF (dbo.validateNume(@nume) = 0)
		SET @error= @error+ 'Nume Invalid '
	IF (dbo.validateTip(@tip) = 0)
		SET @error=@error + 'Tip Invalid '
	IF (dbo.validatePret(@pret) = 0)
		SET @error=@error+ 'Pret Invalid '
	IF (dbo.validateIDFabrica(@idFabrica) = 0)
		set @error=@error+ 'IDFabrica Invalid '
	RETURN @error
END
GO


-- 1. O Procedura stocata de tip insert data m-n , daca procedura esueaza, rollback intreg
GO
CREATE OR ALTER PROCEDURE insertMedicamentClient(
	--Medicament
	@numeMedicament VARCHAR(50),
	@tip VARCHAR(50),
	@pret INT,
	@idFabrica INT,

	--Client
	@numeClient VARCHAR(50),
	@varsta INT,
	@gen VARCHAR(50),
	@judet VARCHAR(50),

	--Medicament-Client
	@deschiere VARCHAR(50),
	@total INT
	)
AS BEGIN
	BEGIN TRAN
	BEGIN TRY
	--Client

	PRINT '[Client] Intram in zona Clientului'
	PRINT '[Client] Incepe validarea clientului'

	DECLARE @error VARCHAR(200)
	SET @error = dbo.validateClient(@numeClient,@varsta,@gen,@judet)
	IF (@error != '')
		BEGIN
			PRINT '[Client] In urma validarii avem urmatoarele erori:' +@error
			RAISERROR(@error,14,1)
		END
	PRINT '[Client] Validarea clientului s-a realizat cu succes!'
	PRINT '[Client] Incepem inserarea clientului in baza de date.'

	INSERT INTO Client(Nume,Varsta,Gen,Judet) VALUES(@numeClient,@varsta,@gen,@judet)

	PRINT '[Client] Clientul a fost adaugat cu succes!'
	DECLARE @idClient INT
	SET @idClient= SCOPE_IDENTITY();
	PRINT '[Client] Viitorul client va avea codul:' + CONVERT(VARCHAR(10), @idClient);
	PRINT '[Log] Incepem sa adaugam in log-uri modificarea'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Insert','Client',GETDATE())
	PRINT '[Log] Am adaugat in log-uri modificarea'

	--Medicament 
	PRINT '[Medicament] Intram in zona Medicament'
	PRINT '[Medicament] Incepe validarea medicamentului'
	SET @error = dbo.validateMedicament(@numeMedicament,@tip,@pret,@idFabrica)
	IF @error != ''
		BEGIN
			PRINT '[Medicament] In urma validarii avem urmatoarele erori:' +@error
			RAISERROR(@error,14,1)
		END
	PRINT '[Medicament] Validarea medicamentului s-a realizat cu succes!'
	PRINT '[Medicament] Incepem inserarea medicamentului in baza de date.'

	INSERT INTO Medicament(Nume,Tip,Pret,IDFabrica) VALUES(@numeMedicament,@tip,@pret,@idFabrica)
	PRINT '[Medicament] Medicamentul a fost adaugat cu succes!'
	DECLARE @idMedicament INT
	SET @idMedicament= SCOPE_IDENTITY();
	PRINT '[Medicament] Viitorul medicament va avea codul:' + CONVERT(VARCHAR(10), @idMedicament);
	PRINT '[Log] Incepem sa adaugam in log-uri modificarea'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Insert','Medicament',GETDATE())
	PRINT '[Log] Am adaugat in log-uri modificarea'

	--Medicament-Client
	PRINT '[Medicament-Client] Intram in zona Medicament-Client'
	if (dbo.validateDescriere(@deschiere) = 0)
		SET @error=@error+'Descriere Invalida '
	IF (dbo.validatePret(@total) = 0)
		SET @error=@error+'Total Invalid Invalid'		
	IF @error != ''
		BEGIN
			PRINT '[Medicament-Client] In urma validarii avem urmatoarele erori:' +@error
			RAISERROR(@error,14,1)
	END
	PRINT '[Medicament-Client] Validarea comenzii s-a realizat cu succes!'
	PRINT '[Medicament] Incepem inserarea comenzii in baza de date.'

	INSERT INTO MedicamentClient(IDClient,IDMedicament,Descriere,Total) VALUES(@idClient,@idMedicament,@deschiere,@total)
	PRINT '[Medicament-Client] Comanda a fost adaugat cu succes!'
	PRINT '[Log] Incepem sa adaugam in log-uri modificarea'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Insert','Medicament-Client',GETDATE())
	PRINT '[Log] Am adaugat in log-uri modificarea'
	COMMIT TRAN
	PRINT '[Log] Am dat commit tranzactiei'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Commit','Tranzactie-Full',GETDATE())
END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		PRINT '[Log] Am dat rollback tranzactiei'
		PRINT '[Eroare]:' + @error +' '+ERROR_MESSAGE()
		INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Rollback','Tranzactie-Full',GETDATE())
	END CATCH
END
GO

--2. Procedura cu rollback partial
GO
CREATE OR ALTER PROCEDURE insertMedicamentClientPartial(
	--Medicament
	@numeMedicament VARCHAR(50),
	@tip VARCHAR(50),
	@pret INT,
	@idFabrica INT,

	--Client
	@numeClient VARCHAR(50),
	@varsta INT,
	@gen VARCHAR(50),
	@judet VARCHAR(50),

	--Medicament-Client
	@deschiere VARCHAR(50),
	@total INT
	)
AS BEGIN
	DECLARE @error varchar(200)
	DECLARE @isValid INT
	SET @isValid = 1

	--Client
BEGIN TRAN
	BEGIN TRY
	PRINT '[Client] Intram in zona Clientului'
	PRINT '[Client] Incepe validarea clientului'
	SET @error = dbo.validateClient(@numeClient,@varsta,@gen,@judet)
	IF (@error != '')
		BEGIN
			PRINT '[Client] In urma validarii avem urmatoarele erori:' +@error
			RAISERROR(@error,14,1)
		END
	PRINT '[Client] Validarea clientului s-a realizat cu succes!'
	PRINT '[Client] Incepem inserarea clientului in baza de date.'

	INSERT INTO Client(Nume,Varsta,Gen,Judet) VALUES(@numeClient,@varsta,@gen,@judet)

	PRINT '[Client] Clientul a fost adaugat cu succes!'
	DECLARE @idClient INT
	SET @idClient= SCOPE_IDENTITY();
	PRINT '[Client] Viitorul client va avea codul:' + CONVERT(VARCHAR(10), @idClient);
	PRINT '[Log] Incepem sa adaugam in log-uri modificarea'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Insert','Client',GETDATE())
	PRINT '[Log] Am adaugat in log-uri modificarea'
COMMIT TRAN
	PRINT '[Log] Am dat commit tranzactiei-Client'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Commit','Tranzactie-Client',GETDATE())
END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SET @isValid = 0;
		PRINT '[Log] Am dat rollback tranzactiei-Client'
		PRINT '[Eroare]:' + @error +' '+ERROR_MESSAGE()
		INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Rollback','Tranzactie-Client',GETDATE())
END CATCH

--Medicament
BEGIN TRAN
BEGIN TRY
	PRINT '[Medicament] Intram in zona Medicament'
	PRINT '[Medicament] Incepe validarea medicamentului'
	SET @error = dbo.validateMedicament(@numeMedicament,@tip,@pret,@idFabrica)
	IF @error != ''
		BEGIN
			PRINT '[Medicament] In urma validarii avem urmatoarele erori:' +@error
			RAISERROR(@error,14,1)
		END
	PRINT '[Medicament] Validarea medicamentului s-a realizat cu succes!'
	PRINT '[Medicament] Incepem inserarea medicamentului in baza de date.'

	INSERT INTO Medicament(Nume,Tip,Pret,IDFabrica) VALUES(@numeMedicament,@tip,@pret,@idFabrica)
	PRINT '[Medicament] Medicamentul a fost adaugat cu succes!'
	DECLARE @idMedicament INT
	SET @idMedicament= SCOPE_IDENTITY();
	PRINT '[Medicament] Viitorul medicament va avea codul:' + CONVERT(VARCHAR(10), @idMedicament);
	PRINT '[Log] Incepem sa adaugam in log-uri modificarea'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Insert','Medicament',GETDATE())
	PRINT '[Log] Am adaugat in log-uri modificarea'
COMMIT TRAN
	PRINT '[Log] Am dat commit tranzactiei-Medicament'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Commit','Tranzactie-Medicament',GETDATE())
END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SET @isValid = 0;
		PRINT '[Log] Am dat rollback tranzactiei-Medicament'
		PRINT '[Eroare]:' + @error +' '+ERROR_MESSAGE()
		INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Rollback','Tranzactie-Medicament',GETDATE())
END CATCH
	-- MedicamentClient(Comanda)
BEGIN TRAN
BEGIN TRY
PRINT '[Medicament-Client] Intram in zona Medicament-Client'
	if (dbo.validateDescriere(@deschiere) = 0)
		SET @error=@error+'Descriere Invalida '
	IF (dbo.validatePret(@total) = 0)
		SET @error=@error+'Total Invalid Invalid'		
	IF @error != ''
		BEGIN
			PRINT '[Medicament-Client] In urma validarii avem urmatoarele erori:' +@error
			RAISERROR(@error,14,1)
		END
		IF @isValid = 0
		BEGIN
			PRINT '[Medicament-Client] Nu putem adauga Comanda pentru ca medicamentul sau clientul nu sunt valide' +@error
			RAISERROR(@error,14,1)
		END
	PRINT '[Medicament-Client] Validarea comenzii s-a realizat cu succes!'
	PRINT '[Medicament] Incepem inserarea comenzii in baza de date.'

	INSERT INTO MedicamentClient(IDClient,IDMedicament,Descriere,Total) VALUES(@idClient,@idMedicament,@deschiere,@total)
	PRINT '[Medicament-Client] Comanda a fost adaugat cu succes!'
	PRINT '[Log] Incepem sa adaugam in log-uri modificarea'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Insert','Medicament-Client-Tranzactie',GETDATE())
	PRINT '[Log] Am adaugat in log-uri modificarea'
	COMMIT TRAN
	PRINT '[Log] Am dat commit tranzactiei'
	INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Commit','Tranzactie-Medicament-Client',GETDATE())
END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		PRINT '[Log] Am dat rollback tranzactiei'
		PRINT '[Eroare]:' + @error +' '+ERROR_MESSAGE()
		INSERT INTO LogManager(TypeOperation,TableOperation,ExecutionDate) VALUES('Rollback','Tranzactie-Medicament-Client',GETDATE())
	END CATCH
END
GO

--Testare 1.
SELECT * FROM Client
SELECT * FROM Medicament
SELECT * FROM MedicamentClient
SELECT * FROM LogManager

EXEC dbo.insertMedicamentClient 'Aspacardin','Antibiotic',1030,1,'Mugurel Manciu',-1,'barbat','Suceava','Pentru calciu',300
--EXEC dbo.insertMedicamentClient 'Aspacardin','Antibiotic',1030,1,'Mugurel Achihaei',49,'barbat','Suceava','Pentru calciu',300
--EXEC dbo.insertMedicamentClient 'Aspacardin','Antibiotic',1030,202,'Mugurel Balan',49,'barbat','Suceava','Pentru calciu',300
--EXEC dbo.insertMedicamentClientPartial 'Aspacardin','Antibiotic',1030,1,'Mugurel Achihaei',49,'barbat','Suceava','Pentru calciu',300
--EXEC dbo.insertMedicamentClientPartial 'Aspacardin','Antibiotic',1030,202,'Mugurel Manciu',49,'barbat','Suceava','Pentru calciu',300
--EXEC dbo.insertMedicamentClientPartial 'Aspacardin','Antibiotic',1030,1,'Mugurel Manciu',-1,'barbat','Suceava','Pentru calciu',300
SELECT * FROM Client
SELECT * FROM Medicament
SELECT * FROM MedicamentClient
SELECT * FROM LogManager
