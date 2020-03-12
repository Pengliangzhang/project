const mysql = require('mysql');
const config = require("./config");

var connection = mysql.createConnection(config.db);

module.exports = {
    connection:connection
}
