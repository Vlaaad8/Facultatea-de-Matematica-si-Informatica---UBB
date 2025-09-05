using System.Net.Sockets;
using System.Text;
using System.Text.Json;
using DefaultNamespace;
using log4net;
using Microsoft.VisualBasic.CompilerServices;
using MPP_CSharpProject.domain;
using Networking.dto;
using Services;

namespace Networking.json;

public class WorkerJSON : IObserver
{
    private IServices services;
    private TcpClient connection;

    private NetworkStream networkStream;
    private volatile bool connected;
    private static readonly ILog log = LogManager.GetLogger(typeof(WorkerJSON));

    public WorkerJSON(IServices services, TcpClient connection)
    {
        this.services = services;
        this.connection = connection;
        try
        {
            networkStream = connection.GetStream();
            connected = true;
        }
        catch (Exception e)
        {
           log.Error(e);
        }
    }

    public virtual void Run()
    {
        using StreamReader streamReader = new StreamReader(networkStream, Encoding.UTF8);
        while (connected)
        {
            try
            {
                string requestJSON = streamReader.ReadLine();
                if (string.IsNullOrEmpty(requestJSON)) continue;
                Request request = JsonSerializer.Deserialize<Request>(requestJSON);
                Response response = handleRequest(request);
                if (response != null)
                {
                    SendResponse(response);
                }
            }
            catch (Exception e)
            {
                log.Error(e);
            }

            try
            {
                Thread.Sleep(1000);
            }
            catch (Exception e)
            {
                log.Error(e);
            }
        }

        try
        {
            networkStream.Close();
            connection.Close();
        }
        catch (Exception e)
        {
            log.Error(e);
        }
    }

    private void SendResponse(Response response)
    {
        String jsonString = JsonSerializer.Serialize(response);
        lock (networkStream)
        {
            byte[] data = Encoding.UTF8.GetBytes(jsonString + "\n");
            networkStream.Write(data, 0, data.Length);
            networkStream.Flush();
        }
    }


    private Response handleRequest(Request request)
    {
        Response response = null;
        if (request.RequestType == RequestType.LOGIN)
        {
           log.Info("Am intrat in request-Login");
            Employee employee = DTOUtils.getFromDTO(request.Employee);
            Employee foundEmployee;
            try
            {
                lock (services)
                {
                    foundEmployee = services.Login(employee, this);
                }

                return new Response { Type = ResponseType.OK };
            }
            catch (Exception e)
            {
                connected = false;
                return UtilsProtocolJSON.CreateErrorResponse(e.Message);
            }
        }

        if (request.RequestType == RequestType.GET_ORIGIN)
        {
            HashSet<string> origins;
            log.Info("Am intrat in request-Origin");
            try
            {
                lock (services)
                {
                    origins = services.GetOrigin();
                }

                return new Response { Type = ResponseType.RECEIVE_ORIGIN, Towns = origins };
            }
            catch (Exception e)
            {
                return UtilsProtocolJSON.CreateErrorResponse(e.Message);
            }
        }

        if (request.RequestType == RequestType.GET_DESTINATION)
        {
            HashSet<string> origins;
            log.Info("Am intrat in request-Destination");
            try
            {
                lock (services)
                {
                    origins = services.GetDestination();
                }

                return new Response { Type = ResponseType.RECEIVE_DESTINAITON, Towns = origins };
            }
            catch (Exception e)
            {
                return UtilsProtocolJSON.CreateErrorResponse(e.Message);
            }
        }

        if (request.RequestType == RequestType.GET_FLIGHTS)
        {
            List<FlightDTO> flightsdto = new List<FlightDTO>();
           log.Info("Am intrat in request-Flights");
            try
            {
                lock (services)
                {
                    IEnumerable<Flight> flights = services.GetAllFlights();
                    foreach (var flight in flights)
                    {
                        FlightDTO flightDto = new FlightDTO(flight.GetId(), flight.Origin, flight.Departure,
                            flight.Airport, flight.DayTime, flight.AvailableSeats);
                        flightsdto.Add(flightDto);
                    }
                }

                log.Info("Trimitem zborurile clientului!");
                return new Response { Type = ResponseType.RECEIVE_FLIGHTS, FlightsDTO = flightsdto };
            }
            catch (Exception e)
            {
                return UtilsProtocolJSON.CreateErrorResponse(e.Message);
            }
        }

        if (request.RequestType == RequestType.SEARCH)
        {
            log.Info("Am intrat in request-Search");
            List<FlightDTO> flightsdto = new List<FlightDTO>();
            Flight flight = DTOUtils.getFromDTO(request.Flight);
            try
            {
                lock (services)
                {
                    List<Flight> flight1 = services.SearchFlight(flight);
                    foreach (var flight11 in flight1)
                    {
                        FlightDTO flightDto = new FlightDTO(flight11.GetId(), flight11.Origin, flight11.Departure,
                            flight11.Airport, flight11.DayTime, flight11.AvailableSeats);
                        flightsdto.Add(flightDto);
                    }
                }

                return new Response { Type = ResponseType.RECEIVE_SEARCH, Flights = flightsdto };
            }
            catch (Exception e)
            {
                return UtilsProtocolJSON.CreateErrorResponse(e.Message);
            }
        }

        if (request.RequestType == RequestType.ADD_TICKET)
        {
            log.Info("Am intrat in request-AddTicket");
            try
            {
                lock (services)
                {
                    services.AddTicket(DTOUtils.getFromDTO(request.Ticket));

                }

                return new Response { Type = ResponseType.OK };
            }
            catch (Exception e)
            {
                return UtilsProtocolJSON.CreateErrorResponse(e.Message);
            }
        }


        return response;
    }

    public void NewTicketBought(Flight flight)
    {
        try
        {
           log.Info("WORKER: Trimit notificarea prin TCP către client...");
            SendResponse(UtilsProtocolJSON.NewTicket(flight));
           log.Info("WORKER: Notificarea trimisă cu succes!");
        }
        catch (Exception e)
        {
            log.Info($"WORKER: Eroare la trimiterea notificării: {e.Message}");
        }
    }
}