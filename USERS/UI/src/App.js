import React from 'react';
// import logo from './logo.svg';
import './App.css';
import { BrowserRouter, Route, Switch } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';

import Home from "./components/Home/home";
import About from "./components/About/About";
import Login from "./components/Login/Login";
import Error from "./components/Error/Error";
import Navigation from "./components/Header/Navigation";
import Footer from "./components/Footer/Footer";
import SignUP from "./components/Sign Up/SignUp";
import Account from "./components/Account/Account";



function App() {
  return (
    <div className="App">
        <BrowserRouter>
          <Navigation />
          <Switch>
            <Route path="/" component={Home} exact/>
            <Route path="/about" component={About} exact/>
            <Route path="/login" component={Login} exact/>
            <Route path="/myaccount" component={Account} exact/>
            <Route path="/signup" component={SignUP} exact/>            
            <Route component={Error} />
          </Switch>
        </BrowserRouter>
    </div>
  );
}



export default App;
