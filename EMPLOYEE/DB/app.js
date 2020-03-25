const express = require("express");
const app = express();
const _ = require("lodash");
const bodyParser = require("body-parser");
const cookieParser = require("cookie-parser");
const session = require("express-session");
const path = require("path");
const cors = require('cors');
var responseClient = require('./Public/util').responseClient;

const PORT = process.env.PORT || 3005;
// setup cros
var corsOptions = {
    origin: ['http://localhost:2999'],
    credentials: true,
    maxAge: '900000'
  }
app.use(cors())

// set up cookie for login
app.use(cookieParser('express_react_cookie'));
app.use(session({
    secret:'express_react_cookie',
    resave:true,
    saveUninitialized:true,
    cookie:{maxAge: 60 * 1000 * 30} //30 mins for session expired
}))

app.use('/',express.static(path.join(__dirname,"..",'public')));

// configuration for app to allow json format
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// database configration
var USER = require("./Database/user.js");
var TICKET = require("./Database/tickets.js");
var FOB = require("./Database/fob.js");

// set up router
app.get('/', (req, res) =>{
    responseClient(res, 200, 0, `Thanks for using EMPLOYEE DB`);
});

app.post('/login', (req, res) =>{
    var body = _.pick(req.body,["username", "ps"]);
    console.log(body)
    if(!body.username){
        return responseClient(res, 200, 0, "user name cannot be none!");
    }

    if(!body.ps){
        return responseClient(res, 200, 0, "password cannot be none!");
    }

    var response = USER.compare(body);
    if(response.result==-1){
        responseClient(res, 200, 0, "User does not exist !");
    }
    else if(response.result==1){
        req.session.userInfo = response.user;
        responseClient(res, 200, 1, "Thanks for loging in !");
    }else{
        responseClient(res, 200, 0, "User name or password do not exist !");
    }
});

app.get('/userInfo', (req, res)=>{
    if(req.session.userInfo){
        responseClient(res, 200, 1, req.session.userInfo);
    }else{
        responseClient(res, 200, 0, "Please login !");
    }
})

app.get('/logout', (req, res)=>{
    req.session.userInfo = undefined;
    responseClient(res, 400, 1, "Thanks for logging in !");
})

app.post('/adminsignup', (req, res) =>{
    var body = _.pick(req.body,["ps", "username", "email"]);
    console.log(body)
    if(body.ps === undefined || body.username === undefined){
        return responseClient(res, 200, 0, "Please enter required information");
    }
    var response = USER.insert(body);
    if(response.res==0){
        responseClient(res, 200, response.res, `Username: ${body.username} exist, please choose another one !`);
    }else{
        responseClient(res, 200, response.res, "Thanks for signup !");
    }
});

app.post('/activatefob', (req, res) =>{
    var body = _.pick(req.body,["fobid", "ticketID"]);
    console.log(body)
    if(body.fobid === undefined || body.ticketID === undefined || body.fobid ==''){
        return responseClient(res, 200, 0, "Please enter required information");
    }
    var response = FOB.activate(body);
    if(response.res == 1){
        responseClient(res, 200, 1, "The fob has been activated");
    }else{
        responseClient(res, 200, 0, "The fob been assigned to others");
    }
});

app.post('/deletefob', (req, res) =>{
    var body = _.pick(req.body,["fobid"]);
    console.log(body)
    if(body.fobid === undefined){
        return responseClient(res, 200, 0, "Please enter required information");
    }
    var response = FOB.deactivated(body);
    if(response.res == 1){
        responseClient(res, 200, 1, "The fob has been de-activated");
    }else{
        responseClient(res, 200, 0, "No such fob to de-activated");
    }
});


app.post('/verifytickets', (req, res) =>{
    var body = _.pick(req.body,["id"]);
    console.log(body)
    if(body.id === undefined || body.id === ''){
        return responseClient(res, 200, 0, `A id is required to provide !`);
    }
    var response = TICKET.verify(body);

    if(response.status==-1){
        responseClient(res, 200, -1, `Not a valid ticket !`);
    }else if (response.status==0){
        responseClient(res, 200, 0, "Your ticket has been used !");
    }else if (response.status==1){
        responseClient(res, 200, 1, "Welcome !");
    }
});


app.get('/queryalltickets', (req, res) =>{
    var response = TICKET.queryAll();
    responseClient(res, 200, 1, response);
});


// establish HTTP connection
app.listen(PORT, () =>{
    console.log(`My REST API is running on port ${PORT} !`);
})

// app.listen(3000, "192.168.56.1", () =>{
//     console.log("My REST API is running on LAN port 3000 !");
// })
