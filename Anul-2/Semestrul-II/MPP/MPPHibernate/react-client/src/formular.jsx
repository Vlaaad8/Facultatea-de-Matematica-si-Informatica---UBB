import {useState} from "react";

export default function FlightForm({addFunc}) {
    const [id, setId] = useState("");
    const [letter, setLetter] = useState("");

    function handleSubmit(event) {
        event.preventDefault();
        addFunc({ id: id,configuration: letter});
        setLetter('')
        setId('')

    }


    return (
        <form className="card-form" onSubmit={handleSubmit}>
            {/** IDn **/}
            <div className="input">
                <label htmlFor="id" className="input-label">
                    ID
                </label>
                <input
                    type="number"
                    className="input-field"
                    value={id}
                    onChange={(e) => setId(e.target.value)}
                    id="id"
                />
            </div>
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
               Update Config
            </button>
        </form>
    );
}
