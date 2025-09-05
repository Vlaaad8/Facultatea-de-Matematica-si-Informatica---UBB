using System.Net.Http.Headers;
using System.Text;
using MPP_CSharpProject.domain;
using Newtonsoft.Json;

namespace RestService
;

public class Program
{
    public static string URL = "http://localhost:8081/ryanair/flights";

    static async Task Main(string[] args)
    {
        using var client = new HttpClient
        {
            BaseAddress = new Uri(URL)
        };
        client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        
        await GetAll(client);
        Console.WriteLine("GetAll :"+URL);
        int id = 3;
        await GetById(client, id);
        Console.WriteLine($"GetById: {URL}/{id}");
        
        FlightJSON flightJson=new FlightJSON("C# Origin12121","C# Destination","C# Airport",200,DateTime.Now);
        FlightJSON flightAdded= await AddFlight(client, flightJson);
        Console.WriteLine("AddFlight :"+URL);
        FlightJSON flightToUpdate=new FlightJSON("C# Origin12121","C# Destination","C# Airport",203,DateTime.Now);
        flightToUpdate.Id=flightAdded.Id;
        await updateFlight(client, flightToUpdate,flightAdded.Id);
        
        await GetAll(client);
        Console.WriteLine("GetAll :"+URL);
        
        await delete(client,flightAdded.Id);
        await GetAll(client);
        Console.WriteLine("GetAll :"+URL);
    }


    private static async Task GetAll(HttpClient client)
        {
            var response = await client.GetAsync(URL);
            var json = await response.Content.ReadAsStringAsync();
            var list = JsonConvert.DeserializeObject<List<FlightJSON>>(json);
            foreach(var flight in list){
                Console.WriteLine(flight.Id+" "+flight.Origin+" "+flight.Departure+" "+flight.Airport+" "+flight.DayTime+" "+flight.Seats);
                
        }
    }

    private static async Task GetById(HttpClient client, int id)
    {
        var response = await client.GetAsync($"{URL}/{id}");
        var json = await response.Content.ReadAsStringAsync();
        if (response.StatusCode == System.Net.HttpStatusCode.OK)
        {
            var flight = JsonConvert.DeserializeObject<FlightJSON>(json);
            Console.WriteLine(flight.Id+" "+flight.Origin+" "+flight.Departure+" "+flight.Airport+" "+flight.DayTime+" "+flight.Seats);
        }
    }

    private static async Task<FlightJSON> AddFlight(HttpClient client, FlightJSON toAddflight)
    {
        var settings = new JsonSerializerSettings {
            DateFormatString = "yyyy-MM-dd'T'HH:mm:ss",
            DateTimeZoneHandling = DateTimeZoneHandling.Unspecified
        };

        var jsonLoad = JsonConvert.SerializeObject(toAddflight, settings);
        var content = new StringContent(jsonLoad, Encoding.UTF8, "application/json");
        var response = await client.PostAsync(URL, content);
        var responseBody = await response.Content.ReadAsStringAsync();
        if (response.StatusCode == System.Net.HttpStatusCode.OK)
        {
            var json= await response.Content.ReadAsStringAsync();
            var flight = JsonConvert.DeserializeObject<FlightJSON>(json);
            Console.WriteLine(flight.Id+" "+flight.Origin+" "+flight.Departure+" "+flight.Airport+" "+flight.DayTime+" "+flight.Seats);
            return flight;
        }

        return null;
    }

    private static async Task updateFlight(HttpClient client, FlightJSON toUpdateflight, int id)
    {
        var settings = new JsonSerializerSettings {
            DateFormatString = "yyyy-MM-dd'T'HH:mm:ss",
            DateTimeZoneHandling = DateTimeZoneHandling.Unspecified
        };

        var jsonLoad = JsonConvert.SerializeObject(toUpdateflight, settings);
        var content = new StringContent(jsonLoad, Encoding.UTF8, "application/json");
        var response = await client.PutAsync($"{URL}/{id}", content);
        var responseBody = await response.Content.ReadAsStringAsync();
        Console.WriteLine("Update Status Code: "+response.StatusCode);
    }

    public static async Task delete(HttpClient client, int id)
    {
        var response = await client.DeleteAsync($"{URL}/{id}");
        var responseBody = await response.Content.ReadAsStringAsync();
        Console.WriteLine("Delete Status Code: "+response.StatusCode);
    }
}