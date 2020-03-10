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
  origin: 'http://localhost:2999',
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
// setInterval((()=>{
//     count++;
//     console.log("set interval is working ! " + count);
//     console.log()
// }), 5000, Date.now())

// set up router
app.get('/', (req, res) =>{
    responseClient(res, 200, 0, req.session);
});

app.post('/enqueue', (req, res) =>{
    var body = _.pick(req.body,["id", "facility"]);
    // body.facility:   1: program one
    //                  2: program two
    var isNUM = /^\d+$/.test(body.id);

    var response = queue.enqueue(body);
    console.log(response);
    if (response.res == 1) {
        responseClient(res, 200, 1, response.result);
    } else if(response.res == 0) {
        responseClient(res, 200, 0, `You already enqueued into ${body.facility} line`);
    } else {
        responseClient(res, 200, 0, "An error was found");
    }
});

app.post('/dequeue', (req, res) =>{
    var body = _.pick(req.body,["id"]);
    var response = USER.insert(body);

    responseClient(res, 200, response.res, "Thanks for signup !");

});


// establish HTTP connection
app.listen(PORT, () =>{
    console.log(`My REST API is running on port ${PORT} !`);
})
