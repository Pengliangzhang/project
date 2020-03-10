const express = require("express");
const app = express();
const _ = require("lodash");
const bodyParser = require("body-parser");
const cookieParser = require("cookie-parser");
const session = require("express-session");
const path = require("path");
const cors = require('cors');
var url = require('url');
var responseClient = require('./Public/util').responseClient;
var qr = require('qr-image');


const PORT = process.env.PORT || 3001;

// setup cros
var corsOptions = {
  origin: 'http://localhost:3000',
  credentials: true,
  maxAge: '900000'
}
app.use(cors(corsOptions))

// set up cookie for login
app.use(cookieParser('express_react_cookie'));
app.set('trust proxy', 1)

app.use(
  session({
    secret:
      "express_react_cookie",
    resave: true,
    saveUninitialized: true
    // cookie: { secure: true, maxAge: 60000 }
  }))

app.use('/public',express.static(path.join(__dirname,"..",'public')));

// configuration for app to allow json format
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// database configration
var USER = require("./Database/user.js");
var TICKET = require("./Database/tickets.js");
var PARKING = require("./Database/parking.js");

var sess;

// set up router
app.get('/', (req, res) =>{
    responseClient(res, 200, 0, req.session);
});

app.get('/userInfo', (req, res)=>{
    // console.log(req.session);
    // if(sess != undefined){
    if(req.session.user != undefined){
        // responseClient(res, 200, 1, sess.user);
        responseClient(res, 200, 1, req.session.user);
    }else{
        responseClient(res, 200, 0, "Please login !");
    }
})

app.get('/logout', (req, res)=>{
    req.session.destroy();
    responseClient(res, 200, 1, "Please login !");
})

app.get('/getusertickets', (req, res) =>{
    if(req.session.user == undefined){
        return responseClient(res, 200, 0, "unable to query data");
    }else{
      var response = TICKET.queryUserTicket(req.session.user.email);
      responseClient(res, 200, 1, response);
    }

})

app.post('/login', (req, res) =>{
    var body = _.pick(req.body,["username", "ps"]);
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
        req.session.user = response.user;
        // res.setHeader("Access-Control-Allow-Origin","*");
        // res.send({"found":"1"})
        // sess = req.session;
        // sess.user = response.user
        responseClient(res, 200, 1, req.session);
    }else{
        responseClient(res, 200, 0, "User name or password do not exist !");
    }
});

app.post('/signup', (req, res) =>{
    var body = _.pick(req.body,["ps", "username", "email"]);
    var response = USER.insert(body);
    if(response.res==0){
        responseClient(res, 200, response.res, `Username: ${body.username} exist, please choose another one !`);
    }else{
        responseClient(res, 200, response.res, "Thanks for signup !");
    }
});

app.post('/buytickets', (req, res) =>{
    var body = _.pick(req.body,["value", "type", "expire", "email"]);
    // body.email = "beck@beck.com";
    if(req.session.user){
        body.email = req.session.user.email;
    }
    var response = TICKET.grantTicket(body);
    if(response.result==1){
        responseClient(res, 200, 1, response.response);
    }else{
        responseClient(res, 200, 0, 'Unable to process the request !');
    }
});

app.post('/buyparkingspot', (req, res) =>{
    var body = _.pick(req.body,["plate"]);
    if(req.session.user){
        body.username = req.session.user.username;
    }
    var response = PARKING.buyParking(body);
    if(response.result==1){
        responseClient(res, 200, response.result, "Your booked a parking spot !");
    }else{
        responseClient(res, 200, response.result, `Unable to process the request !`);
    }
});

app.get('/create_qrcode', (req, res) =>{
    var text = req.query.id;
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
