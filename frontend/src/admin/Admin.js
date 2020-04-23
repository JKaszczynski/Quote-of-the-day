import React from 'react';
import {Redirect} from "react-router-dom";
import {isAuthenticated} from "./authentication";

const quoteReducer = (state, action) => {
    switch (action.type) {
        case 'SET_QUOTE':
            return {
                ...state,
                quote: action.payload.quote,
                message: ""
            };
        case 'SET_AUTHOR':
            return {
                ...state,
                author: action.payload.author,
                message: ""
            };
        case 'SET_MESSAGE':
            return {
                ...state,
                message: action.payload.message
            };
        case 'RESET_QUOTE_AUTHOR':
            return {
                ...state,
                quote: "",
                author: "",
            };
        default:
            return {...state}
    }
};

export const Admin = () => {
    const [authenticated, setAuthenticated] = React.useState(false);
    const [loading, setLoading] = React.useState(true);
    const [quote, dispatchQuote] = React.useReducer(
        quoteReducer,
        {quote: "", author: "", message: ""}
    );

    React.useEffect(() => {
        isAuthenticated().then(result => {
            console.log(result);
            setAuthenticated(result);
            setLoading(false);
        });
    }, []);


    const handleQuoteChange = (event) => dispatchQuote({
        type: "SET_QUOTE",
        payload: {quote: event.target.value}
    });

    const handleAuthorChange = (event) => dispatchQuote({
        type: "SET_AUTHOR",
        payload: {author: event.target.value}
    });

    const handleQuoteSubmit = async (event) => {
        event.preventDefault();
        const response = await saveQuote();
        if (response.status === 201) {
            dispatchQuote({
                type: "RESET_QUOTE_AUTHOR",
            });
            dispatchQuote({
                type: "SET_MESSAGE",
                payload: {message: "Successfully persisted quote"}
            });
        } else {
            dispatchQuote({
                type: "SET_MESSAGE",
                payload: {message: response.statusText}
            });
        }
    };

    const saveQuote = () => {
        return fetch("http://localhost:8080", {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify(quote)
        })
    };

    if (loading) {
        return "";
    }
    if (!authenticated) {
        return <Redirect to={"/login"}/>
    }
    return (
        <form onSubmit={handleQuoteSubmit}>
            {quote.message}
            <label htmlFor="quote">Quote</label>
            <textarea id="quote"
                      value={quote.quote}
                      onChange={handleQuoteChange}/>

            <label htmlFor="author">Author</label>
            <input type="text"
                   id="author"
                   value={quote.author}
                   onChange={handleAuthorChange}/>
            <input type="submit" value="Add quote"/>
        </form>
    )
};