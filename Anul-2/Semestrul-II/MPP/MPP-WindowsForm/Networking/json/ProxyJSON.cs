using System.Diagnostics;
using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using DefaultNamespace;
using log4net;
using MPP_CSharpProject.domain;
using Networking.dto;
using Services;

namespace Networking.json;

public class ProxyJSON : IServices
{
    private string host;
    private int port;

    private IObserver client;
    private NetworkStream stream;
    private TcpClient connection;
    private Queue<Response> qResponse;
    private volatile bool finished;
    private EventWaitHandle waitHandle;
    private static readonly ILog log = LogManager.GetLogger(typeof(ProxyJSON));
    public ProxyJSON(string host, int port)
    {
        this.host = host;
        this.port = port;
        qResponse = new Queue<Response>();
        
        InitializeConnection();
    }

    public void InitializeConnection()
    {
        try
        {
            connection = new TcpClient(host, port);
            stream = connection.GetStream();
            finished = false;
            waitHandle = new AutoResetEvent(false);
            StartReader();
        }
        catch (Exception e)
        {
            log.Error(e);
        }
    }

    private void StartReader()
    {
        Thread thread = new Thread(Run);
        thread.Start();
    }

    private void HandleUpdate(Response response)
    {
        if (response.Type == ResponseType.NEW_TICKET)
        {
            Flight flight = DTOUtils.getFromDTO(response.Flight);
            try
            {   
                log.DebugFormat("PROXY: Received NewTicket Notification");
                client.NewTicketBought(flight);
            }
            catch (Exception e)
            {
                log.Error(e);
            }
        }
    }

    public virtual void Run()
    {
        using StreamReader streamReader = new StreamReader(stream, Encoding.UTF8);
        while (!finished)
        {
            try
            {
                string responseJSON = streamReader.ReadLine();
                if (string.IsNullOrEmpty(responseJSON))
                    continue;
                Response response = JsonSerializer.Deserialize<Response>(responseJSON);
                if (response.Type == ResponseType.NEW_TICKET)
                {
                    HandleUpdate(response);
                }
                else
                {
                    lock (qResponse)
                    {
                        qResponse.Enqueue(response);
                    }

                    waitHandle.Set();
                }
            }
            catch (Exception e)
            {
                log.Error(e);
            }
        }
    }

    private void CloseConnection()
    {
        finished = true;
        try
        {
            stream.Close();
            connection.Close();
            waitHandle.Close();
            client = null;
        }
        catch (Exception e)
        {
            log.Error(e);
        }
    }

    private void SendRequest(Request request)
    {
        try
        {
            lock (stream)
            {
                string jsonRequest = JsonSerializer.Serialize(request);
                byte[] jsonRequestBytes = Encoding.UTF8.GetBytes(jsonRequest + "\n");
                stream.Write(jsonRequestBytes, 0, jsonRequestBytes.Length);
                stream.Flush();
            }
        }
        catch (Exception e)
        {
            log.Error(e);
        }
    }

    private Response ReadResponse()
    {
        Response response = null;
        try
        {
            waitHandle.WaitOne();
            lock (qResponse)
            {
                response = qResponse.Dequeue();
            }
        }
        catch (Exception e)
        {
            log.Error(e);
        }

        return response;
    }

    public void AddTicket(Ticket ticket)
    {
        SendRequest(UtilsProtocolJSON.AddTicket(ticket));
        Response response = ReadResponse();
        if (response.Type == ResponseType.OK)
        {
            log.Info(response);
        }

        if (response.Type == ResponseType.ERROR)
        {
            throw new Exception(response.ErrorMessage);
        }
    }

    public IEnumerable<Flight> GetAllFlights()
    {
        SendRequest(UtilsProtocolJSON.GetFlightsRequest());
        Response response = ReadResponse();
        log.Info(response.Type);
        if (response.Type == ResponseType.RECEIVE_FLIGHTS)
        {
            IEnumerable<FlightDTO> flights = response.FlightsDTO;
            List<Flight> flightList = new List<Flight>();
            foreach (FlightDTO flight in flights)
            {
                flightList.Add(DTOUtils.getFromDTO(flight));
            }

            return flightList;
        }

        if (response.Type == ResponseType.ERROR)
        {
            throw new Exception(response.ErrorMessage);
        }

        return null;
    }

    public virtual Employee Login(Employee employee, IObserver observer)
    {
        SendRequest(UtilsProtocolJSON.CreateLoginRequest(employee));
        Response response = ReadResponse();
        if (response.Type == ResponseType.OK)
        {
            this.client = observer;
            return employee;
        }

        if (response.Type == ResponseType.ERROR)

        {
            String error = response.ErrorMessage;
            log.Error(error);
            CloseConnection();
        }

        return null;
    }

    public HashSet<string> GetOrigin()
    {
        SendRequest(UtilsProtocolJSON.GetOriginRequest());
        Response response = ReadResponse();
        if (response.Type == ResponseType.RECEIVE_ORIGIN)
        {
            HashSet<string> origin = response.Towns;
            return origin;
        }

        if (response.Type == ResponseType.ERROR)
        {
            throw new Exception(response.ErrorMessage);
        }

        return null;
    }

    public HashSet<string> GetDestination()
    {
        SendRequest(UtilsProtocolJSON.GetDestinationRequest());
        Response response = ReadResponse();
        if (response.Type == ResponseType.RECEIVE_DESTINAITON)
        {
            HashSet<string> origin = response.Towns;
            return origin;
        }

        if (response.Type == ResponseType.ERROR)
        {
            throw new Exception(response.ErrorMessage);
        }

        return null;
    }

    public List<Flight> SearchFlight(Flight flight)
    {
        SendRequest(UtilsProtocolJSON.GetSearch(flight));
        Response response = ReadResponse();
        if (response.Type == ResponseType.RECEIVE_SEARCH)
        {
            IEnumerable<FlightDTO> flights = response.Flights;
            List<Flight> flightList = new List<Flight>();
            foreach (var flightDto in flights)
            {
                flightList.Add(DTOUtils.getFromDTO(flightDto));
            }

            return flightList;
        }

        if (response.Type == ResponseType.ERROR)
        {
            throw new Exception(response.ErrorMessage);
        }

        return null;
    }
}