import {useState} from "react";

export default function FlightForm({addFunc}) {
    const [i, setI] = useState("");
    const [j, setJ] = useState("");
    const [gameID, setGameID] = useState("");

    function handleSubmit(event) {
        event.preventDefault();
        addFunc({i: i,j:j,gameID:gameID});
        setI("")
        setJ("")
        setGameID("")

    }


    return (
        <form className="card-form" onSubmit={handleSubmit}>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="i" className="input-label">
                    Config
                </label>
                <input
                    type="number"
                    className="input-field"
                    value={i}
                    onChange={(e) => setI(e.target.value)}
                    id="i"
                />
            </div>

            <div className="input">
                <label htmlFor="j" className="input-label">
                    J
                </label>
                <input
                    type="number"
                    className="input-field"
                    value={j}
                    onChange={(e) => setJ(e.target.value)}
                    id="j"
                />
            </div>

            <div className="input">
                <label htmlFor="gameID" className="input-label">
                    Game ID
                </label>
                <input
                    type="number"
                    className="input-field"
                    value={gameID}
                    onChange={(e) => setGameID(e.target.value)}
                    id="gameID"
                />
            </div>

            <button type="submit" className="add-button">
                Add Config
            </button>
        </form>
    );
}
