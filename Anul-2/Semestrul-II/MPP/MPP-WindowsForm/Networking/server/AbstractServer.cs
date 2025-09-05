using System.Net;
using System.Net.Sockets;
using log4net;
using MPP_CSharpProject.domain;

namespace Networking;

public abstract class AbstractServer
{
    private TcpListener server;
    private String host;
    private int port;
    private static readonly ILog log = LogManager.GetLogger(typeof(AbstractServer));

    public AbstractServer(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public void Start()
    {
        IPAddress ipAddress = IPAddress.Parse(host);
        IPEndPoint localEndPoint = new IPEndPoint(ipAddress, port);
        server=new TcpListener(localEndPoint);
        server.Start();
        while (true)
        {
            log.Info("Waiting for a connection...");
            TcpClient client = server.AcceptTcpClient();
            log.Info("Connection accepted");
            processRequest(client);
        }
    }
    public abstract void processRequest(TcpClient client);

}