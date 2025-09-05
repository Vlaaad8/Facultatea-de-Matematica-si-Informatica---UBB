 import {Component, useEffect, useState} from "react";
 import {GetFlights} from "../RestCalls.js";
 import '../CSS/FlightTable.css'
 import '../App.css'
 function FlightRow({flight,deleteFunction,updateFunction}) {
     const [seats,setSeats] = useState(flight.availableSeats);
     function handleDelete(){
         console.log(flight.id)
         deleteFunction(flight.id)
     }
     useEffect(() => {
         setSeats(flight.availableSeats);
     }, [flight.availableSeats]);

     function handleUpdate(e){
         if (e.key === "Enter") {
             e.preventDefault();
             const newVal = Math.max(0, parseInt(e.target.innerText, 10) || 0);
             setSeats(newVal);
             flight.availableSeats = newVal;
             updateFunction(flight);
             e.target.blur();
         }
         if (e.key === "Escape") {
             e.target.innerText = seats;
             e.target.blur();
         }
     }

     return(
         <tr>
             <td>{flight.origin}</td>
             <td>{flight.departure}</td>
             <td>{flight.airport}</td>
             <td>{flight.daytime}</td>
            <td contentEditable={true} onKeyDown={handleUpdate}>{seats}</td>
             <td>
                 <button onClick={handleDelete}>Delete</button>
             </td>
         </tr>
     )
 }

 export default function FlightTable({flights,deleteFunction,updateFunction}){
     let rows=[];
     flights.forEach(function(flight){
         rows.push(<FlightRow key={flight.id} deleteFunction={deleteFunction} updateFunction={updateFunction} flight={flight}/>)
     });

     return(
         <div className="FlightTable">
             <table className="center">
                 <thead>
                     <tr>
                        <th>Origin</th>
                        <th>Departure</th>
                        <th>Airport</th>
                        <th>DayTime</th>
                        <th>Available Seats</th>
                        <th>Delete</th>
                     </tr>
                 </thead>
                 <tbody>{rows}</tbody>
             </table>
         </div>
     );

 }