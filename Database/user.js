const mysql = require('mysql');
const config = require("./config");
const QuickEncrypt = require('quick-encrypt');

var connection = mysql.createConnection(config.db);

var parserUser = function(body) {
  console.log(body);
  let keys = QuickEncrypt.generate(1024);
  console.log(keys.public);
  console.log(keys.private);

  // Encrypt
  let publicKey = keys.public;
  let encryptedPS = QuickEncrypt.encrypt( body.ps, publicKey);
  console.log("Encrypted: "+encryptedText);

  // Decrypt
  let privateKey = keys.private;
  let decryptedText = QuickEncrypt.decrypt(encryptedText, privateKey);
  console.log("Decrypted: " + decryptedText);

  let createUSER = `create table if not exists USERS(
    id int primary key auto_increment,
    username varchar(255) not null,
    email varchar(255) not null,
    password varchar(1024) not null
  )`;

  let insertDATA = `INSERT INTO USERS (username, email, password)VALUES('${body.username}', '${body.email}', '${encryptedPS}')`
  connection.connect(function(err) {
    if (err) {
      console.error('Database connection failed: ' + err.stack);
      return 0;
    }
    console.log('Connected to database.');
  });  
  // create table USERS if not exist
  connection.query(createUSER, (err, results, fields)=>{
    if(err){
      console.log(err.message);      
    }
  });

  connection.query(insertDATA, (err, result)=>{
    if(err){
      console.log(err);
      throw err;
    }
  });
  // connection.end();
}

var comparePASS = function(body) {

} 


module.exports = {
  insert: parserUser,
  compare: comparePASS
}