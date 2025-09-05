using MPP_CSharpProject.domain;

namespace DefaultNamespace;

public class Ticket:Entity<int>
{
    private string _buyer;
    private Flight _flight;
    private int _numberOfTickets;

    public Ticket(string buyer, Flight flight, int numberOfTickets)
    {
        _buyer = buyer;
        _flight = flight;
        _numberOfTickets = numberOfTickets;
    }

    public string GetBuyer() => _buyer;
    public Flight GetFlight() => _flight;
    public int GetNumberOfTickets() => _numberOfTickets;

    public override string ToString()
    {
        return "Ticket number: " + _numberOfTickets + ", buyer: " + _buyer + ", flight: " + _flight.GetId();
    }
}