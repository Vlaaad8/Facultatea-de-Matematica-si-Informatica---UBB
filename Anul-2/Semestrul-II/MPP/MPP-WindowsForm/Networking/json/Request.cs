using Networking.dto;

namespace Networking.json;

public class Request
{
    public RequestType RequestType { get; set; }

    public EmployeeDTO Employee { get; set; }

    public FlightDTO Flight { get; set; }

    public TicketDTO Ticket { get; set; }

    public override string ToString()
    {
        return string.Format("Request[type={0}, employee={1}, flight={2}, ticket={3}]", RequestType, Employee, Flight,
            Ticket);
    }
}