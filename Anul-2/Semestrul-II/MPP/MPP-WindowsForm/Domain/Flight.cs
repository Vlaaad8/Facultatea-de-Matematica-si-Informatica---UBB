using DefaultNamespace;

namespace MPP_CSharpProject.domain;

public class Flight:Entity<int>
{
    private string _origin;
    private string _departure;
    private int _availableSeats;
    private string _airport;
    private DateTime _daytime;

    public Flight(string origin, string departure, int availableSeats, string airport, DateTime daytime)
    {
        _origin = origin;
        _departure = departure;
        _availableSeats = availableSeats;
        _airport = airport;
        _daytime = daytime;
    }

    public string Origin
    {
        get => _origin;
        set => _origin = value;
    }

    public string Departure
    {
        get => _departure;
        set => _departure = value;
    }

    public int AvailableSeats
    {
        get => _availableSeats;
        set => _availableSeats = value;
    }

    public string Airport
    {
        get => _airport;
        set => _airport = value;
    }

    public DateTime DayTime
    {
        get => _daytime;
        set => _daytime = value;
    }



    public override string ToString()
    {
        return "Flight:" + GetId()+" "+_origin+" "+ _departure+" "+ _availableSeats+" "+ _airport+" "+ _daytime;
    }
}