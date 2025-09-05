USE Fabrica
go

SELECT * FROM MedicamentClient

EXEC CRUD_MedicamentClient 1, 1, 'Interesant', 1000,4

CREATE OR ALTER PROCEDURE CRUD_MedicamentClient
	@idMedicament INT,
	@idClient INT,
	@descriere varchar(50),
	@total INT,
	@numarRanduri INT
AS BEGIN
   SET NOCOUNT ON;

    BEGIN TRY
	EXEC dbo.ValidareMedicamentClient @idMedicament,@idClient,@descriere,@total
	DECLARE @i INT = 0
	WHILE @i < @numarRanduri
	BEGIN
		INSERT INTO MedicamentClient(IDMedicament,IDClient,Descriere,Total) VALUES
		(@idMedicament,@idClient,@descriere,@total)
		SET @i = @i+1
	END

	SELECT * FROM MedicamentClient

	UPDATE MedicamentClient SET Descriere='O comanda masiva' WHERE IDMedicament=@idMedicament AND IDClient=@idClient and Total=@total
	SELECT * FROM MedicamentClient

	DELETE FROM MedicamentClient WHERE IDMedicament=@idMedicament AND IDClient=@idClient and Total=@total
	SELECT * FROM MedicamentClient

        PRINT 'Operatiile CRUD realizate cu succes!';
    END TRY
    BEGIN CATCH
        -- Gestionare erori
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END

CREATE OR ALTER PROCEDURE ValidareMedicamentClient
	@idMedicament INT,
	@idClient INT,
	@descriere varchar(50),
	@total INT
AS BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';
	IF dbo.Validate_Parameter_Is_Not_Null(@idMedicament) = 0
    BEGIN
        SET @ErrorMessages += 'ID-ul medicamentului nu poate fi vid!' + CHAR(13) + CHAR(10);
    END
	ELSE IF NOT EXISTS (SELECT 1 FROM Medicament WHERE IDMedicament=@idMedicament)
	BEGIN
		SET @ErrorMessages +='ID-ul medicamentului  nu exista!' + CHAR(13) + CHAR(10);
	END
	IF dbo.Validate_Parameter_Is_Not_Null(@idClient) = 0
	BEGIN
        SET @ErrorMessages += 'ID-ul Clientului nu poate fi vid!' + CHAR(13) + CHAR(10);
    END
	ELSE IF NOT EXISTS (SELECT 1 FROM Client WHERE IDClient=@idClient)
	BEGIN
		SET @ErrorMessages +='ID-ul clientului  nu exista!' + CHAR(13) + CHAR(10);
	END

	IF dbo.Validate_Parameter_Is_Not_Null(@descriere) = 0
	BEGIN
        SET @ErrorMessages += 'Descriere nu poate fi vid!' + CHAR(13) + CHAR(10);
    END

	IF @total < 0
	BEGIN
		SET @ErrorMessages +='Total trebuie sa fie pozitiv' + CHAR(13) + CHAR(10);
	END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END

    -- Fara erori
    RETURN 1;
END