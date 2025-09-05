using System.Text;

namespace Laborator_10.utils;

public class SchoolNames
{private List<string> _schools=new List<string>();

    public SchoolNames()
    {
        _schools.Add("Liceul cu Program Sportiv Cluj-Napoca");
        _schools.Add("Liceul Teoretic \"Gheorghe Sincai\"");
        _schools.Add("Scoala Gimnaziala \"Iuliu Hatieganu\"");
        
    }

    public bool ValidateSchoolNames(string name)
    {
        return _schools.Contains(name);
    }

    public static string GenerateStudentName()
    {
        string name = GenerateRandomString(10);
        string lastName = GenerateRandomString(10);
        return name+" "+lastName;
    }
    
    public static string GenerateRandomString(int length)
    {
        const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++)
        {
            result.Append(chars[random.Next(chars.Length)]);
        }

        return result.ToString();
    }
    
    
}