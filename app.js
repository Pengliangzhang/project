const express = require("express");
const app = express();
const _ = require("lodash");
const bodyParser = require("body-parser");

var tools = require("./Database/connect.js");
// const user = require("./Database/Client");

// configuration for app
app.use(bodyParser.json());
app.get('/', (req, res) =>{
    console.log("Established a connection !");
    res.send("Hello youtube")
});

app.post('/login.js', (req, res) =>{

    var body = _.pick(req.body,["ps", "username", "email"]);
    var response = tools.print(body);
    res.end("Thanks !")
});


// establish HTTP connection
app.listen(3000, () =>{
    console.log("My REST API is running on port 3000 !");
})

app.listen(3000, "192.168.56.1", () =>{
    console.log("My REST API is running on LAN port 3000 !");
})