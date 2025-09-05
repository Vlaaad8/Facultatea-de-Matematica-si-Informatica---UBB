use Fabrica
go

EXEC CRUD_Medicament 1, 'Agocalmin Forte','Naturist',100,4

CREATE OR ALTER PROCEDURE CRUD_Medicament
	@idFabrica INT,
	@nume varchar(50),
	@tip varchar (50),
	@pret INT,
	@numarRanduri INT
AS BEGIN 
	SET NOCOUNT ON;
	BEGIN TRY
	EXEC dbo.ValidareMedicament @idFabrica,@nume,@tip,@pret --Validare medicament

	--Insert
	DECLARE @i INT = 0
	while @i < @numarRanduri
	BEGIN
		INSERT INTO Medicament(IDFabrica,Nume,Tip,Pret) VALUES
		(@idFabrica,@nume,@tip,@pret)
		SET @i = @i+1
	END
	--Select
	SELECT * FROM Medicament
	--Update
	UPDATE Medicament 
	SET Tip='Analgezice calmante' WHERE Nume=@nume and Pret=@pret and IDFabrica=@idFabrica
	SELECT * FROM Medicament
	--Delete
	DELETE FROM Medicament WHERE Nume=@nume and Pret=@pret and IDFabrica=@idFabrica
	SELECT * FROM Medicament

        PRINT 'Operatiile CRUD realizate cu succes!';
    END TRY
    BEGIN CATCH
        -- Gestionare erori
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END







	CREATE OR ALTER PROCEDURE ValidareMedicament
	@idFabrica INT,
	@nume varchar(50),
	@tip varchar (50),
	@pret INT
AS BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';
	IF dbo.Validate_Parameter_Is_Not_Null(@idFabrica) = 0
    BEGIN
        SET @ErrorMessages += 'ID-ul fabricii nu poate fi vid!' + CHAR(13) + CHAR(10);
    END
	ELSE IF NOT EXISTS (SELECT 1 FROM FabricaMedicamente WHERE IDFabrica=@idFabrica)
	BEGIN
		SET @ErrorMessages +='ID-ul fabricii nu exista!' + CHAR(13) + CHAR(10);
	END
	IF dbo.Validate_Parameter_Is_Not_Null(@nume) = 0
	BEGIN
        SET @ErrorMessages += 'Numele nu poate fi vid!' + CHAR(13) + CHAR(10);
    END
	IF @pret <= 0
    BEGIN
        SET @ErrorMessages += 'Pretul trebuie sa fie un numar pozitiv!' + CHAR(13) + CHAR(10);
    END
	IF dbo.Validate_Parameter_Is_Not_Null(@tip) = 0
	BEGIN
        SET @ErrorMessages += 'Tipul nu poate sa fie vid!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END

    -- Fara erori
    RETURN 1;
END