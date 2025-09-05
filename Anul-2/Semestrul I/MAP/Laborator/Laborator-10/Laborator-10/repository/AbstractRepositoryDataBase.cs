namespace Laborator_10.repository;

public abstract class AbstractRepositoryDataBase<E> : IRepositoryDataBase<E>
{
    private string _connectionString;
    public AbstractRepositoryDataBase(string connectionString)
    {
        _connectionString = connectionString;
    }
    
    public abstract E Save(E entity);
    
    public abstract E Delete(E entity);

    public abstract IEnumerable<E> GetAll();

    public abstract E FindOne(int id);

    public string ConnectionString => _connectionString;
}