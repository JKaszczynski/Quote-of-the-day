import React from "react";
import {Redirect} from "react-router-dom";
import {authenticate, isAuthenticated} from "./authentication";

const credentialsReducer = (state, action) => {
    switch (action.type) {
        case "SET_USERNAME":
            return {
                ...state,
                username: action.payload.username
            };
        case "SET_PASSWORD":
            return {
                ...state,
                password: action.payload.password
            };
        case "SET_AUTHENTICATED":
            return {
                ...state,
                authenticated: action.payload.authenticated,
                loading: false
            };
        case "SET_MESSAGE":
            return {
                ...state,
                message: action.payload.message
            };
        default:
            return {
                ...state
            };
    }
};

export const Login = () => {

        React.useEffect(() => {
            isAuthenticated().then(result => {
                dispatchCredentials({
                    type: "SET_AUTHENTICATED",
                    payload: {
                        authenticated: result
                    }
                })
            })
        }, []);

        const [credentials, dispatchCredentials] = React.useReducer(
            credentialsReducer,
            {username: "", password: "", authenticated: false, loading: true, message: ""}
        );

        const handleUsernameChange = (event) => dispatchCredentials({
            type: "SET_USERNAME",
            payload: {username: event.target.value}
        });
        const handlePasswordChange = (event) => dispatchCredentials({
            type: "SET_PASSWORD",
            payload: {password: event.target.value}
        });

        const handleAuthenticationSubmit = (event) => {
            event.preventDefault();
            authenticate(credentials)
                .then(response => responseResolver(response))
                .catch(() => responseResolver({status: 404}));
        };

        const responseResolver = (response) => {
            if (response.status === 200) {
                dispatchCredentials({
                    type: "SET_AUTHENTICATED",
                    payload: {
                        authenticated: true
                    }
                });
            } else if (response.status === 401) {
                dispatchCredentials({
                    type: "SET_MESSAGE",
                    payload: {message: "Invalid user credentials"}
                });
            } else {
                dispatchCredentials({
                    type: "SET_MESSAGE",
                    payload: {message: "Could not process request, please try again"}
                });
            }
        };

        if (credentials.loading) {
            return "";
        }
        if (credentials.authenticated) {
            return <Redirect to="/admin"/>
        }
        return (
            <form onSubmit={handleAuthenticationSubmit}>
                {credentials.message}
                <br/>
                <label htmlFor="username">Username</label>
                <input type="text"
                       id="username"
                       onChange={handleUsernameChange}
                       value={credentials.username}/>

                <label htmlFor="password">Password</label>
                <input type="password"
                       id="password"
                       onChange={handlePasswordChange}
                       value={credentials.password}/>
                <button type="submit">Login</button>
            </form>
        );
    }
;