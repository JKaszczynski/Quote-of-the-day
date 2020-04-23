export const authenticate = (credentials) => {
    const header = {
        "Authorization": `Basic ${window.btoa(credentials.username + ":" + credentials.password)}`
    };
    return sendRequest(header);
};

export const isAuthenticated = async () => {
    const response = await sendRequest()
        .catch(() => {
            return {status: 404};
        });
    return response.status === 200;
};

const sendRequest = (headers = {}) => {
    return fetch("http://localhost:8080/auth", {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
            ...headers
        },
        credentials: "include"
    })
};