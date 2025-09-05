namespace Laborator_12_InClsa;

public class Dog
{
    private string _name;
    private int _age;

    public Dog(string name, int age)
    {
        _name = name;
        _age = age;
    }

    public void Bark()
    {
        Console.WriteLine($"Dog {_name} is barking");
    }

    public void Eat()
    {
        Console.WriteLine($"Dog {_name} is eating");
    }
}