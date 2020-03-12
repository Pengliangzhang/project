const mysql = require('mysql');
const config = require("./config");
const configAli = require("./configAli");


var connection = mysql.createConnection(config.db);

module.exports = {
    connection:connection
}