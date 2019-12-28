import React from "react";
import { MDBContainer, MDBRow, MDBCol, MDBInput, MDBBtn } from 'mdbreact';
import axios from 'axios';


const Login = () => {
    return (
    <div> 
        <MDBContainer>
        <MDBRow>
            <MDBCol md="6">
            <form>
                <p className="h5 text-center mb-4">Sign in</p>
                <div className="grey-text">
                <MDBInput
                    label="Type your email"
                    icon="envelope"
                    group
                    type="email"
                    validate
                    error="wrong"
                    success="right"
                />
                <MDBInput
                    label="Type your password"
                    icon="lock"
                    group
                    type="password"
                    validate
                />
                </div>
                <div className="text-center">
                <MDBBtn onClick={()=>{ SignIN() }}>Login</MDBBtn>
                </div>
            </form>
            </MDBCol>
        </MDBRow>
        </MDBContainer>
    </div>
    )
}


async function makePostRequest() {

    var params = {
        "ps": "1",
        "username": "a"
      }

    let res = await axios.post('http://192.168.0.23:3000/login', params);

    console.log(res.data);
}

// makePostRequest();

const SignIN = ()=>{
    console.log("Clicked ! ");
    var res = 0;
    axios.post('/userlogin', {
        ps: "1",
        username: "a"
    })
    .then(function (response) {
        console.log(response);
        res = response;
    })
    .catch(function (error) {
        console.log(error);
    });
    console.log(res)
}

export default Login;