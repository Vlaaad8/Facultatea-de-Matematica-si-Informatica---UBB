namespace Seminar_10;

public interface Container
{
    Task Remove();
    void Add(Task task);
    int Size();
    bool IsEmpty();
}