using DefaultNamespace;
using Google.Protobuf.WellKnownTypes;
using log4net;
using Microsoft.AspNetCore.Components.Sections;
using MPP_CSharpProject.domain;
using Org.Example.ClientFx.Grpc;

namespace Server;

using Grpc.Core;


public class ProtoServiceImplementation : BookingService.BookingServiceBase
{
    private IEmployeeRepository _employeeRepository;
    private IFlightRepository _flightRepository;
    private ITicketRepository _ticketRepository;
    

    public ProtoServiceImplementation(IEmployeeRepository employeeRepository, IFlightRepository flightRepository,
        ITicketRepository ticketRepository)
    {
        _employeeRepository = employeeRepository;
        _flightRepository = flightRepository;
        _ticketRepository = ticketRepository;
    }

    public override Task<DefaultResponse> Login(EmployeeDTO request, ServerCallContext context)
    {   Console.Write("Login: a");
        Employee foundEmployee = _employeeRepository.FindUserByPassword(request.User, request.Password);

        var response = new DefaultResponse();
        if (foundEmployee != null)
        {
            response.Success = true;
        }
        else
        {
            response.Error = "Invalid username or password";
        }

        return Task.FromResult(response);
    }

    public override Task<FlightResponse> GetAllFlights(Empty request, ServerCallContext context)
    {
        try
        {
            var flightList = new FlightList();

            IEnumerable<Flight> flights = _flightRepository.GetAll();
            foreach (Flight flight in flights)
            {
                flightList.Flights.Add(new FlightDTO
                {
                    Id = flight.GetId(),
                    Origin = flight.Origin,
                    Destination = flight.Departure,
                    Airport = flight.Airport,
                    AvailableSeats = flight.AvailableSeats,
                    Date = flight.DayTime.ToString()
                });
            }

            FlightResponse flightResponse = new FlightResponse();
            flightResponse.Flight = flightList;
            Console.WriteLine("Am ajuns aici!");
            return Task.FromResult(flightResponse);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }
        return Task.FromResult(new FlightResponse());
    }

    public override Task<CityResponse> GetOrigin(Empty request, ServerCallContext context)
    {
        HashSet<String> origins = _flightRepository.GetOrigins();
        var cityList = new CityList();
        foreach (var origin in origins)
        {
            cityList.Cities.Add(origin);
        }

        CityResponse cityResponse = new CityResponse();
        cityResponse.City = cityList;
        return Task.FromResult(cityResponse);
    }

    public override Task<CityResponse> GetDestination(Empty request, ServerCallContext context)
    {
        HashSet<String> origins = _flightRepository.GetDestinations();
        var cityList = new CityList();
        foreach (var origin in origins)
        {
            cityList.Cities.Add(origin);
        }

        CityResponse cityResponse = new CityResponse();
        cityResponse.City = cityList;
        return Task.FromResult(cityResponse);
    }

    public override Task<FlightResponse> SearchFlight(FlightDTO request, ServerCallContext context)
    {
        var origin = request.Origin;
        var destination = request.Destination;
        var date = Convert.ToDateTime(request.Date);
        DateOnly dateOnly = DateOnly.FromDateTime(date);
        FlightList flightList = new FlightList();
        List<Flight> flights = _flightRepository.FindByDestination(origin, destination, dateOnly);
        foreach (Flight flight in flights)
        {
            flightList.Flights.Add(new FlightDTO
            {
                Id = flight.GetId(),
                Origin = flight.Origin,
                Destination = flight.Departure,
                Airport = flight.Airport,
                AvailableSeats = flight.AvailableSeats,
                Date = flight.DayTime.ToString()
            });
        }

        FlightResponse flightResponse = new FlightResponse();
        flightResponse.Flight = flightList;
        return Task.FromResult(flightResponse);
    }

    public override async Task<DefaultResponse> AddTicket(TicketDTO request, ServerCallContext context)
    {
        FlightDTO flightDTO = request.Flight;
        Flight flight = new Flight(flightDTO.Origin, flightDTO.Destination, flightDTO.AvailableSeats, flightDTO.Airport,
            Convert.ToDateTime(flightDTO.Date));
        flight.SetId(flightDTO.Id);
        Ticket ticket = new Ticket(request.Buyers, flight, request.NumberOfTickets);
        ticket.SetId(request.Id);
        _ticketRepository.Save(ticket);
        flight.AvailableSeats -= ticket.GetNumberOfTickets();
        Console.WriteLine("UPDATE"+flight.GetId()+" "+flight.Origin+" "+flight.Departure+" "+flight.Airport+" "+flight.AvailableSeats);
        _flightRepository.Update(flight);

        DefaultResponse response = new DefaultResponse();
        response.Success = true;
        await NotificationServiceImpl.BroadcastAsync(request.Flight);
        return new DefaultResponse { Success = true };
    }
}