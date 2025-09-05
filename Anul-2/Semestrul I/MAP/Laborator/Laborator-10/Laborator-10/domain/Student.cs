namespace Laborator_10.domain;

public class Student:Entity<int>
{
    private string _name;
    private string _schoolName;

    public Student(string name, string schoolName)
    {
        this._name = name;
        this._schoolName = schoolName;
    }

    public string GetName()
    {
        return _name;
    }

    public string GetSchoolName()
    {
        return _schoolName;
    }

    public override string ToString()
    {
        return $"Name: {_name}, SchoolName: {_schoolName}";
    }
}