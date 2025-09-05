namespace Laborator_10.domain;

public interface IValidator<E>
{
    void Validate(E entity);
}