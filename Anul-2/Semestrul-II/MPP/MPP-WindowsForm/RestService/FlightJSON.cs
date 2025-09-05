using Newtonsoft.Json;

namespace RestService;

public class FlightJSON
{
        [JsonProperty("id")] public int Id { get; set; }
        [JsonProperty("origin")] public string Origin { get; set; }
        [JsonProperty("departure")] public string Departure { get; set; }
        [JsonProperty("airport")] public string Airport { get; set; }
        [JsonProperty("availableSeats")] public int Seats { get; set; }
        [JsonProperty("daytime")] public DateTime DayTime { get; set; }
        
        public FlightJSON(int id, string origin,string destination,string airport,int seats,DateTime dateTime)
        {
                Id = id;
                Origin = origin;
                Departure = destination;
                Airport = airport;
                Seats = seats;
                DayTime = dateTime;
        }
        public FlightJSON(string origin,string destination,string airport,int seats,DateTime dateTime)
        {
                Origin = origin;
                Departure = destination;
                Airport = airport;
                Seats = seats;
                DayTime = dateTime;
        }
        public FlightJSON()
        {
                // constructorul gol e tot ce trebuie
        }
}