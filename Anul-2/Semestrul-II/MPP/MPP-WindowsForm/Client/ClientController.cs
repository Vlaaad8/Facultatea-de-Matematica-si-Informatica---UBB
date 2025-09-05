using log4net;
using MPP_CSharpProject.domain;
using Services;

namespace MPP_WindowsForm;

public class ClientController:IObserver
{
    public event EventHandler<FlightEventArgs> updateEvent;
    private readonly IServices server;
    private Employee currentEmployee;
    private static readonly ILog log = LogManager.GetLogger(typeof(ClientController));

    public ClientController(IServices server)
    {
        this.server = server;
        currentEmployee = null;
    }

    public Employee Login(String username, String password)
    {
        Employee employee = new Employee(username, password, "", "");
        employee.SetId(0);
        return server.Login(employee,this);
    }
    protected virtual void OnTicketEvent(FlightEventArgs e)
    {
        if (updateEvent == null) return;
        updateEvent.Invoke(this, e);
        log.Info("Update Event called");
    }
    public void NewTicketBought(Flight flight)
    {
        var args = new FlightEventArgs(FlightEvent.NewTicket, flight);
        OnTicketEvent(args);  
    }
}