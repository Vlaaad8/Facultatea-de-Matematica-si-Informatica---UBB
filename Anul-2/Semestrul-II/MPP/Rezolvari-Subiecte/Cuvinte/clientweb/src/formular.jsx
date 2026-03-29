import {useState} from "react";

export default function FlightForm({addFunc}) {
    const [letter, setLetter] = useState("");
    const [word1, setWord1] = useState("");
    const [word2, setWord2] = useState("");
    const [word3, setWord3] = useState("");
    const [word4, setWord4] = useState("");


    function handleSubmit(event) {
        event.preventDefault();
        addFunc({letters: letter, word1: word1, word2: word2, word3:word3,word4:word4});
        setLetter('')
        setWord1('')
        setWord2('')
        setWord3('')
        setWord4('')

    }


    return (
        <form className="card-form" onSubmit={handleSubmit}>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="letter" className="input-label">
                    Letters
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={letter}
                    onChange={(e) => setLetter(e.target.value)}
                    id="letter"
                />
            </div>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="word1" className="input-label">
                    Word1
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={word1}
                    onChange={(e) => setWord1(e.target.value)}
                    id="word1"
                />
            </div>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="word2" className="input-label">
                    Word2
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={word2}
                    onChange={(e) => setWord2(e.target.value)}
                    id="word2"
                />
            </div>
            <div className="input">
                <label htmlFor="word3" className="input-label">
                    Word3
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={word3}
                    onChange={(e) => setWord3(e.target.value)}
                    id="word3"
                />
            </div>
            <div className="input">
                <label htmlFor="word4" className="input-label">
                    Word4
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={word4}
                    onChange={(e) => setWord4(e.target.value)}
                    id="word4"
                />
            </div>

            <button type="submit" className="add-button">
                Add Config
            </button>
        </form>
    );
}
