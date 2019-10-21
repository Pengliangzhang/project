const express = require("express");
const app = express();

app.get('/', (req, res) => res.send("Hello youtube"));

app.listen(3000, () =>{
    console.log("My REST API is running on port 3000 !");
})