// See https://aka.ms/new-console-template for more information

using Seminar_11;

Angajat angajat = new Angajat("asa",20,KnowledgeLevel.Junior);
Angajat angajat2 = new Angajat("asabbasa",30,KnowledgeLevel.Senior);
Angajat angajat3 = new Angajat("asaasasa",15,KnowledgeLevel.Medium);
IValidator<Angajat> validator = new AngajatValidator();

InMemoryRepository<string,Angajat> angajatRepository = new InMemoryRepository<string,Angajat>(validator);
angajatRepository.Save(angajat);
angajatRepository.Save(angajat2);
angajatRepository.Save(angajat3);

IEnumerable<Angajat> all=angajatRepository.FindAll();
for (int i = 0; i < all.Count(); i++)
{
    Console.WriteLine(all.ElementAt(i));
}