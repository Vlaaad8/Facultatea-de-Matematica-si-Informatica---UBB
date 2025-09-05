namespace Laborator_10.repository;

public interface IRepositoryDataBase<E>
{   E Save (E entity);
    E Delete (E entity);
    IEnumerable<E> GetAll();
    E FindOne(int id);
}