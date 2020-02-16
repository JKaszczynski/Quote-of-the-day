import React from 'react';

export default class Quote extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            quote: {
                quote: "",
                author: ""
            }
        };
    }

    componentDidMount() {
        this.getQuote();
    }

    getQuote() {
        let xmlHttpRequest = new XMLHttpRequest();
        xmlHttpRequest.addEventListener('load', () =>
                this.setState({quote: JSON.parse(xmlHttpRequest.responseText)})
        );
        xmlHttpRequest.open('GET', 'http://localhost:8080');
        xmlHttpRequest.send();
    }

    render() {
        return (
            <div className='Quote'>
                <div>{this.state.quote.quote}</div><br/>
                <div className='Author'> ~ {this.state.quote.author}</div>
            </div>
        );
    }
}