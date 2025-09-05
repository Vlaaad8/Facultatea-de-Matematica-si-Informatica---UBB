use Fabrica
go
-- 1.Folosim WHERE , informatii din mai mult de 2 tabele , relatie m-n
-- Afiseaza numele clientului,medicamentului si efectul advers al acestuia, conditie: clientul sa fie din judetul Suceava
SELECT Client.Nume AS Client, Medicament.Nume AS Medicament, EfectAdvers.Simptom 
FROM Client 
JOIN MedicamentClient ON Client.IDClient = MedicamentClient.IDClient 
JOIN Medicament ON MedicamentClient.IDMedicament = Medicament.IDMedicament 
JOIN EfectAdvers ON Medicament.IDMedicament = EfectAdvers.IDMedicament WHERE Client.Judet='Suceava';

-- 2.Folosim HAVING, GROUP BY , relatie m-n, mai mult de 2 tabele
--Afiseaza clientul, suma media a facturilor lui si suma pe care o plateste pentru medicamente
--Conditie: Media facturilor sa fie mai mare ca 900
SELECT Client.Nume as Client, AVG(Factura.Suma) as Facturi, SUM(Medicament.Pret) as Cheltuieli
FROM CLIENT
JOIN MedicamentClient ON Client.IDClient = MedicamentClient.IDClient 
JOIN Medicament ON MedicamentClient.IDMedicament = Medicament.IDMedicament 
JOIN Factura on Factura.IDClient=Client.IDClient
GROUP BY Client.Nume
HAVING AVG(Factura.Suma) >900

-- 3.Folosim GROUP BY, informatii din mai mult de 2 tabele, m-n
--Afiseaza comenzile de medicamente ale angajatilor care sunt si clienti
SELECT Angajat.Nume AS Nume,
       FabricaMedicamente.Nume AS Fabrica,
       Medicament.Nume AS Medicament
FROM Angajat
JOIN Client ON Client.Nume = Angajat.Nume
JOIN FabricaMedicamente ON Angajat.IDFabrica = FabricaMedicamente.IDFabrica
JOIN MedicamentClient ON Client.IDClient = MedicamentClient.IDClient
JOIN Medicament ON MedicamentClient.IDMedicament = Medicament.IDMedicament
GROUP BY Angajat.Nume, FabricaMedicamente.Nume,Medicament.Nume;

-- 4.Folosim WHERE,informatii din mai multe tabele
--Afiseaza angajatii care sunt Personal Auxiliar si care sunt angajati la o fabrica care contine in nume A+
SELECT Angajat.Nume AS Nume,Manager.Nume as Manager,FabricaMedicamente.Nume as Fabrica,
       Angajat.Functie AS Functie
FROM Angajat
JOIN FabricaMedicamente ON FabricaMedicamente.IDFabrica=Angajat.IDFabrica
JOIN Manager ON Manager.IDManager=FabricaMedicamente.IDFabrica
WHERE Angajat.Functie='Personal Auxiliar' and FabricaMedicamente.Nume LIKE '%A+%'

-- 5.Folosim DISTINCT,GROUP BY,HAVING,mai mult de 2 tabele
--Afisam clientii care au suma medicamentelor mai mare ca 10
SELECT DISTINCT Client.Nume AS Client, SUM(Medicament.Pret) as Cheltuielti_Medicamente,Client.Judet as Judet
from Client
JOIN MedicamentClient ON Client.IDClient = MedicamentClient.IDClient 
JOIN Medicament ON MedicamentClient.IDMedicament = Medicament.IDMedicament 
JOIN Factura on Factura.IDClient=Client.IDClient
GROUP BY Client.Nume,Factura.Suma,Client.Judet
HAVING SUM(Medicament.Pret) >10;

-- 6.Folosim DISTINCT, WHERE 
-- Afiseaza salariul si functia pentru fiecare categorie, in care salariul e mai mare ca 1000
SELECT DISTINCT Angajat.Salariu AS Salariu,Angajat.Functie AS Functie
from Angajat
where Angajat.Salariu>1000;

--7 WHERE - mai mult de 2 tabele, m-n
--Afiseaza acei clienti a caror efecte adverse sunt Efecte hepatice si renale
SELECT Client.Nume as Client, EfectAdvers.Nume as Efect, Medicament.Pret as Cost
from Client
JOIN MedicamentClient ON Client.IDClient = MedicamentClient.IDClient 
JOIN Medicament ON MedicamentClient.IDMedicament = Medicament.IDMedicament 
JOIN EfectAdvers ON Medicament.IDMedicament = EfectAdvers.IDMedicament
WHERE EfectAdvers.Nume='Efecte hepatice si renale';

--8 WHERE,GROUP BY,informatii din mai multe tabele, relatie m-n
--Afiseza acei angajati si medicamentele care au in nume Oprea si medicamentele 
SELECT 
    FabricaMedicamente.Nume AS Nume_Fabrica, 
    Medicament.Nume AS Nume_Medicament, 
    Angajat.Nume AS Nume_Angajat,
    AVG(Angajat.Salariu) AS Salariu_Mediu
FROM FabricaMedicamente 
JOIN Medicament ON FabricaMedicamente.IDFabrica = Medicament.IDFabrica 
JOIN Angajat ON FabricaMedicamente.IDFabrica = Angajat.IDFabrica
WHERE Medicament.Nume = 'Parasinus' and Angajat.Nume LIKE '%Oprea%'
GROUP BY FabricaMedicamente.Nume, Medicament.Nume, Angajat.Nume;

--9. WHERE, mai mult de 2 tabele, relatie m-n
--Afisez clientii,medicamentele si efetele adverse a caror varsta e mai mica de 30 de ani si contin in nume ina
SELECT 
    Client.Nume AS Nume_Client, 
    Medicament.Nume AS Nume_Medicament, 
    EfectAdvers.Simptom AS Simptom_Efect
FROM Client 
JOIN MedicamentClient ON Client.IDClient = MedicamentClient.IDClient 
JOIN Medicament ON MedicamentClient.IDMedicament = Medicament.IDMedicament 
JOIN EfectAdvers ON Medicament.IDMedicament = EfectAdvers.IDMedicament
WHERE Client.Varsta < 30 and Medicament.Nume LIKE '%ina';


--10 WHERE,GROUP BY,HAVING,informatii din mai mult de 2 tabele
--Afisez angajatii si cheltuielile fabricii a caror nume contine ea si cheltuieli mai mari ca 5000
SELECT 
    Angajat.Nume AS Nume_Angajat,
    FabricaMedicamente.Nume AS Nume_Fabrica,
    SUM(Cheltuiala.Suma) AS Total_Cheltuieli
FROM Angajat
JOIN FabricaMedicamente ON Angajat.IDFabrica = FabricaMedicamente.IDFabrica
JOIN Cheltuiala ON FabricaMedicamente.IDFabrica = Cheltuiala.IDFabrica
WHERE Angajat.Nume LIKE '%ea%'
GROUP BY Angajat.Nume, FabricaMedicamente.Nume
HAVING SUM(Cheltuiala.Suma) > 5000


