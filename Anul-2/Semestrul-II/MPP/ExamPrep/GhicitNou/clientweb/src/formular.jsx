import {useState} from "react";

export default function FlightForm({addFunc}) {
    const [id, setId] = useState("");
    const [value1, setValue1] = useState("");
    const [value2, setValue2] = useState("");
    const [value3, setValue3] = useState("");
    const [value4, setValue4] = useState("");
    function handleSubmit(event) {
        event.preventDefault();
        addFunc({id: id, value1:value1, value2:value2, value3:value3,value4:value4});
        setId("");
        setValue1("");
        setValue2("");
        setValue3("");
        setValue4("");

    }


    return (
        <form className="card-form" onSubmit={handleSubmit}>
            {/** Origin **/}
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
                <label htmlFor="value1" className="input-label">
                    Value1
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={value1}
                    onChange={(e) => setValue1(e.target.value)}
                    id="value1"
                />
            </div>
            <div className="input">
                <label htmlFor="value2" className="input-label">
                    Value2
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={value2}
                    onChange={(e) => setValue2(e.target.value)}
                    id="value2"
                />
            </div>

            <div className="input">
                <label htmlFor="value3" className="input-label">
                    Value1
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={value3}
                    onChange={(e) => setValue3(e.target.value)}
                    id="value3"
                />
            </div>

            <div className="input">
                <label htmlFor="value4" className="input-label">
                    Value1
                </label>
                <input
                    type="text"
                    className="input-field"
                    value={value4}
                    onChange={(e) => setValue4(e.target.value)}
                    id="value4"
                />
            </div>

            <button type="submit" className="add-button">
                Add Config
            </button>
        </form>
    );
}
