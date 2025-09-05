using Laborator_10.domain;
using Npgsql;
using NpgsqlTypes;

namespace Laborator_10.repository;

public class RepositoryTeam : AbstractRepositoryDataBase<Team>
{
    public RepositoryTeam(string connectionString) : base(connectionString)
    {
    }

    public override Team Save(Team entity)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "INSERT INTO team(name) VALUES(@name)";
        var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@name", entity.Name);
        if (command.ExecuteNonQuery() > 0)
        {
            return null;
        }

        return entity;
    }

    public override Team Delete(Team entity)
    {
        using var connection=new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "DELETE FROM team WHERE id = @id";
        var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue(entity.GetId());
        if (command.ExecuteNonQuery() > 0)
        {
            return entity;
        }
        return null;
    }

    public override IEnumerable<Team> GetAll()
    {
        List<Team> teams = new List<Team>();
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM team";
        var command = new NpgsqlCommand(query, connection);
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            int id = reader.GetInt32(0);
            string name = reader.GetString(1);
            Team team = new Team(name);
            team.SetId(id);
            teams.Add(team);

        }
        return teams;
    }

    public override Team FindOne(int id)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM team WHERE id = @id";
        var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@id", id);
        using var reader = command.ExecuteReader();
        reader.Read();
        string name= reader.GetString(1);
        Team team = new Team(name);
        team.SetId(id);
        return team;
            
        
    }
}