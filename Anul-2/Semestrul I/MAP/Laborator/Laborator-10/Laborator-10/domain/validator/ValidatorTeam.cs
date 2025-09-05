using System.ComponentModel.DataAnnotations;
using Laborator_10.utils;

namespace Laborator_10.domain;

public class ValidatorTeam:IValidator<Team>
{
    public void Validate(Team entity)
    {TeamNames validator=new TeamNames();
        if (validator.ValidateTeamNames(entity.Name) == false)
        {
            throw new ValidationException("Invalid team name");
        }
    }
}