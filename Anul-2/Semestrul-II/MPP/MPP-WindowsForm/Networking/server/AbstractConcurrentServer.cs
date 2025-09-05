using System.Net.Sockets;

namespace Networking;

public abstract class AbstractConcurrentServer:AbstractServer
{
    protected AbstractConcurrentServer(string host, int port) : base(host, port)
    {
    }

    public override void processRequest(TcpClient client)
    {
        Thread thread = createWorker(client);
        thread.Start();
    }
    
    protected abstract Thread createWorker(TcpClient client);

}