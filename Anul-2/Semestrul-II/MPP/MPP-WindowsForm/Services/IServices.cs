using DefaultNamespace;
using MPP_CSharpProject.domain;

namespace Services;

public interface IServices
{
 void AddTicket(Ticket ticket);
 IEnumerable<Flight> GetAllFlights();
 Employee Login(Employee employee,IObserver observer);
 HashSet<string> GetOrigin();
 HashSet<string> GetDestination();
 List<Flight> SearchFlight(Flight flight);

}