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
import Footer from "./components/Footer/Footer"


function App() {
  return (
    <div className="App">
        <BrowserRouter>
          <Navigation />
          <Switch>
            <Route path="/" component={Home} exact/>
            <Route path="/about" component={About} exact/>
            <Route path="/login" component={Login} exact/>
            <Route  component={Error} />
          </Switch>
          <Footer />
        </BrowserRouter>
    </div>
  );
}



export default App;
