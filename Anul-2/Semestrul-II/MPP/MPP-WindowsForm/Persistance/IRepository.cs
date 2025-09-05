namespace DefaultNamespace;

public interface IRepository<TE>
{
    public TE? FindOne(int id);
    
    public TE Save(TE entity);
    
    public TE Delete(TE entity);
    
    public TE Update(TE entity);
    
    public IEnumerable<TE> GetAll();
    
}