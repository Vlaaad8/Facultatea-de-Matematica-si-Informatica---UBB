use Fabrica
go
INSERT INTO FabricaMedicamente(Nume,Adresa,Telefon,Descriere) VALUES ('Antibiotice A+','Muncipiul Iasi',456532,'Producem Penicilina din 1953')
INSERT INTO Manager(IDManager,Nume,Email,Parola) VALUES (1,'Vlad Balahura','vlad.balahura@yahoo.com','LaboratorBazeDate')

INSERT INTO Angajat(IDFabrica,Nume,Functie,Varsta,Salariu) VALUES (1,'Antonia Moga','Director Economic',21,3000), (1,'Dana Rusu','Personal Auxiliar',20,1900),(1,'Vlad Enea','Director Comercial',20,4000)
INSERT INTO Angajat(IDFabrica,Nume,Functie,Varsta,Salariu) VALUES (1,'Oprea Rares','Personal Auxiliar',20,2400)
INSERT INTO Angajat(IDFabrica,Nume,Functie,Varsta,Salariu) VALUES (1,'Oprea Horia','Personal Auxiliar',23,2400)


INSERT INTO Cheltuiala(Suma,Rating,Descriere,IDFabrica) VALUES (2000,4,'Reparatii instalatie electrica',1),(30000,4.8,'Aparatura noua',1),(10000,2.3,'Salarii Angajati',1)
INSERT INTO Cheltuiala(Suma,Rating,Descriere,IDFabrica) VALUES (1000000,5,'Modernizare parcare',1)

INSERT INTO Medicament(Nume,Tip,Pret,IDFabrica) VALUES ('Parasinus','Analgezic',20,1),('No-Spa Forte','Antispasmodic',10,1),('Penicilina','Antibiotic',300,1),('Doxiciclina','Antibiotic',20,1),('MIG 400','Analgezic',7,1)

INSERT INTO Client(Nume,Varsta,Gen,Judet) VALUES ('Antonia Moga',21,'femeie','Suceava')
INSERT INTO Client(Nume,Varsta,Gen,Judet) VALUES ('Angelia Manciu',50,'femeie','Suceava')
INSERT INTO Client(Nume,Varsta,Gen,Judet) VALUES ('Elvira Achihaei',72,'femeie','Suceava'),('Ovidiu Fraseniuc',25,'barbat','Bucuresti'),('Vasile Achihaei',75,'barbat','Botosani'),('Emil Boc',62,'barbat','Cluj')

INSERT INTO MedicamentClient(IDClient,IDMedicament) VALUES (1,1),(1,2),(1,4),(2,5),(2,1),(3,1),(3,3),(3,4),(3,5),(5,5),(5,4),(4,3)
INSERT INTO MedicamentClient(IDClient,IDMedicament) VALUES (6,1),(6,3)

INSERT INTO EfectAdvers(Nume,Simptom,Recomandare,IDMedicament) VALUES ('Reactii gastrointestinale','Varsaturi','Mancati inainte de administrare',3),('Reactii alergice','Mancarimi','Vedeti un doctor',5),('Efecte hepatice si renale','Toxicitate hepatica','Hidratare',1)
INSERT INTO EfectAdvers(Nume,Simptom,Recomandare,IDMedicament) VALUES ('Efecte cardiovasculare','Palpitatii','Odihna',4),('Sedare si confuzie','Confuzie','Repaus complet',2),('Toxicitate hematologica','Anemie','Evitati alcoolul',2)

INSERT INTO Factura (DataEmitere, Suma, Descriere, IDClient) 
VALUES 
    ('2024-10-29', 100, 'Reteta Lunara', 1),
	('2024-9-29', 230, 'Reteta Lunara', 1),
	('2024-8-29', 2330, 'Reteta Lunara', 1),
    ('2021-09-11', 1000, 'Tratament Gripa', 3),
    ('2023-09-10', 2304, 'Vaccin Covid', 2);
INSERT INTO Factura (DataEmitere, Suma, Descriere, IDClient) 
VALUES 	('2024-9-29', 230, 'Reteta Lunara', 1),
	('2024-8-29', 2330, 'Reteta Lunara', 1);

INSERT INTO Recenzie(Rating,Descriere,IDClient) VALUES (4.2,'Tratamentele au functionat',1),(3.5,'M-am simtit rau in continuare',5),(4.92,'Extraordinar',2)

SELECT Client.Nume AS Client, Medicament.Nume AS Medicament, EfectAdvers.Simptom 
FROM Client 
JOIN MedicamentClient ON Client.IDClient = MedicamentClient.IDClient 
JOIN Medicament ON MedicamentClient.IDMedicament = Medicament.IDMedicament 
JOIN EfectAdvers ON Medicament.IDMedicament = EfectAdvers.IDMedicament WHERE Client.Judet='Suceava';

SELECT FabricaMedicamente.Nume AS Fabrica, Manager.Nume AS Manager, SUM(Cheltuiala.Suma) AS TotalSuma 
FROM FabricaMedicamente 
JOIN Manager ON FabricaMedicamente.IDFabrica = Manager.IDManager 
JOIN Cheltuiala ON FabricaMedicamente.IDFabrica = Cheltuiala.IDFabrica 
GROUP BY FabricaMedicamente.Nume, Manager.Nume 
HAVING SUM(Cheltuiala.Suma) > 10;


SELECT * FROM FabricaMedicamente
SELECT * FROM Medicament
SELECT * FROM Client
SELECT * FROM EfectAdvers
SELECT * FROM Factura