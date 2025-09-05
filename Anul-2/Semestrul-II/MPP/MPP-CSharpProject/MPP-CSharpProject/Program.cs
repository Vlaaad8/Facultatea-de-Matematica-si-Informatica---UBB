using System.Configuration;
using System.Drawing.Printing;
using System.Reflection;
using DefaultNamespace;
using log4net;
using log4net.Config;
using MPP_CSharpProject.domain;

public static class MainClass
{
    private static readonly ILog log = LogManager.GetLogger(typeof(MainClass));

    public static void Main(string[] args)
    {
        var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
        XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));
        log.Info("Starting application");
        IDictionary<string, string> props = new SortedList<string, string>();
        props.Add("ConnectionString", GetConnectionStringByName("MariaDBConnection"));


        var repositoryEmployee = new EmployeeDBRepository(props);
        //repositoryEmployee.Save(new Employee("antonia8", "parola", "Antonia", "Moga"));
        //repositoryEmployee.Save(new Employee("dana7", "parola", "Dana", "Rusu"));
        
        IEnumerable<Employee> employees = repositoryEmployee.GetAll();
        foreach (Employee employee in employees)
        {
            Console.WriteLine(employee);
        }
        
        Console.WriteLine(repositoryEmployee.FindUserByPassword("antonia8","parola"));
        
        Console.WriteLine(repositoryEmployee.FindOne(3));

        Employee updateEmployee = new Employee("vladEm", "parola", "Vlad", "Enea");
        updateEmployee.SetId(3);
        repositoryEmployee.Update(updateEmployee);
        
        IEnumerable<Employee> employees2 = repositoryEmployee.GetAll();
        foreach (Employee employee in employees2)
        {
            Console.WriteLine(employee);
        }
        
        IFlightRepository flightRepository= new FlightRepository(props);

        Flight flightOne = new Flight("Suceava", "Bucuresti", 123, "SCV", DateTime.Now);
        
        flightRepository.Save(flightOne);
        
        IEnumerable<Flight> flights = flightRepository.GetAll();

        foreach (Flight flight in flights)
        {
            Console.WriteLine(flight);
        }
        
        Console.WriteLine("Zborul gasit este: "+ flightRepository.FindOne(7));

        foreach (Flight flight in flightRepository.FindByAvailableSeats())
        {
            Console.WriteLine(flight);
        }

        foreach (var flight in flightRepository.FindByDestination("Bucuresti","Cluj-Napoca",DateOnly.FromDateTime(DateTime.Now)))
        {
            Console.WriteLine(flight);
        }
        
        ITicketRepository repositoryTicket = new TicketRepository(props);
        Ticket ticket = new Ticket("Ana Maria,Marcela Pop", flightRepository.FindOne(7), 2);
        repositoryTicket.Save(ticket);

        foreach (var ticket1 in repositoryTicket.GetAll())
        {
            Console.WriteLine(ticket1);
        }

    }

    private static string GetConnectionStringByName(string name)
    {
        string returnValue = null;
        var settings = ConfigurationManager.ConnectionStrings[name];
        if (settings != null)
            returnValue = settings.ConnectionString;
        return returnValue;
    }
}