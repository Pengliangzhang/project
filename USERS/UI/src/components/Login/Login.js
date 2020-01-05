import React from "react";
// import { MDBContainer, MDBRow, MDBCol, MDBInput, MDBBtn } from 'mdbreact';
import axios from 'axios';


class Login extends React.Component{
    constructor(props){
        super(props)
        this.state={
            username:"",
            ps:""
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
        if (this.state.ps==='' || this.state.username===''){
            console.log("NONE")
        }else{
            var res;

            await axios.post('/userlogin', {
                ps: this.state.ps,
                username: this.state.username
            })
            .then(function (response) {
                res = response;
                console.log(res);
            })
            .catch(function (error) {
                console.log(error);
            });
            console.log(res)
            var data = res.data;
            if (data.code===1){
                window.location.href = '/myaccount'
            }else if (data.code===0){
                window.location.href = '/login'
            }
            //;
        }
    }

    render(){
        return (
            <div>
                姓名：<input name="username" value={this.state.username} onChange={this.handleChange}/>
                密码：<input name="ps" type='password' value={this.state.password} onChange={this.handleChange}/>
               <input type='submit' value="登录" onClick={this.up}/>
               {/* <MDBContainer>
                <MDBRow>
                    <MDBCol md="6">
                    <form>
                        <p className="h5 text-center mb-4">Sign in</p>
                        <div className="grey-text">
                        <MDBInput
                            label="Type your username"
                            icon="envelope"
                            group
                            type="text"
                            error="wrong"
                            success="right"
                            name="username" 
                            value={this.state.username} 
                            onChange={this.handleChange}
                            required
                        />
                        <MDBInput
                            label="Type your password"
                            icon="lock"
                            group
                            type="password"
                            name="ps"
                            value={this.state.password} 
                            onChange={this.handleChange}
                        />
                        </div>
                        <div className="text-center">
                        <MDBBtn type='submit' onClick={this.up}>Login</MDBBtn>
                        </div>
                    </form>
                    </MDBCol>
                </MDBRow>
                </MDBContainer> */}
               
            </div>
        )
    }
}

export default Login;