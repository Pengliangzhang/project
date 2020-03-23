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


const PORT = process.env.PORT || 3003;

// setup cros
var corsOptions = {
  origin: ['http://localhost:2999'],
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
var queue = require("./Database/queue.js");
var count = 0;

queue.newTables();

setInterval((()=>{
    count++;
    var response = queue.queueTransferToWait();
    console.log("Done ");
    var now = new Date();
    console.log(now);
    console.log("Round: " + count + ", following people please come to the waitting zone !");
    console.log(response);
    console.log();
}), 180000)

// set up router
app.get('/', (req, res) =>{
    responseClient(res, 200, 0, `Thanks for using QUEUE DB`);
});

app.post('/enqueue', (req, res) =>{
    var body = _.pick(req.body,["id", "facility"]);
    // body.facility:   1: program one
    //                  2: program two
    console.log(body)
    var isNUM = /^\d+$/.test(body.id);
    var response = queue.enqueue(body);
    if (response.res == 1) {
        responseClient(res, 200, 1, response.result);
    } else if(response.res == 0) {
        responseClient(res, 200, 0, `You already enqueued into ${body.facility} line`);
    } else {
        responseClient(res, 200, 0, "An error was found");
    }
});


app.post('/dequeueFROMwait', (req, res) =>{
    var body = _.pick(req.body,["id", "facility"]);
    console.log(body)
    var response = queue.dequeueFROMwait(body);
    if(response.response.affectedRows == 1){
        responseClient(res, 200, 0, "Welcome to " + body.facility);
    }else{
        responseClient(res, 200, 0, "Cannot find the information in this queue line");
    }
});

app.post('/updateposition', (req, res) =>{
    var body = _.pick(req.body,["id", "facility"]);
    // body.facility:   1: program one
    //                  2: program two
    console.log(body)
    var response = queue.updateposition(body);
    if(response >= 0){
        responseClient(res, 200, 1, response);
    } else {
        responseClient(res, 200, 0, "You are not in the queue");
    }
});

app.get('/queryqueue', (req, res) =>{
    var response = queue.queryQueue();
    responseClient(res, 200, 1, response);
});

// establish HTTP connection
app.listen(PORT, () =>{
    console.log(`My REST API is running on port ${PORT} !`);
})
