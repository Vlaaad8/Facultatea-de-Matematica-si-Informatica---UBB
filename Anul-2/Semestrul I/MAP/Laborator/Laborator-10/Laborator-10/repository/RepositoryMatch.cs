using Laborator_10.domain;
using Npgsql;
using NpgsqlTypes;

namespace Laborator_10.repository;

public class RepositoryMatch:AbstractRepositoryDataBase<Match>
{
    public RepositoryMatch(string connectionString) : base(connectionString)
    {
    }

    public override Match Save(Match entity)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query =
            "INSERT INTO match(idteamone,nameteamone,idteamtwo,nameteamtwo,datematch) VALUES (@id1,@name1,@id2,@name2,@date)";
        using var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@id1", entity.FirstTeam.GetId());
        command.Parameters.AddWithValue("@name1", entity.FirstTeam.Name);
        command.Parameters.AddWithValue("@id2", entity.SecondTeam.GetId());
        command.Parameters.AddWithValue("@name2", entity.SecondTeam.Name);
        command.Parameters.AddWithValue("@date", entity.StartDate);
        if (command.ExecuteNonQuery() == 1)
        {
            return null;
        }
        return entity;
    }


    public override Match Delete(Match entity)
    {
        using var connection= new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query="DELETE FROM match WHERE idteamone = @idteamone";
        var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@idteamone", entity.GetId());
        if (command.ExecuteNonQuery() == 1)
        {
            return entity;
        }
        return null;
    }

    public override IEnumerable<Match> GetAll()
    {
        List<Match> matches = new List<Match>();
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM match";
        using var command = new NpgsqlCommand(query, connection);
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            int id=reader.GetInt32(0);
            int id1=reader.GetInt32(1);
            string name1=reader.GetString(2);
            int id2=reader.GetInt32(3);
            string name2=reader.GetString(4);
            DateTime startDate=reader.GetDateTime(5);
            Team firsTeam = new Team(name1);
            Team secondTeam = new Team(name2);
            firsTeam.SetId(id1);
            secondTeam.SetId(id2);
            Match match=new Match(firsTeam,secondTeam,startDate);
            match.SetId(id);
            matches.Add(match);
        }
        return matches;
    }

    public override Match FindOne(int id)
    {   using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM match WHERE idteamone = @idteamone";
        var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@idteamone", id);
        using var reader = command.ExecuteReader();
        reader.Read();
            int matchId=reader.GetInt32(0);
            int id1=reader.GetInt32(1);
            string name1=reader.GetString(2);
            int id2=reader.GetInt32(3);
            string name2=reader.GetString(4);
            DateTime startDate=reader.GetDateTime(5);
            Team firsTeam = new Team(name1);
            Team secondTeam = new Team(name2);
            firsTeam.SetId(id1);
            secondTeam.SetId(id2);
            Match match=new Match(firsTeam,secondTeam,startDate);
            match.SetId(matchId);
            return match;
    }
}