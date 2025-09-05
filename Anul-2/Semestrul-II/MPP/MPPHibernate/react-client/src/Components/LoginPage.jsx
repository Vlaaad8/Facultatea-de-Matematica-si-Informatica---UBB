import {useState} from "react";
import '../CSS/LoginPage.css'

function LoginPage({loginFunction}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    function handleSubmit(event) {
        event.preventDefault();
        console.log(username);
        console.log(password);
        console.log(loginFunction);
        return loginFunction(username, password);
    }

    return (

        <form className="card-form" onSubmit={handleSubmit}>
            {/** Username **/}
            <div className="input">
                <label htmlFor="username" className="input-label">
                    Username
                </label>
                <input
                    type="text"
                    className="input-field"
                    placeholder=" "
                    onChange={(e) => setUsername(e.target.value)}
                    id="username"
                />
            </div>

            {/** Password **/}
            <label htmlFor="password" className="input-label">
                Password
            </label>
            <div className="input">
                <input
                    type="password"
                    className="input-field"
                    placeholder=" "
                    onChange={(e) => setPassword(e.target.value)}
                    id="password"
                />
            </div>
            <button type="submit" className="add-button">
                Login
            </button>
        </form>
    )
}

export default LoginPage;