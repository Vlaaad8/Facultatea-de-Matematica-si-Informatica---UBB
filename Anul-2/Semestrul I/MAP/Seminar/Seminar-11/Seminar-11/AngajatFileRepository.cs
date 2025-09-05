namespace Seminar_11;

public class AngajatFileRepository:InFileRepository<string,Angajat>
{
    public AngajatFileRepository(IValidator<Angajat> vali, string fileName, CreateEntity<Angajat> createEntity) : base(vali, fileName, createEntity)
    {
    }
}