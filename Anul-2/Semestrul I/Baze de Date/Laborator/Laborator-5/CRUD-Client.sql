use Fabrica
go

EXEC CRUD_Client 'Popescu Severin Marcel', 40, 'barbat','Suceava', 10

CREATE OR ALTER PROCEDURE CRUD_Client
	@nume varchar(50),
	@varsta INT,
	@gen varchar(50),
	@judet varchar(50),
	@numarRanduri INT
AS
BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
		EXEC dbo.ValidareClient @nume,@varsta,@gen,@judet --Validarea datelor

		--Inserarea datelor in tabel
		DECLARE @i INT = 0;
		WHILE @i < @numarRanduri
		BEGIN
			INSERT INTO Client(Nume,Varsta,Gen,Judet) VALUES
			(@nume,@varsta,@gen,@judet)
			SET @i = @i+1
		END
		--Selectia
		SELECT * FROM Client
		--Update
		UPDATE Client SET Judet='Caras Severin' where Nume=@nume and Varsta=@varsta
		SELECT * FROM Client
		--Delete
		DELETE FROM Client WHERE Nume=@nume and Varsta=@varsta and Gen=@gen
		SELECT * FROM Client

        PRINT 'CRUD-Client realizat cu succes!';
    END TRY
    BEGIN CATCH
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE(); --Afisare potentiale erori
    END CATCH
END

--Validarea genului
CREATE OR ALTER FUNCTION ValidareGen(@gen varchar(50))
	RETURNS INT
AS
BEGIN
	IF @gen='barbat' or @gen='femeie'
	BEGIN
		RETURN 1
	END
	RETURN 0
END

--Validarea datelor
CREATE OR ALTER PROCEDURE ValidareClient
	@nume varchar(50),
	@varsta INT,
	@gen varchar(50),
	@judet varchar(50)
AS BEGIN 
	DECLARE @ErrorMessages NVARCHAR(MAX) = '';

	IF dbo.Validate_Parameter_Is_Not_Null(@nume) = 0
	    BEGIN
        SET @ErrorMessages += 'Numele nu poate fi vid!' + CHAR(13) + CHAR(10);
    END

	IF @varsta <= 0
    BEGIN
        SET @ErrorMessages += 'Varsta trebuie sa fie un numar pozitiv!' + CHAR(13) + CHAR(10);
    END

	IF (dbo.Validate_Parameter_Is_Not_Null(@gen) = 0 or dbo.ValidareGen(@gen) = 0)
	BEGIN
		SET @ErrorMessages += 'Genul trebuie sa fie barbat sau femeie!' + CHAR(13) + CHAR(10);
	END

	IF dbo.Validate_Parameter_Is_Not_Null(@judet) = 0
	BEGIN
        SET @ErrorMessages += 'Judetul nu poate fi vid!' + CHAR(13) + CHAR(10);
    END

    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END

    -- Fara erori
    RETURN 1;
END


