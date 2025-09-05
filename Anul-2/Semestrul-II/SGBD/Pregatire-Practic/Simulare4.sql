use Simulare2 
go

CREATE TABLE dbo.Clienti (
    ClientID        INT           IDENTITY(1,1) PRIMARY KEY,
    Nume            NVARCHAR(100) NOT NULL,
    Oras            NVARCHAR(50)  NOT NULL,
    DataInregistrare DATE          NOT NULL
);

-- 2. Creare tabelă Comenzi
CREATE TABLE dbo.Comenzi (
    ComandaID       INT           IDENTITY(1,1) PRIMARY KEY,
    ClientID        INT           NOT NULL,
    DataComanda     DATE          NOT NULL,
    Suma            DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (ClientID) REFERENCES dbo.Clienti(ClientID)
);

INSERT INTO dbo.Clienti (Nume, Oras, DataInregistrare) VALUES
  ('Popescu Ion',   'București', '2024-11-10'),
  ('Ionescu Maria', 'Cluj',      '2025-01-05'),
  ('Georgescu Dan', 'Iași',      '2025-03-20'),
  ('Vasilescu Ana', 'Timișoara', '2025-04-15');

-- Inserare date de test în Comenzi
INSERT INTO dbo.Comenzi (ClientID, DataComanda, Suma) VALUES
  (1, '2025-05-02',  120.00),
  (1, '2025-05-18',  250.50),
  (2, '2025-05-20',   75.00),
  (3, '2025-05-22',  310.00),
  (3, '2025-05-23',  150.75),
  (4, '2025-05-25',   90.00);
 --Afişează comenzile plasate începând cu data de 20 mai 2025 împreună cu numele clientului.

 SELECT cl.Nume,c.DataComanda,c.Suma from Comenzi c INNER JOIN Clienti cl on cl.ClientID=c.ClientID where c.DataComanda>'2025-05-20'

 --Pentru fiecare client care are comenzi, afișează numărul total de comenzi și suma totală a comenzilor.
 SELECT cl.Nume ,COUNT(c.DataComanda) as 'Numar Comenzi' , SUM(c.Suma) as 'Suma totala' from Comenzi c INNER join Clienti cl on cl.ClientID=c.ClientID group by cl.Nume HAVING Count(c.DataComanda)>1


 SELECT COUNT(c.ClientID) as 'Produs Cartezian'
FROM dbo.Clienti AS c
CROSS JOIN dbo.Comenzi AS o;

CREATE NONCLUSTERED INDEX index_Nume on Clienti(Nume)

SELECT Nume from Clienti

