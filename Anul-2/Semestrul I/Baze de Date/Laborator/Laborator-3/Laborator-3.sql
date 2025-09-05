go
use Fabrica
go
--Imi creez o tabela pentru a gestiona versiunea 
CREATE TABLE Versiune
(VersiuneCurenta INT DEFAULT 0
)
--Modific coloana Angajat ,astfel incat aceasta sa nu fie null : PROCEDURA 1
CREATE PROCEDURE doProcedure1
AS
BEGIN
ALTER TABLE Angajat
ALTER COLUMN Salariu int NOT NULL
SELECT * FROM Angajat
END;

--Restabilesc efectestelei PROCEDURII 1
CREATE PROCEDURE undoProcedure1
AS 
BEGIN
ALTER TABLE Angajat
ALTER COLUMN Salariu INT
SELECT * FROM Angajat
END;

--Setez ca valoare implicita functia unui angajat ca fiind muncitor, cand vor fi introduse date noi, vor avea functia muncitor automat PROCEDURA 2
CREATE PROCEDURE doProcedure2
AS
BEGIN
ALTER TABLE Angajat
ADD CONSTRAINT df_Functie DEFAULT 'muncitor' for Functie
SELECT * FROM Angajat
END;
--sterg constrangerea ca fiecare angajat nou sa fie muncitor PROCEDURA 2
CREATE PROCEDURE undoProcedure2
AS
BEGIN
ALTER TABLE Angajat
DROP CONSTRAINT df_Functie
SELECT * FROM Angajat
END;
--Creez o noua tablea numita Plan Viitor PROCEDURA 3
create PROCEDURE doProcedure3
AS 
BEGIN
CREATE TABLE PlanViitor
(PlanID INT PRIMARY KEY IDENTITY,
Descriere varchar(50),
Cost INT,
IDFabrica INT FOREIGN KEY REFERENCES FabricaMedicamente(IDFabrica)
)
END;
--Sterg tabela Plan Viitor PROCEDURA 3
CREATE PROCEDURE undoProcedure3
AS 
BEGIN
DROP TABLE PlanViitor
END;
--Ii adaugam Managerului o noua coloana , aceea fiind Salariu PROCEDURA 4
create PROCEDURE doProcedure4
AS
BEGIN
ALTER TABLE Manager
ADD Salariu INT
SELECT * FROM Manager
END;
--Stergem coloana Salariu ce apartine Managerului PROCEDURA 4 
create PROCEDURE undoProcedure4
AS
BEGIN
ALTER TABLE Manager
drop COLUMN Salariu
SELECT * FROM Manager
END;

--Creem o contrangere de tip FOREIGN KEY de la Factura la Efect Advers PROCEDURA 5
create PROCEDURE doProcedure5
AS
BEGIN
ALTER TABLE Factura
ADD CONSTRAINT fk_ID_Factura FOREIGN KEY(IDClient) REFERENCES EfectAdvers(IDEfect)
END;

--Stergem constrangerea de la Factura la Efect Advers PROCEDURA 5
create PROCEDURE undoProcedure5
AS
BEGIN
ALTER TABLE Factura
DROP CONSTRAINT fk_ID_Factura
END;

--FUNCTIA MAIN
CREATE PROCEDURE main (@versiuneDorita INT)
AS
BEGIN
    DECLARE @versiuneCurenta INT;
	if (@versiuneDorita >5)
	BEGIN;
		PRINT 'NU AVEM ACEASTA VERSIUNE'
			RETURN;
		END;
    -- Extragem versiunea curenta; dacă nu exista, se presupune 0 ca versiune de inceput
    SELECT @versiuneCurenta = COALESCE((SELECT TOP 1 VersiuneCurenta FROM Versiune), 0);

    -- Dacă tabela este goală, introducem versiunea 0
    IF NOT EXISTS (SELECT 1 FROM Versiune)
    BEGIN
        INSERT INTO Versiune (VersiuneCurenta) VALUES (0);
        SET @versiuneCurenta = 0;
    END

	--Decidem daca facem upgrade sau downgrade
    IF @versiuneDorita > @versiuneCurenta
    BEGIN
   
        WHILE @versiuneCurenta < @versiuneDorita
        BEGIN
            SET @versiuneCurenta = @versiuneCurenta + 1;

            -- Apelăm procedurile de upgrade corespunzătoare fiecărei versiuni
			IF @versiuneCurenta = 1 
			BEGIN 
				PRINT 'Trecem la versiunea 1....'; 
				EXEC doProcedure1; 
			END;

			IF @versiuneCurenta = 2 
			BEGIN
				PRINT 'Trecem la versiunea 2....'; 
				EXEC doProcedure2;
			END;

		IF @versiuneCurenta = 3 
		BEGIN
			PRINT 'Trecem la versiunea 3....'; 
			EXEC doProcedure3;
		END;

		IF @versiuneCurenta = 4 
		BEGIN
			PRINT 'Trecem la versiunea 4....'; 
			EXEC doProcedure4;
		END;

		IF @versiuneCurenta = 5 
		BEGIN
			PRINT 'Trecem la versiunea 5....'; 
			EXEC doProcedure5;
		END;

            -- Actualizam versiunea curenta inn tabelul Versiune
            UPDATE Versiune SET VersiuneCurenta = @versiuneCurenta;
        END;
    END
    ELSE IF @versiuneDorita < @versiuneCurenta
    BEGIN
      
        WHILE @versiuneCurenta > @versiuneDorita
        BEGIN
		IF @versiuneCurenta = 5 
		BEGIN
			PRINT 'Retrogradăm de la versiunea 5....';
			EXEC undoProcedure5;
		END;

		IF @versiuneCurenta = 4 
		BEGIN
			PRINT 'Retrogradăm de la versiunea 4....';
			EXEC undoProcedure4;
		END;
	
		IF @versiuneCurenta = 3 
		BEGIN
			PRINT 'Retrogradăm de la versiunea 3....';
			EXEC undoProcedure3;
		END;

		IF @versiuneCurenta = 2 
		BEGIN
			PRINT 'Retrogradam de la versiunea 2....';
			EXEC undoProcedure2;
		END;

		IF @versiuneCurenta = 1 
		BEGIN
			PRINT 'Retrogradam de la versiunea 1....';
			EXEC undoProcedure1;
		END;


            -- Retrogradam versiunea curenta si o actualizam în tabel
            SET @versiuneCurenta = @versiuneCurenta - 1;
            UPDATE Versiune SET VersiuneCurenta = @versiuneCurenta;
        END;
    END

    -- Afisam mesajul final
    IF @versiuneCurenta = @versiuneDorita
        PRINT 'Baza de date este acum la versiunea dorita: ' + CAST(@versiuneDorita AS VARCHAR);
    ELSE
        PRINT 'Nu s-a putut ajunge la versiunea dorita.';
END;


