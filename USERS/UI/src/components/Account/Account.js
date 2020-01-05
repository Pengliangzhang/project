import React from "react";
import axios from 'axios';

class Account extends React.Component{
    constructor(props){
        super(props)
        this.state={
            response:[],
            isLoaded:false
        }
    }
    componentDidMount(){
        let _this = this;
        axios.get('/userinfo')
        .then(function (response) {
            _this.setState({
                response: response,
                isLoaded:true
            })
        })
        .catch(function (error) {
            // handle error
            console.log(error)
            _this.setState({
                isLoaded:false
            })            
        })
        .finally(function () {
            // always executed
            // console.log(res.data.code)  
        });
    }
  
    render(){
        if(!this.state.isLoaded){
            return <div>Loading</div>
        }else{
            if(this.state.response.data.code === 0){
                window.location.href = '/login';
            }else if (this.state.response.data.code === 1){
                return accountDETAIL();
            }else{
                window.location.href = '/error';
            }
        }
    }
}

const accountDETAIL = () => {
    return (
        <div>
            <p> DETAIL </p>
        </div>
    )
}

export default Account;