using DefaultNamespace;
using MPP_CSharpProject.domain;
using Networking.dto;

namespace Networking.json;

public class UtilsProtocolJSON
{
    public static Request CreateLoginRequest(Employee employee)
    {
        EmployeeDTO employeeDto = new EmployeeDTO(employee.GetId(), employee.GetUser(), employee.GetPassword(),
            employee.GetFirstName(), employee.GetLastName());
        return new Request { RequestType = RequestType.LOGIN, Employee = employeeDto };
    }

    public static Request GetOriginRequest()
    {
        return new Request { RequestType = RequestType.GET_ORIGIN };
    }

    public static Request GetDestinationRequest()
    {
        return new Request { RequestType = RequestType.GET_DESTINATION };
    }

    public static Request GetFlightsRequest()
    {
        return new Request { RequestType = RequestType.GET_FLIGHTS };
    }

    public static Request GetSearch(Flight flight)
    {
        FlightDTO flightDto = new FlightDTO(flight.GetId(), flight.Origin, flight.Departure, flight.Airport,
            flight.DayTime, flight.AvailableSeats);
        return new Request { RequestType = RequestType.SEARCH, Flight = flightDto };
    }

    public static Request AddTicket(Ticket ticket)
    {
        Flight flight = ticket.GetFlight();
        FlightDTO flightDto = new FlightDTO(flight.GetId(), flight.Origin, flight.Departure, flight.Airport,
            flight.DayTime, flight.AvailableSeats);
        TicketDTO ticketDto = new TicketDTO(ticket.GetId(), flightDto, ticket.GetNumberOfTickets(),
            ticket.GetBuyer());
        return new Request { RequestType = RequestType.ADD_TICKET, Ticket = ticketDto };
    }

    public static Response CreateOkResponse()
    {
        return new Response { Type = ResponseType.OK };
    }

    public static Response CreateErrorResponse(string message)
    {
        return new Response { Type = ResponseType.ERROR, ErrorMessage = message };
    }

    public static Response CreateNotFoundResponse()
    {
        return new Response { Type = ResponseType.NOT_FOUND };
    }

    public static Response ReceiveOrigin(HashSet<string> origins)
    {
        return new Response { Type = ResponseType.RECEIVE_ORIGIN, Towns = origins };
    }

    public static Response ReceiveDestination(HashSet<string> destinations)
    {
        return new Response { Type = ResponseType.RECEIVE_DESTINAITON, Towns = destinations };
    }

    public static Response ReceiveFlight(IEnumerable<Flight> flights)
    {
        List<FlightDTO> flightDtos = new List<FlightDTO>();
        foreach (var flight in flights)
        {
            FlightDTO flightDto = new FlightDTO(flight.GetId(), flight.Origin, flight.Departure, flight.Airport,
                flight.DayTime, flight.AvailableSeats);
            flightDtos.Add(flightDto);
        }

        return new Response { Type = ResponseType.RECEIVE_FLIGHTS, FlightsDTO = flightDtos };
    }

    public static Response ReceiveSearch(List<Flight> flights)
    {
        List<FlightDTO> flightDtos = new List<FlightDTO>();
        foreach (var flight in flights)
        {
            FlightDTO flightDto = new FlightDTO(flight.GetId(), flight.Origin, flight.Departure, flight.Airport,
                flight.DayTime, flight.AvailableSeats);
            flightDtos.Add(flightDto);
        }

        return new Response { Type = ResponseType.RECEIVE_SEARCH, Flights = flightDtos };
    }

    public static Response NewTicket(Flight flight)
    {
        FlightDTO flightDto = new FlightDTO(flight.GetId(), flight.Origin, flight.Departure, flight.Airport,
            flight.DayTime, flight.AvailableSeats);
        return new Response { Type = ResponseType.NEW_TICKET, Flight = flightDto };
    }
}