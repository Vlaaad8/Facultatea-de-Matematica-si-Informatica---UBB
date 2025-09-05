using MPP_CSharpProject.domain;

namespace DefaultNamespace;

public interface IFlightRepository: IRepository<Flight>
{
    List<Flight> FindByAvailableSeats();
    List<Flight> FindByDestination(string origin,string departure,DateOnly departureDate);

    HashSet<string> GetOrigins();
    HashSet<string> GetDestinations();
}