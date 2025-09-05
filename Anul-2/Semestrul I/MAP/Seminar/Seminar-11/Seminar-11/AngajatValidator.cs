namespace Seminar_11;

class AngajatValidator : IValidator<Angajat>
{
    public void Validate(Angajat e)
    {
        bool valid = true;
        if (valid == false)
        {
            throw new ValidationException("Obiectul nu e valid");
        }
    }
}