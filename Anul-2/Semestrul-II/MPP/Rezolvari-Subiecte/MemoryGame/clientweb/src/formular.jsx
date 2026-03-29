import {useState} from "react";

export default function FlightForm({addFunc}) {
    const [letter, setLetter] = useState("");

    function handleSubmit(event) {
        event.preventDefault();
        addFunc({configuration: letter});
        setLetter('')

    }


    return (
        <form className="card-form" onSubmit={handleSubmit}>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="letter" className="input-label">
                    Config
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={letter}
                    onChange={(e) => setLetter(e.target.value)}
                    id="letter"
                />
            </div>

            <button type="submit" className="add-button">
                Add Config
            </button>
        </form>
    );
}
