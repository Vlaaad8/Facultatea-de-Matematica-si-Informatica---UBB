namespace Networking.dto;

public interface Request
{
    
}

[Serializable]
public class LoginRequest : Request
{
    private EmployeeDTO _employee;

    public LoginRequest(EmployeeDTO employee)
    {
        _employee = employee;
    }

    public virtual EmployeeDTO Employee
    {
        get => _employee;
    }
    
}

[Serializable]
public class SearchRequest : Request
{
    private FlightDTO _flight;

    public SearchRequest(FlightDTO flight)
    {
        _flight = flight;
    }

    public virtual FlightDTO Flight
    {
        get => _flight;
    }
    
}

[Serializable]
public class AddTicketRequest : Request
{
    private TicketDTO _ticket;

    public AddTicketRequest(TicketDTO ticket)
    {
        _ticket = ticket;
    }

    public virtual TicketDTO Ticket
    {
        get => _ticket;
    }
}

[Serializable]
public class GetOriginRequest : Request
{

}

[Serializable]
public class GetDestinationRequest : Request
{
    
}

[Serializable]
public class GetFlightRequest : Request
{
    
}