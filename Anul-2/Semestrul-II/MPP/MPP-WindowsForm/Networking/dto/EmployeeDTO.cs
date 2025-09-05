namespace Networking.dto;

[Serializable]
public class EmployeeDTO
{
    private int _id;
    private string _user;
    private string _password;
    private string _firstName;
    private string _lastName;

    public EmployeeDTO(int id, string user, string password, string firstName, string lastName)
    {
        _id = id;
        _user = user;
        _password = password;
        _firstName = firstName;
        _lastName = lastName;
    }

    public int Id
    {
        get => _id;
    }

    public string User
    {
        get => _user;
    }

    public string Password => _password;

    public string FirstName
    {
        get => _firstName;
    }

    public string LastName
    {
        get => _lastName;
    }
}