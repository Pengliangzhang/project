var mysql = require('mysql');

var connection = mysql.createConnection({
    host     : "project-datebase.c2sslqw871wc.us-west-1.rds.amazonaws.com",
    user     : "admin",
    password : "12345678",
    port     : 3306
  });
  
  connection.connect(function(err) {
    if (err) {
      console.error('Database connection failed: ' + err.stack);
      return;
    }
  
    console.log('Connected to database.');
  });
  
  connection.end();

exports.module = {mysql}