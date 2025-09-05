namespace Laborator_10_InClasa;

public class Profesor:Person
{
    private string _subject;


    public Profesor(int age, string subject) : base(age)
    {
        _subject = subject;
    }

    public void Explain()
    {
        Console.WriteLine("Explanation begins");
        Console.WriteLine(_subject);
    }
}