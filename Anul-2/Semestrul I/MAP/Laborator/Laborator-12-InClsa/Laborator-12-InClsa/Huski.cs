namespace Laborator_12_InClsa;

public class Huski:Dog
{
    private int speed;


    public Huski(string name, int age, int speed) : base(name, age)
    {
        this.speed = speed;
    }

    public void Run()
    {
        Console.WriteLine("Huski running...");
    }
}