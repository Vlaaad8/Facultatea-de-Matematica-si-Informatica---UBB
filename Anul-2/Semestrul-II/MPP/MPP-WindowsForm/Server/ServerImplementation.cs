using DefaultNamespace;
using log4net;
using MPP_CSharpProject.domain;
using Org.BouncyCastle.Asn1.X509;
using Org.BouncyCastle.Math;
using Services;

namespace Server;

public class ServerImplementation : IServices
{
    private IEmployeeRepository _employeeRepository;
    private IFlightRepository _flightRepository;
    private ITicketRepository _ticketRepository;
    
    private readonly IDictionary<String,IObserver> _loggedClients;
    private static readonly ILog log = LogManager.GetLogger(typeof(ServerImplementation));

    public ServerImplementation(IEmployeeRepository employeeRepository, IFlightRepository flightRepository,
        ITicketRepository ticketRepository)
    {
        _employeeRepository = employeeRepository;
        _flightRepository = flightRepository;
        _ticketRepository = ticketRepository;
        _loggedClients = new Dictionary<String,IObserver>();
    }
    public void AddTicket(Ticket ticket)
    {
        _ticketRepository.Save(ticket);
        Flight flight = ticket.GetFlight();
        flight.AvailableSeats-=ticket.GetNumberOfTickets();
        _flightRepository.Update(flight);
        log.Info($"SERVER: Încerc notificarea către {_loggedClients.Count} observatori.");
        
        foreach (var observer in _loggedClients.Values)
        {
            Task.Run(() =>
            {
                try
                {
                    observer.NewTicketBought(flight);
                    log.Info("SERVER: Notificare trimisă cu succes unui observer.");
                }
                catch (Exception e)
                {
                    log.Info($"SERVER: Eroare la notificare: {e.Message}");
                }
            });
        }

    }

    public IEnumerable<Flight> GetAllFlights()
    {
        return _flightRepository.GetAll();
    }

    public Employee Login(Employee employee, IObserver observer)
    {
        Employee? employeeFound = _employeeRepository.FindUserByPassword(employee.GetUser(), employee.GetPassword());
        if (employeeFound != null)
        {
            if (_loggedClients.ContainsKey(employee.GetUser()))
            {
                log.Info("Employee already logged in");
                throw new Exception("User already logged in!");
            }
            _loggedClients[employee.GetUser()] = observer;
            log.Info($"SERVER: User '{employee.GetUser()}' logat cu succes. Total clienți: {_loggedClients.Count}");
            return employeeFound;
        }
        log.Info($"SERVER: Logare eșuată pentru user '{employee.GetUser()}'");
        return null;
    }

    public HashSet<string> GetOrigin()
    {
        return _flightRepository.GetOrigins();
    }

    public HashSet<string> GetDestination()
    {
        return _flightRepository.GetDestinations();
    }

    public List<Flight> SearchFlight(Flight flight)
    {   
        DateOnly dateOnly = DateOnly.FromDateTime(flight.DayTime);
        return _flightRepository.FindByDestination(flight.Origin,flight.Departure,dateOnly);
    }
}