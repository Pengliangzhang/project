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
// app.options('*', cors(corsOptions));

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
var QUEUE = require("./Database/queue.js");

var sess;

// set up router
app.get('/', (req, res) =>{
    responseClient(res, 200, 0, "Thanks for using USERS DB");
});

app.get('/userInfo', (req, res)=>{
    if(req.session.user != undefined){
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
        return responseClient(res, 200, 0, "unable to query data, please login first");
    }else{
      var response = TICKET.queryUserTicket(req.session.user.email);
      responseClient(res, 200, 1, response);
    }

})

app.get('/getqueue', (req, res) =>{
    if(req.session.user == undefined){
        return responseClient(res, 200, 0, "unable to query data, please login first");
    }else{
      var responseIDs = QUEUE.queryUSERs(req.session.user.email);
      var response = [];
      for(var i=0; i<responseIDs.results.length; i++) {
            // console.log(responseIDs.results[i].id)
            var responseQUEUE = QUEUE.updateposition(responseIDs.results[i].id, 'rollercoaster');
            var msg;
            // console.log(responseIDs.results[i].id + " num of visitors in front " + responseQUEUE.result)
            if(responseQUEUE.result == -1){
                var msg = "You do not in the queue !"
            } else if (responseQUEUE.result == -2) {
                msg = "Please come to the waitting zone!"
            } else if (responseQUEUE.result >= 0) {
                msg = "There has " + responseQUEUE.result + " in front of you. "
            } else {
                msg = "There has " + responseQUEUE.result + " in front of you. "
            }
            var json = {id: responseIDs.results[i].id, msg: msg}
            response[i] = json;
      }
      responseClient(res, 200, 1, response);
    }
})

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
        req.session.user = response.user;
        responseClient(res, 200, 1, req.session);
    }else{
        responseClient(res, 200, 0, "User name or password do not exist !");
    }
});

app.post('/signup', (req, res) =>{
    var body = _.pick(req.body,["ps", "username", "email"]);
    console.log(body)
    var response = USER.insert(body);
    if(response.res==0){
        responseClient(res, 200, response.res, `Username: ${body.username} exist, please choose another one !`);
    }else{
        responseClient(res, 200, response.res, "Thanks for signup !");
    }
});

app.post('/buytickets', (req, res) =>{
    var body = _.pick(req.body,["value", "type", "expire", "email"]);
    console.log(body)
    if(body.type == null || body.email==null || body.expire==null || body.value==null){
        return responseClient(res, 200, 0, 'Please fill out required information !');
    }
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
    console.log(body)
    if(req.session.user){
        body.username = req.session.user.username;
    }else {
        console.log(req.session.user)
        return responseClient(res, 200, 0, "please login first !");
    }
    var response = PARKING.buyParking(body);
    if(response.result==1){
        responseClient(res, 200, response.result, "Your booked a parking spot !");
    }else{
        responseClient(res, 200, response.result, `Unable to process the request !`);
    }
});

app.get('/queryparkingspot', (req, res) =>{
    var username;
    if(req.session.user){
        username = req.session.user.username;
    }else {
        return responseClient(res, 200, 0, "please login first !");
    }
    var response = PARKING.queryParkingSPOT(username);

    if(response.result==1){
        responseClient(res, 200, response.result, response.response);
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
