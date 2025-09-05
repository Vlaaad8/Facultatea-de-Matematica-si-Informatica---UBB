use Fabrica 
go


SELECT * FROM View_Client

CREATE OR ALTER VIEW View_Client
AS
    SELECT Varsta, Judet,IDClient
    FROM Client
    WHERE Varsta > 20;

CREATE NONCLUSTERED INDEX idx_Varsta
ON Client(Varsta)

CREATE NONCLUSTERED INDEX idx_Judet
ON Client(Judet)

CREATE NONCLUSTERED INDEX idx_vj
ON Client(Varsta,Judet);

DECLARE @i INT = 1;  
Drop table Client
WHILE @i <= 50  
BEGIN
    INSERT INTO Client (Nume, Varsta, Gen, Judet)
    VALUES (
        CASE 
            WHEN @i % 15 = 0 THEN 'Popescu Ion'
            WHEN @i % 15 = 1 THEN 'Ionescu Maria'
            WHEN @i % 15 = 2 THEN 'Vasilescu Ana'
            WHEN @i % 15 = 3 THEN 'Georgescu Mihai'
            WHEN @i % 15 = 4 THEN 'Dumitrescu Elena'
            WHEN @i % 15 = 5 THEN 'Stanescu Andrei'
            WHEN @i % 15 = 6 THEN 'Radulescu Cristina'
            WHEN @i % 15 = 7 THEN 'Marinescu Vlad'
            WHEN @i % 15 = 8 THEN 'Constantinescu Laura'
            WHEN @i % 15 = 9 THEN 'Mihailescu Gabriel'
            WHEN @i % 15 = 10 THEN 'Florescu Diana'
            WHEN @i % 15 = 11 THEN 'Dragomir Stefan'
            WHEN @i % 15 = 12 THEN 'Enache Alina'
            WHEN @i % 15 = 13 THEN 'Olteanu Adrian'
            ELSE 'Neagu Andreea'
        END,  
        ROUND((RAND() * 50) + 18, 0),  
        CASE 
            WHEN @i % 2 = 0 THEN 'barbat'
            ELSE 'femeie'
        END,  
        CASE 
            WHEN @i % 4 = 0 THEN 'Suceava'
            WHEN @i % 4 = 1 THEN 'Bucuresti'
            WHEN @i % 4 = 2 THEN 'Cluj'
            ELSE 'Timis'
        END  
    );

    SET @i = @i + 1;  
END;