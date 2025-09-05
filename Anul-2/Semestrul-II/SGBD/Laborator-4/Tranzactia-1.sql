go
use Fabrica
go
--Transaction One
-----------------
--Dirty Reads
-----------------
BEGIN TRANSACTION 
UPDATE Client SET Varsta=57 where IDClient=1
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','Update',GETDATE())
WAITFOR DELAY '00:00:10'
ROLLBACK TRANSACTION

-----------------
--Non-Repeatable Reads
-----------------
INSERT INTO Client(Nume,Varsta,Gen,Judet) VALUES('Benedict XVI',100,'barbat','Germania')
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','Insert',GETDATE())
BEGIN TRANSACTION 
WAITFOR DELAY '00:00:10'
UPDATE Client SET Varsta=105 where Nume='Benedict XVI'
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','Update',GETDATE())
COMMIT TRAN

-----------------
--Phantom Reads
-----------------
BEGIN TRAN
WAITFOR DELAY '00:00:10'
INSERT INTO Client(Nume,Varsta,Gen,Judet) VALUES('John Paul I',55,'barbat','Italia')
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','Insert',GETDATE())
COMMIT TRAN

-----------------
--Deadlock
-----------------
GO
CREATE OR ALTER PROC deadlock AS
BEGIN
	BEGIN TRAN
	UPDATE Client set Nume='Ovidiu Balan-Tran1' where IDClient=3
	INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','Update',GETDATE())
	WAITFOR DELAY '00:00:10'
	UPDATE Angajat set Nume='Ovidiu Balan-Tran1' where IDAngajat=1
	INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Angajat','Update',GETDATE())
	COMMIT TRAN
END
GO

EXEC deadlock
