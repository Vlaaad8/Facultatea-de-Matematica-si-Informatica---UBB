using System.Formats.Tar;
using Laborator_10.domain;
using Npgsql;

namespace Laborator_10.repository;

public class RepositoryPlayer : AbstractRepositoryDataBase<Player>
{
    public RepositoryPlayer(string connectionString) : base(connectionString)
    {
    }

    public override Player Save(Player entity)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "INSERT INTO player(name,schoolname,idTeam,nameTeam) VALUES(@name,@schoolname,@idTeam,@nameTeam)";
        using var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@name", entity.GetName());
        command.Parameters.AddWithValue("@schoolName", entity.GetSchoolName());
        command.Parameters.AddWithValue("@idTeam", entity.Teams.GetId());
        command.Parameters.AddWithValue("@nameTeam", entity.Teams.Name);
        if (command.ExecuteNonQuery() == 1)
        {
            return null;
        }

        return entity;
    }

    public override Player Delete(Player entity)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "DELETE FROM player WHERE id=@id";
        using var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@id", entity.GetId());
        if (command.ExecuteNonQuery() == 1)
        {
            return entity;
        }

        return null;
    }

    public override IEnumerable<Player> GetAll()
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM player";
        using var command = new NpgsqlCommand(query, connection);
        using var reader = command.ExecuteReader();
        var result = new List<Player>();
        while (reader.Read())
        {
            int id = reader.GetInt32(0);
            string name = reader.GetString(1);
            string schoolName = reader.GetString(2);
            int idTeam = reader.GetInt32(3);
            string nameTeam = reader.GetString(4);
            Team team = new Team(nameTeam);
            team.SetId(idTeam);
            Player player = new Player(name, schoolName, team);
            player.SetId(id);
            result.Add(player);
        }

        return result;
    }

    public override Player FindOne(int id)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM player WHERE id=@id";
        using var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@id", id);
        using var reader = command.ExecuteReader();
        var result = reader.Read();
        int idPlayer = reader.GetInt32(0);
        string name = reader.GetString(1);
        string schoolName = reader.GetString(2);
        int idTeam = reader.GetInt32(3);
        string nameTeam = reader.GetString(4);
        Team team = new Team(nameTeam);
        team.SetId(idTeam);
        Player player = new Player(name, schoolName, team);
        player.SetId(idPlayer);
        return player;
    }
}