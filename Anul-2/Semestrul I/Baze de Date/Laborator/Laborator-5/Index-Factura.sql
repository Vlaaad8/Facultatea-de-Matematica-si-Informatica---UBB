use Fabrica
go
use Fabrica
go

SELECT * FROM view_Factura

CREATE OR ALTER VIEW view_Factura
AS
SELECT
	IDFacutura,DataEmitere,Suma
	FROM Factura 
	WHERE DataEmitere >= '2014-01-27'
	and Suma > 100

CREATE NONCLUSTERED INDEX idx_dataEmitere
ON Factura (DataEmitere)

CREATE NONCLUSTERED INDEX idx_Suma
ON Factura (Suma)

CREATE NONCLUSTERED INDEX idx_suma_dataEmitere
ON Factura (DataEmitere, Suma);


SELECT * FROM Factura

DECLARE @i INT=11;

WHILE @i<50
BEGIN
	INSERT INTO Factura(DataEmitere,Suma,Descriere,IDClient)
    VALUES (
        DATEADD(DAY, @i, '2014-01-01'),  
        (@i * 100) % 1000 + 100,'Achitat',1)
    
    SET @i = @i + 1;
END
