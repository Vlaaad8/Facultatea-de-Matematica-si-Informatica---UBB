use Problema1
go

CREATE INDEX index1 ON Clienti(nume)


select c.nume from Clienti c where c.nume like '%c%'