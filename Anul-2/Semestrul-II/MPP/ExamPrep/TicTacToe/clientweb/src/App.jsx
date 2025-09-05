import './App.css'
import {AddConfig} from "./RestCalls.js";
import FlightForm from "./formular.jsx";

function App() {

    async function addFunction(configuration) {
        await AddConfig(configuration);
    }

    return (
        <>  <h1>Ryanair - revolutionary aviation</h1>
            <div>{<FlightForm addFunc={addFunction}/>}</div>
        </>
    )
}

export default App
