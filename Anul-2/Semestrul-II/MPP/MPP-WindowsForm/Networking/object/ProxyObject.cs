using DefaultNamespace;
using MPP_CSharpProject.domain;
using Services;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using MessagePack;

#pragma warning disable SYSLIB0011

namespace Networking.dto;

public class ProxyObject : IServices
{
    private string host;
    private int port;

    private IObserver observer;
    private NetworkStream socket;

#pragma warning disable SYSLIB0011
    private IFormatter formatter;
#pragma warning restore SYSLIB0011
    private TcpClient connection;

    private Queue<Response> qResponses;
    private volatile bool finisted;

    private EventWaitHandle waitHandle;

    public ProxyObject(string host, int port)
    {
        this.host = host;
        this.port = port;
        qResponses = new Queue<Response>();
    }

    private void InitializeConnection()
    {
        connection = new TcpClient(host, port);
        socket = connection.GetStream();
#pragma warning disable SYSLIB0011
        formatter = new BinaryFormatter();
#pragma warning restore SYSLIB0011
        finisted = false;
        waitHandle = new AutoResetEvent(false);
        StartReader();
    }

    private void CloseConnection()
    {
        finisted = true;
        try
        {
            socket.Close();
            connection.Close();
            waitHandle.Close();
            observer = null;
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }
    }

    private void StartReader()
    {
        Thread threadWorker = new Thread(Run);
        threadWorker.Start();
    }

    private Response ReadResponse()
    {
        Response response = null;
        try
        {
            waitHandle.WaitOne();
            lock (response)
            {
                response = qResponses.Dequeue();
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }

        return response;
    }

    private void SendRequest(Request request)
    {
        try
        {
            lock (socket)
            {
                formatter.Serialize(socket, request);
                socket.Flush();
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
        }
    }


    public void AddTicket(Ticket ticket)
    {
        throw new NotImplementedException();
    }

    public IEnumerable<Flight> GetAllFlights()
    {
        throw new NotImplementedException();
    }

    public virtual Employee Login(Employee employee, IObserver observerLogin)
    {
        InitializeConnection();
        EmployeeDTO employeeLogin = new EmployeeDTO(employee.GetId(), employee.GetUser(), employee.GetPassword(),
            employee.GetFirstName(), employee.GetLastName());
        SendRequest(new LoginRequest(employeeLogin));
        Response response = ReadResponse();
        if (response is OkResponse)
        {
            this.observer = observerLogin;
            return employee;
        }
        else if (response is ErrorResponse)
        {
            ErrorResponse errorResponse = (ErrorResponse)response;
            CloseConnection();
            Console.WriteLine(errorResponse.Message);
        }

        return null;
    }

    public HashSet<string> GetOrigin()
    {
        throw new NotImplementedException();
    }

    public HashSet<string> GetDestination()
    {
        throw new NotImplementedException();
    }

    public List<Flight> SearchFlight(Flight flight)
    {
        throw new NotImplementedException();
    }

    private void HandleUpdate(NewTicketResponse response)
    {
        Ticket ticket = DTOUtils.getFromDTO(response.Ticket);
        try
        {
            observer.NewTicketBought(ticket);
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.Message);
        }
    }

    //TODO posibil sa nu mearga din cauza protected -> public 
    protected virtual void Run()
    {
        while (!finisted)
        {
            try
            {
                object response = formatter.Deserialize(socket);
                Console.WriteLine("response received " + response);

                if (response is NewTicketResponse)
                {
                    HandleUpdate((NewTicketResponse)response);
                }
                else
                {
                    lock (qResponses)
                    {
                        qResponses.Enqueue((Response)response);
                    }

                    waitHandle.Set();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }
    }
}