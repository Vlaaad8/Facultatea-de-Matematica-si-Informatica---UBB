use Fabrica
go

EXEC Crud_Angajat 1,'Alexandrovici Augustin','Manager',43,2000,4

CREATE OR ALTER PROCEDURE CRUD_Angajat
	@idFabrica INT,
	@nume varchar(50),
	@functie varchar(50),
	@varsta INT,
	@salariu INT,
	@numarRanduri INT
AS BEGIN
	SET NOCOUNT ON;
	BEGIN TRY
	EXEC dbo.ValidareAngajat @idFabrica,@nume,@functie,@varsta,@salariu -- Verificarea datelor

	--Incepem adaugarea
	DECLARE @i INT = 0
	WHILE @i < @numarRanduri
	BEGIN
		INSERT INTO Angajat(IDFabrica,Nume,Functie,Varsta,Salariu) VALUES 
		(@idFabrica,@nume,@functie,@varsta,@salariu)
		SET @i = @i+1
	END
	--Selectia
	SELECT * FROM Angajat
	--Update
	UPDATE Angajat SET Functie='Muncitor in promovare' WHERE Nume=@nume and Salariu=@salariu and Varsta=@varsta
	SELECT * FROM Angajat

	--Delete
	DELETE FROM Angajat WHERE Nume=@nume and Salariu=@salariu and Varsta=@varsta
	SELECT * FROM Angajat

        PRINT 'Operatiile CRUD realizate cu succes!';
    END TRY
    BEGIN CATCH
        -- Gestionare erori
        PRINT 'A aparut o eroare: ' + ERROR_MESSAGE();
    END CATCH
END



CREATE OR ALTER PROCEDURE ValidareAngajat
	@idFabrica INT,
	@nume varchar(50),
	@functie varchar(50),
	@varsta INT,
	@salariu INT
AS 
BEGIN
    DECLARE @ErrorMessages NVARCHAR(MAX) = '';
	IF dbo.Validate_Parameter_Is_Not_Null(@idFabrica) = 0 --Sa nu fie null!
    BEGIN
        SET @ErrorMessages += 'ID-ul fabricii nu poate fi vid!' + CHAR(13) + CHAR(10);
    END
	ELSE IF NOT EXISTS (SELECT 1 FROM FabricaMedicamente WHERE IDFabrica=@idFabrica) -- Sa existe!
	BEGIN
		SET @ErrorMessages +='ID-ul fabricii nu exista!' + CHAR(13) + CHAR(10);
	END

	IF dbo.Validate_Parameter_Is_Not_Null(@nume) = 0
	BEGIN
        SET @ErrorMessages += 'Numele nu poate fi vid!' + CHAR(13) + CHAR(10);
    END

	IF @varsta <= 0
    BEGIN
        SET @ErrorMessages += 'Varsta trebuie sa fie un numar pozitiv!' + CHAR(13) + CHAR(10);
    END

	IF @salariu <= 0
    BEGIN
        SET @ErrorMessages += 'Salariul trebuie sa fie un numar pozitiv!' + CHAR(13) + CHAR(10);
    END
	IF dbo.Validate_Parameter_Is_Not_Null(@functie) = 0
	BEGIN
        SET @ErrorMessages += 'Functia nu poate sa fie vida!' + CHAR(13) + CHAR(10);
    END
    IF @ErrorMessages <> ''
    BEGIN
        RAISERROR(@ErrorMessages, 16, 1);
        RETURN 0;
    END

    -- Fara erori
    RETURN 1;
END