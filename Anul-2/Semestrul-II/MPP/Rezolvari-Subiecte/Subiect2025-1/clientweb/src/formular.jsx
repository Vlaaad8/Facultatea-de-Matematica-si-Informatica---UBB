import {useState} from "react";

export default function FlightForm({addFunc}) {
    const [coordI, setCoordI] = useState("");
    const [coordJ, setCoordJ] = useState("");
    const [animal, setAnimal] = useState("");
    const [animalLink, setAnimalLink] = useState("");

    function handleSubmit(event) {
        event.preventDefault();
        addFunc({i: i.get, j: coordJ, animal: animal, animalLink: animalLink});
        setCoordI('')
        setCoordJ('')
        setAnimal('')
        setAnimalLink('')

    }


    return (
        <form className="card-form" onSubmit={handleSubmit}>
            {/** Origin **/}
            <div className="input">
                <label htmlFor="i" className="input-label">
                    I
                </label>
                <input
                    type="number"
                    className="input-field"
                    value={coordI}
                    onChange={(e) => setCoordI(e.target.value)}
                    id="i"
                />
            </div>

            {/** Departure **/}
            <label htmlFor="j" className="input-label">
                J
            </label>
            <div className="input">
                <input
                    type="number"
                    className="input-field"
                    value={coordJ}
                    onChange={(e) => setCoordJ(e.target.value)}
                    id="j"
                />
            </div>

            {/** Airport **/}
            <div className="input">
                <label htmlFor="animal" className="input-label">
                    Animal
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={animal}
                    onChange={(e) => setAnimal(e.target.value)}
                    id="animal"
                />
            </div>
            <div className="input">
                <label htmlFor="animalLink" className="input-label">
                    Animal Link
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={animalLink}
                    onChange={(e) => setAnimalLink(e.target.value)}
                    id="animalLink"
                />
            </div>
            <button type="submit" className="add-button">
                Add Config
            </button>
        </form>
    );
}
