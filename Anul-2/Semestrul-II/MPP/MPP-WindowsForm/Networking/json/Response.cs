using MPP_CSharpProject.domain;
using Networking.dto;

namespace Networking.json;

public class Response
{
    public ResponseType Type { get; set; }
    public string ErrorMessage { get; set; }
    public HashSet<String> Towns { get; set; }
    public List<FlightDTO> Flights { get; set; }
    public IEnumerable<FlightDTO> FlightsDTO { get; set; }
    public FlightDTO Flight { get; set; }

    public override string ToString()
    {
        return string.Format("Request[type={0},errorMessage={1},towns={2},fligts={3},flightsdto={4},ticket{5}",Type,ErrorMessage, Towns, Flights, FlightsDTO, Flight);
    }
}