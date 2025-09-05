function status(response) {
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
}

const URL='http://localhost:8081/examen/games'


export function UpdateConfig(config) {
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append("Content-Type", "application/json");
    let myInit = {method: "PUT", headers: headers, body: JSON.stringify(config)};
    let request = new Request(URL ,myInit);
    return fetch(request).then(status)
        .then(response => {
            return response.text();
        })
        .catch(err => {
            console.error(err);
            return new Promise.reject(err);
        })
}