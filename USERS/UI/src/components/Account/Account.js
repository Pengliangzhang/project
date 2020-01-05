import React from "react";
import axios from 'axios';
import { Table } from 'react-bootstrap';

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
            <Table striped bordered hover size="sm">
                <thead>
                    <tr>
                    <th>#</th>
                    <th>Ticket ID</th>
                    <th>Ticket Type</th>
                    <th>Purchase Date</th>
                    <th>Expire Date</th>
                    <th>Amount</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    <td>1</td>
                    <td>1234567890123</td>
                    <td>General</td>
                    <td>2020/01/01</td>
                    <td>2020/02/01</td>
                    <td>$ 165</td>
                    </tr>
                    <tr>
                    <td>2</td>
                    <td>Jacob</td>
                    <td>Thornton</td>
                    <td>@fat</td>
                    <td>@fat</td>
                    </tr>
                    <tr>
                    <td>3</td>
                    <td colSpan="2">Larry the Bird</td>
                    <td>@twitter</td>
                    <td>@twitter</td>
                    </tr>
                </tbody>
            </Table>
        </div>
    )
}

export default Account;