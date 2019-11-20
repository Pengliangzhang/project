const express = require("express");
const app = express();
const _ = require("lodash");
const bodyParser = require("body-parser");
const deasync = require("deasync")

var USER = require("./Database/user.js");
// const user = require("./Database/Client");

// configuration for app to allow json format
app.use(bodyParser.json());

// set up router
app.get('/', (req, res) =>{
    console.log("Established a connection !");
    res.send("Hello youtube")
});

app.post('/login', (req, res) =>{
    var body = _.pick(req.body,["username", "ps"]);

    var response = USER.compare(body);
    while(response == undefined) {
        require('deasync').runLoopOnce();
    }
    res.end("Thanks ! " + response);  
});

app.post('/signup', (req, res) =>{
    var body = _.pick(req.body,["ps", "username", "email"]);
    var response = USER.insert(body);
    res.end("Thanks !")
});

app.post('/tickets', (req, res) =>{
    var body = _.pick(req.body,["ps", "username", "email"]);
    var response = USER.insert(body);
    res.end("Thanks !")
});

app.post('/payment', (req, res) =>{
    var body = _.pick(req.body,["ps", "username", "email"]);
    var response = USER.insert(body);
    res.end("Thanks !")
});


// establish HTTP connection
app.listen(3000, () =>{
    console.log("My REST API is running on port 3000 !");
})

app.listen(3000, "192.168.56.1", () =>{
    console.log("My REST API is running on LAN port 3000 !");
})