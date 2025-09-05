namespace Laborator_10.service;

public interface Service<T>
{
    public IEnumerable<T> FindAll();
    public T FindOne(int id);
    public void Delete(int id);
}