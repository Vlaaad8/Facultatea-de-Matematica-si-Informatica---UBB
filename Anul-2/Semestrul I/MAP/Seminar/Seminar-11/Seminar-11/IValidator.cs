namespace Seminar_11;

public interface IValidator<E>
{
    void Validate(E e);
}