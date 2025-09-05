using System.Data;
using log4net;
using MPP_CSharpProject.domain;
using MPP_CSharpProject.utils;

namespace DefaultNamespace;

public class FlightRepository(IDictionary<string, string> dbConnection):IFlightRepository
{
    private static readonly ILog Log = LogManager.GetLogger("FlightDBRepository");
    public Flight? FindOne(int entity)
    {   Log.Info($"Searching for flight with id {entity}");
        using var connection = DBUtils.getConnection(dbConnection);
        
        using var command = connection.CreateCommand();
        command.CommandText = "SELECT * FROM Flights WHERE id = @FlightId";
        var idParameter = command.CreateParameter();
        idParameter.ParameterName = "@FlightId";
        idParameter.Value= entity;
        command.Parameters.Add(idParameter);
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            var origin=reader.GetString(1);
            var departure=reader.GetString(2);
            var airport=reader.GetString(3);
            var dayTime=reader.GetDateTime(4);
            var availableSeats=reader.GetInt32(5);
            
            Flight flight=new Flight(origin,departure,availableSeats,airport,dayTime);
            flight.SetId(entity);
            Log.Info("Found flight");
            return flight;
        }
        Log.Warn("No flight found");
        return null;
    }

    public Flight Save(Flight entity)
    {   Log.Info($"Saving flight {entity}");
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText =
            "INSERT INTO flights(origin,departure,availableSeats,airport,dayTime) VALUES(@origin,@departure,@availableSeats,@airport,@dayTime)";
        
            var originParameter = command.CreateParameter();
            originParameter.ParameterName = "@origin";
            originParameter.Value = entity.Origin;
            command.Parameters.Add(originParameter);
            var departureParameter = command.CreateParameter();
            departureParameter.ParameterName = "@departure";
            departureParameter.Value = entity.Departure;
            command.Parameters.Add(departureParameter);
            var availableSeatsParameter = command.CreateParameter();
            availableSeatsParameter.ParameterName = "@availableSeats";
            availableSeatsParameter.Value = entity.AvailableSeats;
            command.Parameters.Add(availableSeatsParameter);
            var airportParameter = command.CreateParameter();
            airportParameter.ParameterName = "@airport";
            airportParameter.Value = entity.Airport;
            command.Parameters.Add(airportParameter);
            var dayTimeParameter = command.CreateParameter();
            dayTimeParameter.ParameterName = "@dayTime";
            command.Parameters.Add(dayTimeParameter);
            
            int result=command.ExecuteNonQuery();
            if (result == 1)
            {
                Log.Info($"Flight {entity} has been saved");
                return entity;
                
            }
            Log.Warn($"Flight {entity} has not been saved");
            return null;
        
        
    }

    public Flight Delete(Flight entity)
    {
        throw new NotImplementedException();
    }

    public Flight Update(Flight entity)
    {
        Log.Info($"Updating flight with {entity}");
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText = "update flights set availableSeats=@available where id=@id ";
        var availableSeatsParameter = command.CreateParameter();
        availableSeatsParameter.ParameterName = "@available";
        availableSeatsParameter.Value = entity.AvailableSeats;
        command.Parameters.Add(availableSeatsParameter);
        var idParameter = command.CreateParameter();
        idParameter.ParameterName = "@id";
        idParameter.Value = entity.GetId();
        command.Parameters.Add(idParameter);
        

        int result=command.ExecuteNonQuery();
        if (result == 1)
        {   Log.Info($"Flight {entity} has been saved");
            return entity;
        }
        Log.Warn($"Flight {entity} has not been saved");
        return null;
    }

    public IEnumerable<Flight> GetAll()
    {   Log.Info($"Getting all flights");
        List<Flight> flights = new List<Flight>();
        using var connection = ConnectionUtils.ConnectionFactory.getInstance().createConnection(dbConnection);
        connection.Open();
        using var command = connection.CreateCommand();
        command.CommandText = "SELECT * FROM Flights";
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            var id = reader.GetInt32(0);
            var origin=reader.GetString(1);
            var departure=reader.GetString(2);
            var airport=reader.GetString(3);
            var dayTime=reader.GetDateTime(4);
            var availableSeats=reader.GetInt32(5);
            Flight flight=new Flight(origin,departure,availableSeats,airport,dayTime);
            flight.SetId(id);
            flights.Add(flight);
        }
        Log.Info("All flights have been retrieved");
        return flights;
    }

    public List<Flight> FindByAvailableSeats()
    {   Log.Info("Finding all flights with available seats");
        List<Flight> flights = new List<Flight>();
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText = "SELECT * FROM flights WHERE availableSeats > 0";
        
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            var id = reader.GetInt32(0);
            var origin=reader.GetString(1);
            var departure=reader.GetString(2);
            var airport=reader.GetString(3);
            var dayTime=reader.GetDateTime(4);
            var availableSeats=reader.GetInt32(5);
            Flight flight=new Flight(origin,departure,availableSeats,airport,dayTime);
            flight.SetId(id);
            flights.Add(flight);
        }
        Log.Info("All flights have been retrieved");
        return flights;
    }

    public List<Flight> FindByDestination(string origin, string departure, DateOnly departureDate)
    {
        Log.Info("Finding all flights with destination, departure, and date");
        List<Flight> flights = new List<Flight>();
    
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText = "SELECT * FROM flights WHERE origin=@origin AND departure=@departure AND DATE(dayTime) = @departureDate and availableSeats>0";
    
        var originParameter = command.CreateParameter();
        originParameter.ParameterName = "@origin";
        originParameter.Value = origin;
        command.Parameters.Add(originParameter);

        var departureParameter = command.CreateParameter();
        departureParameter.ParameterName = "@departure";
        departureParameter.Value = departure;
        command.Parameters.Add(departureParameter);

        var dayTimeParameter = command.CreateParameter();
        dayTimeParameter.ParameterName = "@departureDate";
        dayTimeParameter.Value = departureDate.ToDateTime(TimeOnly.MinValue);
        command.Parameters.Add(dayTimeParameter);
    
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            var id = reader.GetInt32(0);
            var origin1 = reader.GetString(1);
            var departure1 = reader.GetString(2);
            var airport = reader.GetString(3);
            var dayTime = reader.GetDateTime(4);
            var availableSeats = reader.GetInt32(5);
        
            Flight flight = new Flight(origin1, departure1, availableSeats, airport, dayTime);
            flight.SetId(id);
            flights.Add(flight);
        }
    
        Log.Info("All flights have been retrieved");
        return flights;
    }

    public HashSet<string> GetOrigins()
    {
        Log.Info("Getting all origins");
        HashSet<string> origins = new HashSet<string>();
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText = "SELECT DISTINCT origin FROM Flights";
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            origins.Add(reader.GetString(0));
        }
        return origins;
    }
    
    public HashSet<String> GetDestinations()
    {
        Log.Info("Getting all departure");
        HashSet<String> origins = new HashSet<String>();
        using var connection = DBUtils.getConnection(dbConnection);
        using var command = connection.CreateCommand();
        command.CommandText = "SELECT DISTINCT departure FROM Flights";
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            origins.Add(reader.GetString(0));
        }
        return origins;
    }

}