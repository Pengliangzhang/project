const express = require("express");
const app = express();
const _ = require("lodash");
const bodyParser = require("body-parser");
const cookieParser = require("cookie-parser");
const session = require("express-session");
const path = require("path");
var responseClient = require('./Public/util').responseClient;
var qr = require('qr-image');

const PORT = process.env.PORT || 3000;

// set up cookie for login
app.use(cookieParser('express_react_cookie'));
app.use(session({
    secret:'express_react_cookie',
    resave:true,
    saveUninitialized:true,
    cookie:{maxAge: 60 * 1000 * 30} //30 mins for session expired
}))

app.use('/public',express.static(path.join(__dirname,"..",'public')));

// configuration for app to allow json format
app.use(bodyParser.json());

// database configration 
var USER = require("./Database/user.js");
var TICKET = require("./Database/tickets.js");
var PARKING = require("./Database/parking.js");

// set up router
app.get('/', (req, res) =>{
    responseClient(res, 400, 0, req.session);
});

app.post('/login', (req, res) =>{
    var body = _.pick(req.body,["username", "ps"]);
    if(!body.username){
        return responseClient(res, 400, 0, "user name cannot be none!");
    }

    if(!body.ps){
        return responseClient(res, 400, 0, "password cannot be none!");
    }

    var response = USER.compare(body);
    if(response.result==-1){
        responseClient(res, 400, 0, "User does not exist !");
    }
    else if(response.result==1){
        req.session.userInfo = response.user;
        responseClient(res, 200, 1, "Thanks for loging in !");
    }else{
        responseClient(res, 400, 0, "User name or password do not exist !");
    }
});

app.get('/userInfo', (req, res)=>{
    if(req.session.userInfo){
        responseClient(res, 200, 1, req.session.userInfo);
    }else{
        responseClient(res, 400, 0, "Please login !");
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

app.post('/buytickets', (req, res) =>{
    var body = _.pick(req.body,["value", "type", "expire", "email"]);
    if(req.session.userInfo){
        body.email = req.session.userInfo.email;
    }
    var response = TICKET.grantTicket(body);
    if(response.res==0){
        responseClient(res, 400, response.result, `Unable to process the request !`); 
    }else{
        responseClient(res, 200, response.response, "Your ticket is printed !");
    }
});

app.post('/buyparkingspot', (req, res) =>{
    var body = _.pick(req.body,["plate"]);
    if(req.session.userInfo){
        body.username = req.session.userInfo.username;
    }
    var response = PARKING.buyParking(body);
    if(response.result==0){
        responseClient(res, 400, response.result, `Unable to process the request !`); 
    }else{
        responseClient(res, 200, response.result, "Your booked a parking spot !");
    }
});

app.post('/create_qrcode', (req, res) =>{
    var body = _.pick(req.body,["id"]);
    var text = body.id;
    try {
        var img = qr.image(text,{size :10});
        res.writeHead(200, {'Content-Type': 'image/png'});
        img.pipe(res);
    } catch (e) {
        res.writeHead(414, {'Content-Type': 'text/html'});
        res.end('<h1>414 Request-URI Too Large</h1>');
    }
})



// establish HTTP connection
app.listen(PORT, () =>{
    console.log(`My REST API is running on port ${PORT} !`);
})