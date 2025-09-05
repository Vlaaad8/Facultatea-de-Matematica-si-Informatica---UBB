using System.Net.Sockets;
using log4net;
using Networking.json;
using Services;

namespace Networking;

public class ConcurrentServer:AbstractConcurrentServer
{
    private IServices services;
    private WorkerJSON workerObject;
    private static readonly ILog log = LogManager.GetLogger(typeof(ConcurrentServer));
    public ConcurrentServer(string host, int port,IServices services) : base(host, port)
    {
        this.services = services;
        log.Info("Concurrent server started");
    }

    protected override Thread createWorker(TcpClient client)
    {
        workerObject = new WorkerJSON(services, client);
        return new Thread(new ThreadStart(workerObject.Run));
    }
}