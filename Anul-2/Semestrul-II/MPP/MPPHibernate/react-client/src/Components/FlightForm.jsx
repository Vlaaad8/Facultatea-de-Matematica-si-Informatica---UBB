import {useState} from "react";
import "../FlightForm.css";

export default function FlightForm({addFunc}) {
    const [origin, setOrigin] = useState("");
    const [departure, setDeparture] = useState("");
    const [airport, setAirport] = useState("");
    const [daytime, setDaytime] = useState(new Date());
    const [availableSeats, setAvailableSeats] = useState("");

    function handleSubmit(event) {
        event.preventDefault();
        addFunc({origin, departure, airport, daytime, availableSeats});
        setOrigin('')
        setDeparture('')
        setAirport('')
        setDaytime(new Date());
        setAvailableSeats('');
    }

    function formatForInput(d) {
        if (!d) return "";
        const pad = (n) => String(n).padStart(2, "0");
        return (
            d.getFullYear() +
            "-" +
            pad(d.getMonth() + 1) +
            "-" +
            pad(d.getDate()) +
            "T" +
            pad(d.getHours()) +
            ":" +
            pad(d.getMinutes()) +
            ":" +
            pad(d.getSeconds())
        );
    }

    return (
        <form className="card-form" onSubmit={handleSubmit}>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="origin" className="input-label">
                    Origin
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={origin}
                    onChange={(e) => setOrigin(e.target.value)}
                    placeholder=" "
                    id="origin"
                />
            </div>

            {/** Departure **/}
            <label htmlFor="departure" className="input-label">
                Departure
            </label>
            <div className="input">
                <input
                    type="text"
                    className="input-field"
                    value={departure}
                    onChange={(e) => setDeparture(e.target.value)}
                    placeholder=" "
                    id="departure"
                />
            </div>

            {/** Airport **/}
            <div className="input">
                <label htmlFor="airport" className="input-label">
                    Airport
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={airport}
                    onChange={(e) => setAirport(e.target.value)}
                    placeholder=" "
                    id="airport"
                />
            </div>

            {/** Daytime **/}
            <div className="input">
                <label htmlFor="daytime" className="input-label">
                    Daytime
                </label>
                <input
                    type="datetime-local"
                    className="input-field"
                    step="1"
                    value={formatForInput(daytime)}
                    onChange={(e) => setDaytime(new Date(e.target.value))}
                    id="daytime"
                />

            </div>

            {/** Available Seats **/}
            <div className="input">
                <label htmlFor="seats" className="input-label">
                    Available Seats
                </label>
                <input
                    type="number"
                    className="input-field"
                    value={availableSeats}
                    onChange={(e) => setAvailableSeats(e.target.value)}
                    placeholder=" "
                    id="seats"
                />
            </div>

            <button type="submit" className="add-button">
                Add Flight
            </button>
        </form>
    );
}
