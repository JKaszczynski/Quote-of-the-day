import React from 'react';
import './App.css';
import Quote from './Quote'

export default class App extends React.Component {
    render() {
        return (
            <div className='App'>
                <Quote/>
            </div>
        );
    }
}