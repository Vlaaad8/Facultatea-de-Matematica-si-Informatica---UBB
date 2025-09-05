namespace MPP_WindowsForm;

public enum FlightEvent
{
    NewTicket
}
public class FlightEventArgs:EventArgs
{
    private readonly FlightEvent _flightEventArgs;
    private readonly Object data;

    public FlightEventArgs(FlightEvent flightEventArgs, Object data)
    {
        _flightEventArgs = flightEventArgs;
        this.data = data;
    }
    public FlightEvent FlightEventType => _flightEventArgs;
    
    public object Data => data;
}