import LoginPage from "./LoginPage.jsx";
import {Login} from "../RestCalls.js";
import App from "../App.jsx";
import {useState} from "react";

function Start() {
    const [token, setToken] = useState("");

    async function LoginEmployee(username,password){
        try{
            let newToken=await Login(username,password);
            console.log(newToken);
            setToken(newToken);
        }
        catch(err){
            console.error(err);
        }
    }
    if(token) {
        return (
            <App token={token}/>
        )
    }
    return (
        <>
        <h1>Welcome - Login to Ryanair - revolutionary aviation</h1>
        <LoginPage loginFunction={LoginEmployee} />
            </>);

}
export default Start;