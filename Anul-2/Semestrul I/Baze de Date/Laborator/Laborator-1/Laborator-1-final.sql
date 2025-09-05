Create database Fabrica
go
use Fabrica
go

CREATE TABLE FabricaMedicamente
(IDFabrica INT PRIMARY KEY IDENTITY,
Nume varchar(50) not null,
Adresa varchar(50) not null,
Telefon int,
Descriere varchar(50) not null
)

CREATE TABLE Manager
(IDManager INT FOREIGN KEY REFERENCES FabricaMedicamente(IDFabrica),
Nume varchar(50) not null,
Email varchar(50) not null,
Parola varchar(50) not null,
CONSTRAINT pk_Manager PRIMARY KEY(IDManager)
)

CREATE TABLE Cheltuiala
(IDCheltuiala INT PRIMARY KEY IDENTITY,
Suma int,
Rating int,
Descriere varchar(50) not null,
IDFabrica INT FOREIGN KEY REFERENCES FabricaMedicamente(IDFabrica)
)

CREATE TABLE Medicament
(IDMedicament INT PRIMARY KEY IDENTITY,
Nume varchar(50) not null,
Tip varchar(50) not null,
Pret int,
IDFabrica INT FOREIGN KEY REFERENCES FabricaMedicamente(IDFabrica)
)
CREATE TABLE EfectAdvers(
IDEfect INT PRIMARY KEY IDENTITY,
Nume varchar(50) not null,
Simptom varchar(50) not null,
Recomandare varchar(50) not null,
IDMedicament INT FOREIGN KEY REFERENCES Medicament(IDMedicament)
)
CREATE TABLE Client
(IDClient INT PRIMARY KEY IDENTITY,
Nume varchar(50) not null,
Varsta int,
Gen varchar(50) CHECK (Gen='barbat' or Gen='femeie'),
Judet varchar(50) not null
)
CREATE TABLE MedicamentClient
(IDMedicament INT FOREIGN KEY REFERENCES Medicament(IDMedicament),
IDClient INT FOREIGN KEY REFERENCES Client(IDClient),
Descriere varchar(50),
Total INT,
)


CREATE TABLE Angajat
(IDAngajat INT PRIMARY KEY IDENTITY,
Nume varchar(50) not null,
Functie varchar(50) not null,
Varsta int,
Salariu int,
IDFabrica INT FOREIGN KEY REFERENCES FabricaMedicamente(IDFabrica)
)

CREATE TABLE Factura(
IDFacutura INT PRIMARY KEY IDENTITY,
DataEmitere DATE,
Suma INT,
Descriere varchar(50) not null,
IDClient INT FOREIGN KEY REFERENCES Client(IDClient)
)
