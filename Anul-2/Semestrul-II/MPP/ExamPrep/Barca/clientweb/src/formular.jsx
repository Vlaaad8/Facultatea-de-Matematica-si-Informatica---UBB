import {useState} from "react";

export default function FlightForm({addFunc}){
    const [value1, setValue1] = useState("");
    const [value2, setValue2] = useState("");
    const [value3, setValue3] = useState("");
    function handleSubmit(event) {
        event.preventDefault();
        addFunc({value1:value1, value2:value2, value3:value3});
        setValue1("");
        setValue2("");
        setValue3("");

    }


    return (
        <form className="card-form" onSubmit={handleSubmit}>
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

            <button type="submit" className="add-button">
                Add Config
            </button>
        </form>
    );
}
