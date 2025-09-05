use Fabrica
go

EXEC dbo.CRUD_Fabrica 'Constanta+','Suceava',4734,'Super tare',3

CREATE OR ALTER PROCEDURE CRUD_Fabrica 
    @nume varchar(50),
    @adresa varchar(50),
    @telefon INT,
    @descriere varchar(50),
    @numarRanduri INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- Validare input 
        EXEC dbo.ValidareFabrica @nume, @adresa, @telefon, @descriere;

        -- Insert 
        DECLARE @i INT = 0;
        WHILE @i < @numarRanduri
        BEGIN
            INSERT INTO FabricaMedicamente (Nume, Adresa, Telefon, Descriere)
            VALUES (@nume, @adresa, @telefon, @descriere);
            SET @i = @i + 1;
        END;

        -- Select
        SELECT * FROM FabricaMedicamente
        ORDER BY Nume;

        -- Update 
        UPDATE FabricaMedicamente
        SET Nume = 'Combinatul de la Suceava'
        WHERE Nume = @nume AND Telefon = @telefon;

       
        SELECT * FROM FabricaMedicamente;

        -- Delete 
        DELETE FROM FabricaMedicamente
        WHERE Telefon = @telefon AND Adresa = @adresa AND Descriere = @descriere;

      
        SELECT * FROM FabricaMedicamente;

        PRINT 'CRUD finalizat pentru tabela FabricaMedicamente';
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH;
END;







--Validare date Fabrica

CREATE OR ALTER PROCEDURE ValidareFabrica
	@nume varchar(50),
	@adresa varchar(50),
	@telefon INT,
	@descriere varchar(50)
AS 
BEGIN 
	DECLARE @ErrorMessages NVARCHAR(MAX) ='';

	IF dbo.Validate_Parameter_Is_Not_Null(@nume) = 0
	BEGIN
		SET @ErrorMessages += 'Numele fabricii nu poate fi null!' + CHAR(13) + CHAR(10);
	END

	IF dbo.Validate_Parameter_Is_Not_Null(@adresa) = 0
	BEGIN 
		SET @ErrorMessages += 'Adresa nu poate fi null!' + CHAR (13) + CHAR(10);
	END

	IF @telefon <=999 or @telefon >9999
	BEGIN
		SET @ErrorMessages += 'Numarul de telefon contine exact 4 cifre!' + CHAR(13) + CHAR(10);
	END

	IF dbo.Validate_Parameter_Is_Not_Null(@descriere) = 0
	BEGIN
		SET @ErrorMessages += 'Descrierea nu poate fi nula!'+ CHAR(13) + CHAR(10);
	END

	IF @ErrorMessages <> ''
	BEGIN
		RAISERROR(@ErrorMessages,16,1)
		RETURN 0;
	END
	RETURN 1;
END


--Verificare daca un sir este null
CREATE OR ALTER FUNCTION Validate_Parameter_Is_Not_Null(@sir VARCHAR(50))
	RETURNS INT
AS
BEGIN
	IF @sir IS NOT NULL
	BEGIN
		IF @sir=''
		BEGIN
			RETURN 0
		END
		RETURN 1
	END
	RETURN 0
END
