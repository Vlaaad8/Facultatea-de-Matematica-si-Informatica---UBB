go
use Fabrica
go

CREATE TABLE LogHistory(
id INT PRIMARY KEY IDENTITY,
Place VARCHAR(50),
Operation VARCHAR(50),
Executed DATETIME
)

--Transaction Two

-----------------
--Dirty Reads
-----------------

--With Problem
SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED
BEGIN TRAN
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
WAITFOR DELAY '00:00:15'
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
COMMIT TRAN

--No Problem
SET TRANSACTION ISOLATION LEVEL READ COMMITTED
BEGIN TRAN
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
WAITFOR DELAY '00:00:15'
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
COMMIT TRAN

-----------------
--Non-Repeatable Reads
-----------------

--With Problem
SET TRANSACTION ISOLATION LEVEL READ COMMITTED
BEGIN TRAN
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
WAITFOR DELAY '00:00:15'
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
COMMIT TRAN

--No Problem
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
BEGIN TRAN
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
WAITFOR DELAY '00:00:15'
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
COMMIT TRAN

-----------------
--Phantom Reads
-----------------

--With Problem
SET TRAN ISOLATION LEVEL REPEATABLE READ
BEGIN TRAN
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
WAITFOR DELAY '00:00:15'
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
COMMIT TRAN

--No Problem
SET TRAN ISOLATION LEVEL SERIALIZABLE
BEGIN TRAN
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
WAITFOR DELAY '00:00:15'
SELECT * FROM Client
INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','SELECT',GETDATE())
COMMIT TRAN

-----------------
--Deadlock
-----------------

--With Problem
GO
CREATE OR ALTER PROC deadlock1 AS
BEGIN
	BEGIN TRAN
	UPDATE Angajat set Nume='Ovidiu Balan-Tran2' where IDAngajat=1
	INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Angajat','Update',GETDATE())
	WAITFOR DELAY '00:00:10'
	UPDATE Client set Nume='Ovidiu Balan-Tran2' where IDClient=3
	INSERT INTO LogHistory(Place,Operation,Executed) VALUES('Client','Update',GETDATE())
	COMMIT TRAN
END
GO

--No Problem
GO
CREATE OR ALTER PROC deadlock2 AS
BEGIN
	BEGIN TRAN
	UPDATE Client set Nume='Ovidiu Balan-Tran2' where IDClient=3
	WAITFOR DELAY '00:00:10'
	UPDATE Angajat set Nume='Ovidiu Balan-Tran2' where IDAngajat=1
	COMMIT TRAN
END
GO

--With Problem
EXEC deadlock1


--No Problem
EXEC deadlock2