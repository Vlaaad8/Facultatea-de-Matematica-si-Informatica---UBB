use Fabrica
go

SELECT * FROM view_Recenzie

CREATE OR ALTER VIEW view_Recenzie
AS
	SELECT Rating,Descriere,IDRecenzie
	FROM Recenzie

CREATE NONCLUSTERED INDEX idx_rating
ON Recenzie (Rating)

CREATE NONCLUSTERED INDEX idx_descriere
ON Recenzie (Descriere)

CREATE NONCLUSTERED INDEX idx_descriere_rating
ON Recenzie (Rating, Descriere);



DECLARE @i INT = 1;  

WHILE @i <= 25  
BEGIN
    INSERT INTO Recenzie (Rating, Descriere,IDClient)
    VALUES (
        ROUND((RAND() * 4) + 1, 2),  
        CASE 
            WHEN @i % 4 = 0 THEN 'Excelent serviciu!'
            WHEN @i % 4 = 1 THEN 'Foarte multumit.'
            WHEN @i % 4 = 2 THEN 'Mediocru.'
            ELSE 'Nu recomand.'
        END  
    ,2);

    SET @i = @i + 1;  
END;