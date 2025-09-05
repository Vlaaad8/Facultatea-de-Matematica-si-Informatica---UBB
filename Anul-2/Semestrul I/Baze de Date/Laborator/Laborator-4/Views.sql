use Fabrica
DELETE FROM Tables
SET IDENTITY_INSERT Tables ON;
INSERT INTO Tables(TableID, Name) VALUES
	(1, 'Client'),
	(2, 'Medicament'),
	(3, 'MedicamentClient');
SET IDENTITY_INSERT Tables OFF;

--Imi "clonez" tabele pe care lucrez
--O tabela cu PK, no FK
CREATE OR ALTER VIEW View_Client AS
SELECT 
    Nume AS Denumire,
    Varsta AS Varsta,
    Judet AS Locatie
FROM Client;

--O tabela cu PK SI FK
CREATE OR ALTER VIEW View_Medicament AS
SELECT
    m.Nume AS Nume,
    m.Tip AS Tip,
    m.Pret AS Pret,
    f.Nume AS Fabrica
FROM Medicament m
INNER JOIN FabricaMedicamente f ON f.IDFabrica = m.IDFabrica;

--Tabela cu realtie la m-n CU 2 PK si GROUP BY
CREATE OR ALTER VIEW View_MedicamentClient AS
SELECT m.Nume as NumeMedicament,
		c.nume as NumeClient
		FROM Medicament m
		INNER JOIN MedicamentClient mc ON m.IDMedicament=mc.IDMedicament
		INNER JOIN Client c ON c.IDClient=mc.IDClient
		GROUP BY m.Nume,c.Nume;

SET IDENTITY_INSERT Views ON;

INSERT INTO Views(ViewID, Name) VALUES
    (1, 'View_Client'),
    (2, 'View_Medicament'),
    (3, 'View_MedicamentClient');
SET IDENTITY_INSERT Views OFF;

-- Testele pe care le am (ca si optiuni) pentru tabele
SET IDENTITY_INSERT Tests ON;
DELETE FROM Tests;
INSERT INTO Tests(TestID, Name) VALUES
    (1, 'selectView'),
    (2, 'insertClienti'),
    (3, 'insertMedicament'),
    (4, 'insertMedicamentClient'),
    (5, 'deleteClienti'),
    (6, 'deleteMedicament'),
    (7, 'deleteMedicamentClient');
SET IDENTITY_INSERT Tests OFF;

-- Testele pentru View
INSERT INTO TestViews (TestID, ViewID) VALUES
    (1, 1),  -- Test for View_Client
    (1, 2),  -- Test for View_Medicament
    (1, 3);  -- Test for View_MedicamentClient

-- Testele pentru tabele in ordinea corespunzatoare
SELECT * FROM TestTables;
INSERT INTO TestTables(TestID, TableID, NoOfRows, Position) VALUES
    (2, 1, 10000, 1),  -- Test for Client table
    (3, 2, 10000, 2),  -- Test for Medicament table
    (4, 3, 10000, 3),  -- Test for MedicamentClient table
    (6, 2, 10000, 4),  -- Test for Medicament table 
    (7, 3, 10000, 5),  -- Test for MedicamentClient table 
    (5, 1, 10000, 6);  -- Test for Client table 

SELECT * FROM View_Client;
SELECT * FROM View_Medicament;
SELECT * FROM View_MedicamentClient;

SELECT * FROM Tables;
SELECT * FROM Views;
SELECT * FROM Tests;
SELECT * FROM TestTables;
SELECT * FROM TestRunTables;
SELECT * FROM TestRuns;
SELECT * FROM TestRunViews;