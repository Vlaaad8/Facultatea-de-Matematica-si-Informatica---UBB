USE Fabrica
-- Creeaza o procedura stocata pentru a testa timpul de executie al diverselor view-uri
CREATE OR ALTER PROCEDURE testRunViewProc AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @view INT;
	--Selectam clientii
	SELECT @view = ViewID FROM Views WHERE Name = 'View_Client';
	SET @start = SYSDATETIME();
	SELECT * FROM View_Client;
	SET @end = SYSDATETIME();
	PRINT('View_Client test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt,EndAt) VALUES
		('View_Client Test ', @start, @end);
	INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt) VALUES
		(@@IDENTITY, @view, @start, @end);


	--Selectam medicamentele
	SELECT @view = ViewID FROM Views WHERE Name = 'View_Medicament';
	SET @start = SYSDATETIME();
	SELECT * FROM View_Medicament;
	SET @end = SYSDATETIME();
	PRINT('View_Medicament test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt,EndAt) VALUES
		('View_Medicament Test ', @start, @end);
	INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt) VALUES
		(@@IDENTITY, @view, @start, @end);

	--Selectam comenzile de medicamente-clienti
	SELECT @view = ViewID FROM Views WHERE Name = 'View_MedicamentClient';
	SET @start = SYSDATETIME();
	SELECT * FROM View_MedicamentClient;
	SET @end = SYSDATETIME();
	PRINT('View_MedicamentClient test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt,EndAt) VALUES
		('View_MedicamentClient Test ', @start, @end);
	INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt) VALUES
		(@@IDENTITY, @view, @start, @end);
END

-- Procedura pentru a introduce clienti de test in tabela 'Client'
GO
CREATE OR ALTER PROC insertClienti AS
BEGIN
		SET IDENTITY_INSERT Client ON;
		-- Preia numarul de randuri care trebuie inserate din tabela TestTables
		DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'insertClienti');

		DECLARE @current INT = 1;
		DECLARE @id INT =30;
		-- Introduce randuri in tabela 'Client' folosind un loop
		WHILE @current <= @rows
		BEGIN
			INSERT INTO Client(IDClient,Nume,Varsta,Gen,Judet) VALUES
			(@id,'numeCL', 20, 'femeie', 'Suceava')
			SET @current = @current + 1
			SET @id=@id+1
		END
		SET IDENTITY_INSERT Client ON;

--Metoda pentru a calcula timpul de efectuare a inserarii de clienti
END
GO
CREATE OR ALTER PROCEDURE INSERT_CLIENTI AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;
		
	SELECT @table = TableID FROM Tables WHERE Name='Client';
	SET @start = SYSDATETIME();
	EXEC insertClienti;
	SET @end = SYSDATETIME();
	PRINT('insertClienti test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('insertClienti Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end);
END
--Inseram numarul specificat de Medicamente
GO
CREATE OR ALTER PROC insertMedicament AS
BEGIN
	SET IDENTITY_INSERT Medicament ON;
	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'insertMedicament');

	DECLARE @current INT = 1;
	DECLARE @id INT = 40;
	WHILE @current <= @rows
	BEGIN
		INSERT INTO Medicament(IDMedicament,Nume,Tip,Pret,IDFabrica) VALUES
		(@id, 'Paracetamol', 'Antibiotic', 100, 1);
		SET @current = @current + 1
		SET @id = @id + 1
	END
	SET IDENTITY_INSERT Medicament OFF;
END
--Calculam timpul pentru inserarea de medicamente
GO
CREATE OR ALTER PROCEDURE INSERT_MEDICAMENT AS
BEGIN
	SET IDENTITY_INSERT Medicament ON;
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name = 'Medicament';
	SET @start = SYSDATETIME();
	EXEC insertMedicament;
	SET @end = SYSDATETIME();
	PRINT('insertMedicament test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('insertMedicament Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end);

END

--Inseram comenzi de medicamente ale clientilor
GO
CREATE OR ALTER PROC insertMedicamentClient AS
BEGIN
	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'insertMedicamentClient');

		DECLARE @current INT = 1;
		DECLARE @idClient INT = 30;
		DECLARE @idMedicament INT = 40;
		WHILE @current <= @rows
		BEGIN
			INSERT INTO MedicamentClient(IDClient,IDMedicament) VALUES
			(@idClient,@idMedicament);
			SET @idClient = @idClient + 1
			SET @idMedicament = @idMedicament + 1
			SET @current = @current + 1
		END
