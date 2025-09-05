using DefaultNamespace;

namespace MPP_CSharpProject.domain;

public class Employee:Entity<int>
{
    private string _user;
    private string _password;
    private string _firstName;
    private string _lastName;

    public Employee(string user, string password, string firstName, string lastName)
    {
        _user = user;
        _password = password;
        _firstName = firstName;
        _lastName = lastName;
    }
    public string GetUser() => _user;
    public string GetPassword() => _password;
    public string GetFirstName() => _firstName;
    public string GetLastName() => _lastName;

    public override string ToString()
    {
        return "Employee: " + "user: "+ _user+ ",password: "+_password+ ","+ _firstName + " " + _lastName;
    }
}