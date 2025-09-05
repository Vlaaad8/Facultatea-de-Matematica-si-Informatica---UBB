namespace Laborator_10_InClasa;

public class Person
{
    private int age;

    public Person(int age)
    {
        this.age = age;
    }

    public void Greet()
    {
        Console.WriteLine("Hello");
    }

    public void SetAge(int n)
    {
        age = n;
    }

    public int GetAge()
    {
        return age;
    }
}