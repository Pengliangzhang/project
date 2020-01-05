import React from "react";
// import { MDBContainer, MDBRow, MDBCol, MDBBtn, MDBInput } from 'mdbreact';
import axios from 'axios';


class SignUP extends React.Component{
  constructor(props){
      super(props)
      this.state={
          username:"",
          ps:"",
          email:""
      }
      this.handleChange=this.handleChange.bind(this);
      this.up=this.up.bind(this);
  }
  handleChange(event){
      // 读取输入的值
    const name=event.target.name;
    const value=event.target.value;
    //   更新状态
    this.setState({
        [name]:value
    })
  }
  async up(){
      console.log(this.state)
      if (this.state.ps==='' || this.state.username==='' || this.state.email===''){
          console.log("NONE")
      }else{
          var res;
          await axios.post('/usersignup', {
              ps: this.state.ps,
              username: this.state.username,
              email: this.state.email
          })
          .then(function (response) {
              console.log(response);
              res = response;
          })
          .catch(function (error) {
              console.log(error);
          });
          console.log(res)
          // window.location.href = '/login';
      }
  }

  render(){
      return (
          <div>
             Username：<input name="username" value={this.state.username} onChange={this.handleChange}/>
             Email：<input name="email" value={this.state.email} onChange={this.handleChange}/>
             Password：<input name="ps" type='password' value={this.state.password} onChange={this.handleChange}/>
             <input type='submit' value="Submit" onClick={this.up}/>
            {/* <MDBContainer>
            <MDBRow>
              <MDBCol md="6">
                <form>
                  <p className="h5 text-center mb-4">Sign up</p>
                  <div className="grey-text">
                    <MDBInput
                      
                      label="User name"
                      icon="user"
                      group
                      type="text"
                      validate
                      error="wrong"
                      success="right"
                      value={this.state.username} 
                      onChange={this.handleChange}
                    />
                    <MDBInput
                      value={this.state.email} 
                      onChange={this.handleChange}
                      label="Your email"
                      icon="envelope"
                      group
                      type="email"
                      validate
                      error="wrong"
                      success="right"
                    />
                    <MDBInput
                      value={this.state.confirmEmail} 
                      onChange={this.handleChange}
                      label="Confirm your email"
                      icon="exclamation-triangle"
                      group
                      type="text"
                      validate
                      error="wrong"
                      success="right"
                    />
                    <MDBInput
                      value={this.state.ps} 
                      onChange={this.handleChange}
                      label="Your password"
                      icon="lock"
                      group
                      type="password"
                      validate
                    />
                  </div>
                  <div className="text-center">
                    <MDBBtn color="primary" type='submit' onClick={this.up}>Register</MDBBtn>
                  </div>
                </form>
              </MDBCol>
            </MDBRow>
            </MDBContainer>              */}
          </div>
      )
  }
}

export default SignUP;