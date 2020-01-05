import React from "react";
// import { MDBContainer, MDBRow, MDBCol, MDBBtn, MDBInput } from 'mdbreact';
import axios from 'axios';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

class Ticket extends React.Component{
  constructor(props){
      super(props)
      this.state={
          username:"",
          ps:"",
          email:"",
          startDate: new Date(),
      }
      this.handleChange=this.handleChange.bind(this);
      this.up=this.up.bind(this);
  }
  handleChange(event){
    //   读取输入的值
    const name=event.target.name;
    const value=event.target.value;
    //   更新状态
    this.setState({
        [name]:value,
    })
  }
  handleDateChange = date => {
    this.setState({
        startDate: date,
    });
  };
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
            <form>
                Username：<input name="username" value={this.state.username} onChange={this.handleChange}/><br/>
                Email：<input name="email" value={this.state.email} onChange={this.handleChange}/><br/>
                Password：<input name="ps" type='password' value={this.state.password} onChange={this.handleChange}/><br/>
                Date: <DatePicker
                    selected={this.state.startDate}
                    onChange={this.handleDateChange}
                /><br/>
                <input type='submit' value="Submit" onClick={this.up}/>
            </form>
             
          </div>
      )
  }
}

export default Ticket;