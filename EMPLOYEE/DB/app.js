const express = require("express");
const app = express();
const _ = require("lodash");
const bodyParser = require("body-parser");
const cookieParser = require("cookie-parser");
const session = require("express-session");
const path = require("path");
var responseClient = require('./Public/util').responseClient;

const PORT = process.env.PORT || 3002;

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
app.use(bodyParser.json());

// database configration
var USER = require("./Database/user.js");
var TICKET = require("./Database/tickets.js");

// set up router
app.get('/', (req, res) =>{
    console.log("Established a connection !");

    var body = _.pick(req.body,["ps", "username"]);
    console.log(req);
    res.setHeader("Access-Control-Allow-Origin","*");
    res.send("Hello admin db system");
});

app.post('/login', (req, res) =>{
    var body = _.pick(req.body,["username", "ps"]);
    console.log(req);
    // console.log(body);
    if(!body.username){
        responseClient(res, 200, 0, "user name cannot be none!");
    }

    if(!body.ps){
        responseClient(res, 200, 0, "password cannot be none!");
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
    responseClient(res, 400, 1, "Please login !");
})

app.post('/signup', (req, res) =>{
    var body = _.pick(req.body,["ps", "username", "email"]);
    var response = USER.insert(body);
    if(response.res==0){
        responseClient(res, 400, response.res, `Username: ${body.username} exist, please choose another one !`);
    }else{
        responseClient(res, 200, response.res, "Thanks for signup !");
    }
});


app.post('/verifytickets', (req, res) =>{
    var body = _.pick(req.body,["id", "email"]);
    var response = TICKET.issue(body);
    // console.log(response)
    // if(response.res==0){
        responseClient(res, 400, "", `Unable to process the request !`);
    // }else{
    //     responseClient(res, 200, "", "Your ticket is printed !");
    // }
});


// establish HTTP connection
app.listen(PORT, () =>{
    console.log(`My REST API is running on port ${PORT} !`);
})

// app.listen(3000, "192.168.56.1", () =>{
//     console.log("My REST API is running on LAN port 3000 !");
// })
