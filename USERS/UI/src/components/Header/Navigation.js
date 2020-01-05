import React from "react";
import {Navbar, Form, Nav, NavDropdown, FormControl, Button} from "react-bootstrap";
import axios from 'axios';

const Navigation = () => {
    var res;
    const checkUSER = async () =>{
        await axios.get('/userinfo')
        .then(function (response) {
            // _this.setState({
                res= response
                // console.log(response)
                // isLoaded:true
            // })
        })
        .catch(function (error) {
            // handle error
            console.log(error)
            // _this.setState({
            //     isLoaded:false
            // })            
        })
        .finally(function () {
            // always executed
            // console.log(res.data.code)  
        });
    }
    checkUSER();
    console.log(res)
    if(res !== undefined){
        console.log("LOG LOG LOG")
    }else{
        console.log("666")
    }
    return (
        <div>
            <Navbar bg="light" expand="lg">
                <Navbar.Brand href="/">Amazing Park</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="mr-auto">
                    <Nav.Link href="/">Home</Nav.Link>
                    <Nav.Link href="/about">About</Nav.Link>
                    <Nav.Link href="/tickets">Tickets</Nav.Link>
                    <NavDropdown title="My Account" id="basic-nav-dropdown">
                    <NavDropdown.Item href="/login">Sign In</NavDropdown.Item>
                    <NavDropdown.Item href="/signup">Sign Up</NavDropdown.Item>
                    <NavDropdown.Item href="/myaccount">My Account</NavDropdown.Item>
                    <NavDropdown.Divider />
                    <NavDropdown.Item href="#action/3.4">Forget Password</NavDropdown.Item>
                    </NavDropdown>
                </Nav>
                <Form inline>
                    <FormControl type="text" placeholder="Search" className="mr-sm-2" />
                    <Button variant="outline-success">Search</Button>
                </Form>
                </Navbar.Collapse>
            </Navbar>
        </div>
    );
};

export default Navigation;