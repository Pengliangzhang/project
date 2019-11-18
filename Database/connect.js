var mysql = require('mysql');
const config = require("./config");

var connection = mysql.createConnection(config.db);

var parserUser = function(body) {
  console.log(body);
  connection.connect(function(err) {
    if (err) {
      console.error('Database connection failed: ' + err.stack);
      return 0;
    }  
    console.log('Connected to database.');
  });  
}
  


module.exports = {
  print: parserUser
}