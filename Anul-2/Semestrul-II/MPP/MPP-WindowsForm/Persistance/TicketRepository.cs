using log4net;
using MPP_CSharpProject.domain;
using MPP_CSharpProject.utils;

namespace DefaultNamespace;

public class TicketRepository(IDictionary<string, string> dbConnection)
    : ITicketRepository
{
    private static readonly ILog Log = LogManager.GetLogger("TicketRepository");
    public Ticket? FindOne(int entity)
    {
        Log.Info($"Finding ticket with id {entity}");
        using var connection=DBUtils.getConnection(dbConnection);
        var command = connection.CreateCommand();
        command.CommandText = "SELECT * FROM tickets WHERE id=@id INNER JOIN flights ON tickets.flightId=flights.id";
        var parameter = command.CreateParameter();
        parameter.ParameterName = "@id";
        parameter.Value = entity;
        command.Parameters.Add(parameter);
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            var id = reader.GetInt32(0);
            var flightId = reader.GetInt32(1);
            var numberOfTickets = reader.GetInt32(2);
            var buyers=reader.GetString(3);
            
            var id2=reader.GetInt32(4);
            var origin=reader.GetString(5);
            var departure=reader.GetString(6);
            var airport=reader.GetString(7);
            var dayTime=reader.GetDateTime(8);
            var availableSeats=reader.GetInt32(9);
            Flight flight= new Flight(origin, departure,availableSeats, airport,dayTime) ;
            flight.SetId(id2);
            
            
            
            Ticket ticket=new Ticket(buyers,flight,numberOfTickets);
            ticket.SetId(id);
            Log.Info("Ticket found!");
            return ticket;
        }
        return null;
    }

    public Ticket Save(Ticket entity)
    {
        Log.Info($"Saving ticket {entity}");
        using var connection=DBUtils.getConnection(dbConnection);
        var command = connection.CreateCommand();
        command.CommandText =
            "INSERT INTO tickets(flightId,numberOfTickets,buyers) VALUES(@flightId,@numberOfTickets,@buyers)";
        
        var flightParameter = command.CreateParameter();
        flightParameter.ParameterName = "@flightId";
        flightParameter.Value = entity.GetFlight().GetId();
        command.Parameters.Add(flightParameter);
        
        var numberOfTicketsParameter = command.CreateParameter();
        numberOfTicketsParameter.ParameterName = "@numberOfTickets";
        numberOfTicketsParameter.Value = entity.GetNumberOfTickets();
        command.Parameters.Add(numberOfTicketsParameter);
        
        var buyersParameter = command.CreateParameter();
        buyersParameter.ParameterName = "@buyers";
        buyersParameter.Value = entity.GetBuyer();
        command.Parameters.Add(buyersParameter);
        
        int result=command.ExecuteNonQuery();
        if (result == 1)
        {   Log.Info("Ticket saved!");
            return entity;
        }
        Log.Warn("Failed to save ticket");
        return null;
    }

    public Ticket Delete(Ticket entity)
    {
        throw new NotImplementedException();
    }

    public Ticket Update(Ticket entity)
    {
        Log.Info($"Updating ticket {entity}");
        using var connection=DBUtils.getConnection(dbConnection);
        var command = connection.CreateCommand();
        command.CommandText ="update tickets set numberOfTickets=@numberOfTickets,buyers=@buyers where id=@id";
        
        var numberOfTicketsParameter = command.CreateParameter();
        numberOfTicketsParameter.ParameterName = "@numberOfTickets";
        numberOfTicketsParameter.Value = entity.GetNumberOfTickets();
        command.Parameters.Add(numberOfTicketsParameter);
        
        var buyersParameter = command.CreateParameter();
        buyersParameter.ParameterName = "@buyers";
        buyersParameter.Value = entity.GetBuyer();
        command.Parameters.Add(buyersParameter);
        
        int result=command.ExecuteNonQuery();
        if (result == 1)
        {   Log.Info("Ticket updated!");
            return entity;
        }
        Log.Warn("Failed to update ticket");
        return null;
    }

    public IEnumerable<Ticket> GetAll()
    {   Log.Info("Retrieving tickets");
        List<Ticket> tickets = new List<Ticket>();
        using var connection=DBUtils.getConnection(dbConnection);
        var command = connection.CreateCommand();
        command.CommandText = "SELECT * FROM tickets INNER JOIN flights on tickets.flightId=flights.id";
        using var reader = command.ExecuteReader();
        while (reader.Read())
        {
            var id = reader.GetInt32(0);
            var flightId = reader.GetInt32(1);
            var numberOfTickets = reader.GetInt32(2);
            var buyers=reader.GetString(3);
            
            var id2=reader.GetInt32(4);
            var origin=reader.GetString(5);
            var departure=reader.GetString(6);
            var airport=reader.GetString(7);
            var dayTime=reader.GetDateTime(8);
            var availableSeats=reader.GetInt32(9);
            Flight flight= new Flight(origin, departure,availableSeats, airport,dayTime) ;    
            flight.SetId(id2);
            
            Ticket ticket=new Ticket(buyers,flight,numberOfTickets);
            ticket.SetId(id);
            tickets.Add(ticket);
        }
        Log.Info("Tickets retrieved");
        return tickets;
    }
}