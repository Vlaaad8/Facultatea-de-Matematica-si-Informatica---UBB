create database Simulare2
use Simulare2
go


CREATE TABLE Om(
idOm INT PRIMARY KEY IDENTITY,
Nume varchar(50),
Tara varchar(50),
Copii INT)

CREATE TABLE Caine(
idCaine INT PRIMARY KEY IDENTITY,
Varsta INT,
Nume VARCHAR(50),
Rasa VARCHAR(50),
idOm INT FOREIGN KEY REFERENCES Om(idOm))

-- Inserăm câteva persoane în tabelul Om
INSERT INTO Om (Nume, Tara, Copii) VALUES
  ('Ion Popescu',   'România', 2),
  ('Maria Ionescu', 'Moldova', 1),
  ('Andrei Georgescu','Bulgaria',0),
  ('Elena Marinescu','România',3),
  ('Vlad Vasilescu','Ungaria',2);

  INSERT INTO Om (Nume, Tara, Copii) VALUES
  ('Ion Popescu2',   'Rusia', 200)
  INSERT INTO Caine (Varsta, Nume,      Rasa,        idOm) VALUES
  (4,       'Rexy',      'Ciobănesc',   1)


  SELECT * FROM Om
-- Să presupunem că ID-urile generate automat în Om sunt 1,2,3,4,5
-- Acum inserăm câini în tabelul Caine, legându-i de stăpâni prin idOm
INSERT INTO Caine (Varsta, Nume,      Rasa,        idOm) VALUES
  (4,       'Rex',      'Ciobănesc',   1),
  (2,       'Bella',    'Labrador',    1),
  (7,       'Max',      'Golden',      2),
  (1,       'Luna',     'Beagle',      3),
  (5,       'Coco',     'Pudel',       4),
  (3,       'Rocky',    'Bulldog',     4),
  (6,       'Daisy',    'Teckel',      5);


--Numărul de câini pe fiecare proprietar
--Afişează pentru fiecare persoană (Om) numele, ţara şi numărul total de câini pe care îi deţine.
SELECT o.Nume,o.Tara,count(o.nume) AS 'Numar de caini' FROM Om o INNER JOIN Caine c on c.idOm=o.idOm group by o.Nume,o.Tara

--Vârsta medie a câinilor după ţara proprietarului
--Afişează, pentru fiecare ţară, vârsta medie a câinilor deţinuţi de persoanele din acea ţară.

SELECT DISTINCT(o.Tara),AVG(c.Varsta) as 'Varsta Medie' from Om o INNER JOIN Caine c ON o.idOm=c.idOm group by o.tara

--Total copii pe ţară pentru proprietarii cu câini
--Afişează, pentru fiecare ţară (Om.Tara), suma tuturor copiilor (Copii) ai proprietarilor care deţin cel puţin un câine.
SELECT o.Tara, AVG(o.Copii) AS 'Medie Copii' from Om o INNER JOIN Caine c ON c.idOm=o.idOm group by o.tara 


--Suma şi media vârstelor câinilor pentru proprietarii cu mai mult de un câine
--Afişează, pentru fiecare persoană care are mai mult de un câine, numele proprietarului (Om.Nume), suma (SUM) şi media (AVG) vârstelor câinilor pe care îi deţine, având HAVING COUNT(*) > 1.

SELECT o.Nume,AVG(c.Varsta) as 'Media Varsta Caine',Count(c.Varsta) as 'Numar Caini' from Om o INNER JOIN Caine c ON o.idOm=c.idOm group by o.nume
  SELECT * FROM Om

 --Numărul de rase distincte deținute de fiecare proprietar
--Pentru fiecare persoană (Om.Nume), afișează câte rase diferite de câini deține.
SELECT o.Nume,Count(DISTINCT c.Rasa) as 'Numar Rase diferite' FROM Om o INNER JOIN Caine c ON c.idOm=o.idOm group by o.Nume

CREATE NONCLUSTERED INDEX idx_Rasa on Caine(Rasa)
SELECT Rasa,Varsta from Caine order by Rasa