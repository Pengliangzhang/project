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
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// database configration
var USER = require("./Database/user.js");
var TICKET = require("./Database/tickets.js");

// set up router
app.get('/', (req, res) =>{
    res.send("Hello admin db system");
});

app.post('/login', (req, res) =>{
    var body = _.pick(req.body,["username", "ps"]);
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
    responseClient(res, 400, 1, "Thanks for logging in !");
})

app.post('/adminsignup', (req, res) =>{
    var body = _.pick(req.body,["ps", "username", "email"]);
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


app.post('/verifytickets', (req, res) =>{
    var body = _.pick(req.body,["id"]);
    if(body.id === undefined){
        responseClient(res, 200, 0, `A id is required to provide !`);
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
