import React from 'react';
import {Route, BrowserRouter as Router} from 'react-router-dom'
import './App.css';
import {Quote} from './quote/Quote'
import {Admin} from './admin/Admin'
import {Login} from './admin/Login'

export const App = () => (
    <div className='App'>
        <Router>
            <Route exact path='/' component={Quote}/>
            <Route exact path='/quote' component={Quote}/>
            <Route exact path='/login' component={Login}/>
            <Route path='/admin' component={Admin}/>
        </Router>
    </div>
);
