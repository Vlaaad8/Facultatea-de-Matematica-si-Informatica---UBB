using System.ComponentModel.DataAnnotations;
using Laborator_10.utils;

namespace Laborator_10.domain;

public class ValidatorActivePlayer:IValidator<ActivePlayer>
{
    public void Validate(ActivePlayer entity)
    {
        if (entity == null)
        {
            throw new ValidationException(nameof(entity));
        }
    }
}