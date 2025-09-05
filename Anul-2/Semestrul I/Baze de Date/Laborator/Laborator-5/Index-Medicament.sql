use Fabrica 
go

SELECT * FROM View_Medicament
CREATE OR ALTER VIEW View_Medicament
AS
    SELECT Pret,Nume,IDMedicament
    FROM Medicament
    WHERE Pret > 25 AND Nume IN ('Paracetamol', 'Ibuprofen') 
 


CREATE NONCLUSTERED INDEX idx_Pret
ON Medicament (Pret)

CREATE NONCLUSTERED INDEX idx_Nume
ON Medicament (Nume)

CREATE NONCLUSTERED INDEX idx_np
ON Medicament (Pret,Nume);

DECLARE @i INT = 1;  

WHILE @i <= 100  
BEGIN
    INSERT INTO Medicament (Nume, Tip, Pret)
    VALUES (
        CASE 
            WHEN @i % 5 = 0 THEN 'Paracetamol'
            WHEN @i % 5 = 1 THEN 'Ibuprofen'
            WHEN @i % 5 = 2 THEN 'Amoxicilina'
            WHEN @i % 5 = 3 THEN 'Vitamina C'
            ELSE 'Aspirina'
        END,  
        CASE 
            WHEN @i % 3 = 0 THEN 'Antibiotic'
            WHEN @i % 3 = 1 THEN 'Analgezic'
            ELSE 'Supliment'
        END, 
        ROUND((RAND() * 400) + 10, 0)  
    );

    SET @i = @i + 1;  -- Increment counter
END;