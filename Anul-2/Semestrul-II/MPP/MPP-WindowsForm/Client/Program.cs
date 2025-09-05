using System.Configuration;
using System.Reflection;
using log4net;
using log4net.Config;
using MPP_WindowsForm;
using Networking.json;
using Services;

static class Program
{
    [STAThread]
    static void Main()
    {
        var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
        XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));
        ApplicationConfiguration.Initialize();


        int port = int.TryParse(ConfigurationManager.AppSettings["port"], out var p) ? p : 55556;
        string ip = ConfigurationManager.AppSettings["ip"] ?? "127.0.0.1";


        ProxyJSON proxy = new ProxyJSON(ip, port);
        
        ClientController controller = new ClientController(proxy);


        LogMenu form = new LogMenu(controller, proxy);
        Application.Run(form);
    }
}