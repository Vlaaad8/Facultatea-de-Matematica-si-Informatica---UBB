using DefaultNamespace;
using MPP_CSharpProject.domain;

namespace Networking.dto;

public class DTOUtils
{
    public static Employee getFromDTO(EmployeeDTO employeeDTO)
    {
        int id=employeeDTO.Id;
        string user=employeeDTO.User;
        string password=employeeDTO.Password;
        string firstName=employeeDTO.FirstName;
        string lastName=employeeDTO.LastName;
        Employee employee = new Employee(user,password,firstName,lastName);
        employee.SetId(id);
        return employee;
    }

    public static Flight getFromDTO(FlightDTO flightDTO)
    {
        int id=flightDTO.Id;
        string origin=flightDTO.Origin;
        string departure=flightDTO.Departure;
        string airport=flightDTO.Airport;
        DateTime date=flightDTO.Date;
        int availableSeats=flightDTO.AvailableSeats;
        Flight flight=new Flight(origin,departure,availableSeats,airport,date);
        flight.SetId(id);
        return flight;
    }

    public static Ticket getFromDTO(TicketDTO ticketDTO)
    {
        int id=ticketDTO.Id;
        Flight flight= getFromDTO(ticketDTO.Flight);
        int seats = ticketDTO.NumberOfTickets;
        string buyers=ticketDTO.Buyers;
        Ticket ticket=new Ticket(buyers,flight,seats);
        ticket.SetId(id);
        return ticket;
    }
}