END
--Calculam timpul de executare a comenzii
GO
CREATE OR ALTER PROCEDURE INSERT_MEDICAMENTCLIENT AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name='MedicamentClient';
	SET @start = SYSDATETIME();
	EXEC insertMedicamentClient;
	SET @end = SYSDATETIME();
	PRINT('insertMedicamentClient test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms')
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('insertMedicamentClient Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end);
END
--Stergem comenzile medicament-client prima data, invers ordinii de adaugare
GO
CREATE OR ALTER PROCEDURE deleteMedicamentClient AS
BEGIN
	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'deleteMedicamentClient');

	DECLARE @current INT = 1;
		DECLARE @idClient INT = 30;
		DECLARE @idMedicament INT = 40;
		WHILE @current <= @rows
		BEGIN
			DELETE FROM  MedicamentClient WHERE IDMedicament = @idMedicament AND IDClient = @idClient;
			SET @idClient = @idClient + 1
			SET @idMedicament = @idMedicament + 1
			SET @current = @current + 1
		END
END

GO
CREATE OR ALTER PROCEDURE DELETE_MEDICAMENTCLIENT AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name = 'MedicamentClient';
	SET @start = SYSDATETIME();
	EXEC deleteMedicamentClient;
	SET @end = SYSDATETIME();
	PRINT('deleteMedicamentClient test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms');
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('deleteMedicamentClient Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end)
END

GO
CREATE OR ALTER PROCEDURE deleteClienti AS
BEGIN
	
	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'deleteClienti');

	DECLARE @current INT = 1;
	DECLARE @id INT = 30;
	WHILE @current <= @rows
	BEGIN
		DELETE FROM Client WHERE IDClient = @id;
		SET @id = @id + 1
		SET @current = @current + 1
	END
END

GO
CREATE OR ALTER PROCEDURE DELETE_CLIENT AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name = 'Client';
	SET @start = SYSDATETIME();
	EXEC deleteClienti;
	SET @end = SYSDATETIME();
	PRINT('deleteClienti test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms');
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('deleteClienti Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end)
END

GO
CREATE OR ALTER PROCEDURE deleteMedicament AS
BEGIN

	DECLARE @rows INT = (SELECT TOP 1 TT.NoOfRows
		FROM TestTables TT INNER JOIN Tests T ON TT.TestID=T.TestID
		WHERE T.Name = 'deleteMedicament');

	DECLARE @current INT = 1;
	DECLARE @idMedicament INT = 40;
	WHILE @current <= @rows
	BEGIN

		DELETE FROM Medicament WHERE IDMedicament=@idMedicament;
		SET @idMedicament = @idMedicament + 1
		SET @current = @current + 1
	END
END

GO
CREATE OR ALTER PROCEDURE DELETE_MEDICAMENT AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @start DATETIME;
	DECLARE @end DATETIME;
	DECLARE @table INT;

	SELECT @table = TableID FROM Tables WHERE Name = 'Medicament';
	SET @start = SYSDATETIME();
	EXEC deleteMedicament;
	SET @end = SYSDATETIME();
	PRINT('deleteMedicament test time: ' + CAST(DATEDIFF(ms, @start, @end) AS VARCHAR) + ' ms');
	INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
		('deleteMedicament Test', @start, @end)
	INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
		(@@IDENTITY, @table, @start, @end)
END

-- Procedura principala pentru a executa toate testele in ordinea specificata in tabela TestTables
GO
CREATE OR ALTER PROCEDURE main AS
BEGIN
	DECLARE @TestID INT, @TableID INT, @NoOfRows INT, @Position INT;
	SELECT TOP 1 @TestID = TestID, @TableID = TableID, @NoOfRows = NoOfRows, @Position = Position
	FROM TestTables
	ORDER BY Position;
	WHILE @@ROWCOUNT > 0
	BEGIN
		-- Apeleaza procedura stocata aici
		--EXEC NumeleProcedurii @TestID, @TableID, @NoOfRows, @Position;
		-- in functie de prioritate 
		IF @Position = 1
		BEGIN
			EXEC INSERT_CLIENTI;
		END

		IF @Position = 2
		BEGIN
			EXEC INSERT_MEDICAMENT;
		END

		IF @Position = 3
		BEGIN
			EXEC INSERT_MEDICAMENTCLIENT;
			EXEC testRunViewProc;
		END

		IF @Position = 4
		BEGIN
			EXEC DELETE_MEDICAMENTCLIENT;
		END

		IF @Position = 5
		BEGIN
			EXEC DELETE_MEDICAMENT;
		END

		IF @Position = 6
		BEGIN
			EXEC DELETE_CLIENT;
		END

		SELECT TOP 1 @TestID = TestID, @TableID = TableID, @NoOfRows = NoOfRows, @Position = Position
		FROM TestTables
		WHERE Position > @Position
		ORDER BY Position;
	END;
END

EXEC main


SELECT * FROM View_Client
SELECT * FROM Medicament