using System.Text.Json.Serialization;

namespace Networking.dto
{
    public class TicketDTO
    {
        private int _id;
        private FlightDTO _flight;
        private int _numberOfTickets;
        private string _buyers;

        [JsonConstructor]
        public TicketDTO(int Id, FlightDTO Flight, int NumberOfTickets, string Buyers)
        {
            _id = Id;
            _flight = Flight;
            _numberOfTickets = NumberOfTickets;
            _buyers = Buyers;
        }

        public int Id
        {
            get => _id;
        }

        public FlightDTO Flight
        {
            get => _flight;
        }

        public int NumberOfTickets
        {
            get => _numberOfTickets;
        }

        public string Buyers
        {
            get => _buyers;
        }
    }
}