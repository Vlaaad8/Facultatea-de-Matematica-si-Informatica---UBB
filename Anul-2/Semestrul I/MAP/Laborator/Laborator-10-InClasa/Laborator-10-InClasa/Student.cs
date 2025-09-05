namespace Laborator_10_InClasa;

public class Student:Person
{
    public Student(int age) : base(age)
    {
    }

    public void ShowAge()
    {
        Console.WriteLine("My age is:"+base.GetAge()+" years old");
    }
}