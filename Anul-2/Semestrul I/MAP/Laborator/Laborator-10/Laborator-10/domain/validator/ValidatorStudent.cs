using System.Formats.Tar;
using Laborator_10.utils;

namespace Laborator_10.domain;

public class ValidatorStudent:IValidator<Student>
{
    public void Validate(Student entity)
    {SchoolNames schoolNames = new SchoolNames();
        if (schoolNames.ValidateSchoolNames(entity.GetSchoolName()) == false)
        {
            throw new ValidationException("Invalid school name");
        }
        
    }
}