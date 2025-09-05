import {useState} from "react";

export default function FlightForm({addFunc}) {
    const [i, setI] = useState("");
    const [j, setJ] = useState("");
    const [text, setText] = useState("");



    function handleSubmit(event) {
        event.preventDefault();
        addFunc({i: i, j: j, text: text});
        setI('')
        setJ('')
        setText('')

    }


    return (
        <form className="card-form" onSubmit={handleSubmit}>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="i" className="input-label">
                    I
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={i}
                    onChange={(e) => setI(e.target.value)}
                    id="i"
                />
            </div>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="j" className="input-label">
                    J
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={j}
                    onChange={(e) => setJ(e.target.value)}
                    id="j"
                />
            </div>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="text" className="input-label">
                    Indiciu
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={text}
                    onChange={(e) => setText(e.target.value)}
                    id="text"
                />
            </div>

            <button type="submit" className="add-button">
                Add Config
            </button>
        </form>
    );
}
