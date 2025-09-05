using System.Configuration;

using System.Collections.Generic;
using System.Reflection;
using DefaultNamespace;
using Grpc.Core;
using log4net;
using log4net.Config;
using Org.Example.ClientFx.Grpc;
using Services;

namespace Server;
public class StartServer
{
    private static int DEFAULT_PORT = 55555;
    private static string DEFAULT_IP = "localhost";
    private static readonly ILog log = LogManager.GetLogger(typeof(StartServer));
    static async Task Main(string[] args)
    {
        var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
        XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));
        log.Info("Starting Server");
        int port=DEFAULT_PORT;
        string ip = DEFAULT_IP;
        String portImported = ConfigurationManager.AppSettings["port"];
        if (portImported != null)
        {
            bool success = Int32.TryParse(portImported, out port);
            if (!success)
            {
                port=DEFAULT_PORT;
                Console.WriteLine("Invalid port, using the default one");
            }
        }
        String ipImported = ConfigurationManager.AppSettings["ip"];
        
        IDictionary<string,string> properties= new SortedList<string,string>();
        properties.Add("ConnectionString",GetConnectionStringByName("MariaDBConnection"));
        
        IEmployeeRepository employeeRepository=new EmployeeDBRepository(properties);
        IFlightRepository flightRepository=new FlightRepository(properties);
        ITicketRepository ticketRepository=new TicketRepository(properties);
        
        //IServices servicesImplementation=new ServerImplementation(employeeRepository,flightRepository,ticketRepository);
        
        ProtoServiceImplementation protoServiceImplementation=new ProtoServiceImplementation(employeeRepository,flightRepository,ticketRepository);
        NotificationServiceImpl notificationServiceImpl = new NotificationServiceImpl();

        var server = new Grpc.Core.Server
        {
            Services =
            {
                BookingService.BindService(protoServiceImplementation),
                NotificationService.BindService(notificationServiceImpl)
            },
            Ports = { new ServerPort(ipImported, port, ServerCredentials.Insecure) }
        };
        server.Start();
        log.Info("Server started,running on "+ipImported+":"+port);
        Console.ReadLine();
        
        await server.ShutdownAsync();
       
    }

    static string GetConnectionStringByName(string name)
    {
        string returnValue = null;
        ConnectionStringSettings connectionStringSettings = ConfigurationManager.ConnectionStrings[name];
        if (connectionStringSettings != null)
        {
            returnValue = connectionStringSettings.ConnectionString;
        }
        return returnValue;
    }
    
}