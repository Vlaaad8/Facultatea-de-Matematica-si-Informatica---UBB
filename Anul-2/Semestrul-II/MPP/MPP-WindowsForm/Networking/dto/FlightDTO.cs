namespace Networking.dto;
[Serializable]
public class FlightDTO
{
    private int _id;
    private string _origin;
    private string _departure;
    private string _airport;
    private DateTime _date;
    private int _availableSeats;

    public FlightDTO(int id, string origin, string departure, string airport, DateTime date, int availableSeats)
    {
        _id = id;
        _origin = origin;
        _departure = departure;
        _airport = airport;
        _date = date;
        _availableSeats = availableSeats;
    }

    public int Id
    {
        get => _id;
    }

    public string Origin
    {
        get => _origin;
    }

    public string Departure
    {
        get => _departure;
    }

    public string Airport
    {
        get => _airport;
    }

    public DateTime Date
    {
        get => _date;
    }

    public int AvailableSeats
    {
        get => _availableSeats;
    }
}