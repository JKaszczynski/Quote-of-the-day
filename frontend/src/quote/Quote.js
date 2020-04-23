import React from "react";

export const Quote = () => {

    const [quote, setQuote] = React.useState({quote: "", author: ""});

    const requestQuote = () => {
        fetch("http://localhost:8080", {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            }
        }).then(response => response.json())
            .then(data => setQuote(data));
    };

    React.useEffect(requestQuote, []);

    React.useEffect(() => {
        const halfHour = 1800000;
        const interval = setInterval(requestQuote, halfHour);
        return () => clearInterval(interval);
    });

    return (
        <div className="Quote">
            <div>
                {quote.quote}
            </div>
            <br/>
            <div className="Author">
                ~ {quote.author}
            </div>
        </div>
    );
};