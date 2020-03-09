const express = require("express");
const session = require("express-session");
const http = require('http');
var app = express();
const hbs = require("hbs");


app.use('/public',express.static(__dirname+'/../src/public/'));
app.use("/js", express.static(__dirname + "/../views/Javascript"));
app.use("/css", express.static(__dirname + "/../views/stylesheet"));


app.set("view engine", "hbs");

hbs.registerPartials(__dirname + "/../views/components");
hbs.registerPartials(__dirname + "/../views/components/home");
hbs.registerPartials(__dirname + "/../views/components/login");
hbs.registerPartials(__dirname + "/../views/components/header");
hbs.registerPartials(__dirname + "/../views/components/admin");

app.get("/", (req, res) => {
    res.status(200).render("./template/home/home.hbs");
});

app.get("/login", (req, res) => {
    res.status(200).render("./template/login/login.hbs");
});

app.get("/admin", (req, res) => {
    res.status(200).render("./template/admin/admin.hbs");
});



module.exports = { app, express};