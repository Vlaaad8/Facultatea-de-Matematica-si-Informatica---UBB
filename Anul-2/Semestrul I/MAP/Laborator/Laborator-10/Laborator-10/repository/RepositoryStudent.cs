using Laborator_10.domain;
using Npgsql;
using NpgsqlTypes;

namespace Laborator_10.repository;

public class RepositoryStudent : AbstractRepositoryDataBase<Student>
{
    public RepositoryStudent(string connectionString) : base(connectionString)
    {
    }
    
    public override Student Save(Student entity)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "INSERT INTO student(name,schoolname) VALUES (@name,@schoolName)";
        using var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@name", entity.GetName());
        command.Parameters.AddWithValue("@schoolName", entity.GetSchoolName());
        if (command.ExecuteNonQuery() == 1)
        {
            return null;
        }

        return entity;
    }

    public override Student Delete(Student entity)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "DELETE FROM student WHERE id = @id";
        using var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@id",entity.GetId());
        if (command.ExecuteNonQuery() > 0)
        {
            return entity;
        }
        return null;
    }   

    public override IEnumerable<Student> GetAll()
    {   List<Student> students = new List<Student>();
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM student";
        using var command = new NpgsqlCommand(query, connection);
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            int id = reader.GetInt32(0);
            string name =reader.GetString(1);
            string schoolName = reader.GetString(2);
            Student student = new Student(name, schoolName);
            student.SetId(id);
            students.Add(student);
        }
        return students;
    }

    public override Student FindOne(int id)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM student WHERE id = @id";
        using var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@id", id);
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            int idTemp = reader.GetInt32(0);
            if (idTemp == id)
            {
                Student student = new Student(reader.GetString(1), reader.GetString(2));
                student.SetId(idTemp);
                return student;
                
            }
        }

        return null;
    }
}