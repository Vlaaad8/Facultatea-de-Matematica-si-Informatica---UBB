using Laborator_10.domain;
using Laborator_10.utils;
using Npgsql;

namespace Laborator_10.repository;

public class RepositoryActivePlayer : AbstractRepositoryDataBase<ActivePlayer>
{
    public RepositoryActivePlayer(string connectionString) : base(connectionString)
    {
    }

    public override ActivePlayer Save(ActivePlayer entity)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "INSERT INTO activeplayer(id,matchid,pointsscored,type) VALUES (@id,@matchid,@pointsscored,@type)";
        var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@id", entity.GetId());
        command.Parameters.AddWithValue("@matchID", entity.MatchID);
        command.Parameters.AddWithValue("@pointsscored", entity.TotalPointsScored);
        command.Parameters.AddWithValue("@type", entity.Type.ToString());
        if (command.ExecuteNonQuery() == 1)
        {
            return null;
        }

        return entity;
    }

    public override ActivePlayer Delete(ActivePlayer entity)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "DELETE FROM activeplayer WHERE MatchID=@matchID";
        var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@matchID", entity.MatchID);
        if (command.ExecuteNonQuery() == 1)
        {
            return entity;
        }

        return null;
    }

    public override IEnumerable<ActivePlayer> GetAll()
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM activeplayer";
        var command = new NpgsqlCommand(query, connection);
        var reader = command.ExecuteReader();
        var result = new List<ActivePlayer>();
        while (reader.Read())
        {
            int idPlayer = reader.GetInt32(0);
            int idMatch = reader.GetInt32(1);
            int pointsScored = reader.GetInt32(2);
            string type = reader.GetString(3);
            PlayerType.TryParse(type, out PlayerType playerType);
            ActivePlayer activePlayer = new ActivePlayer(idPlayer,idMatch, pointsScored, playerType);
            result.Add(activePlayer);
        }

        return result;
    }

    public override ActivePlayer FindOne(int id)
    {
        using var connection = new NpgsqlConnection(ConnectionString);
        connection.Open();
        var query = "SELECT * FROM activeplayer WHERE playerID=@playerID";
        var command = new NpgsqlCommand(query, connection);
        command.Parameters.AddWithValue("@playerID", id);
        var reader = command.ExecuteReader();
        reader.Read();
        int idPlayer = reader.GetInt32(0);
        int idMatch = reader.GetInt32(1);
        int pointsScored = reader.GetInt32(2);
        string type = reader.GetString(3);
        PlayerType.TryParse(type, out PlayerType playerType);
        ActivePlayer activePlayer = new ActivePlayer(idPlayer,idMatch, pointsScored, playerType);
        return activePlayer;
    }
}