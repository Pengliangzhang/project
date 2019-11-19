const mysql = require('mysql');
const config = require("./config");
const QuickEncrypt = require('quick-encrypt');

var connection = mysql.createConnection(config.db);

var parserUser = function(body) {
  console.log(body);
  let keys = QuickEncrypt.generate(1024);

  // Encrypt
  let publicKey = keys.public;
  let encryptedPS = QuickEncrypt.encrypt( body.ps, publicKey);

  let createUSER = `create table if not exists USERS(
    id int primary key auto_increment,
    username varchar(255) not null UNIQUE,
    email varchar(255) not null
  )`;

  let createPASS = `create table if not exists PASS(
    id int primary key auto_increment,
    password varchar(1024) not null,
    privatekey varchar(1024) not null
  )`;

  let insertDATA = `INSERT INTO USERS (username, email)VALUES('${body.username}', '${body.email}')`
  let insertPASS = `INSERT INTO PASS (password, privatekey)VALUES('${encryptedPS}', '${keys.private}')`;
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
  connection.query(createPASS, (err, results, fields)=>{
    if(err){
      console.log(err.message);      
    }
  });
  // insert an user row
  connection.query(insertDATA, (err, result)=>{
    if(err){
      console.log(err);
      throw err;
    }
  });
  // insert an PASS row
  connection.query(insertPASS, (err, result)=>{
    if(err){
      console.log(err);
      throw err;
    }
  });
  connection.end();
}

var comparePASS = function(body) {
  let sql = `SELECT * FROM USERS WHERE username='${body.username}'`;
  // create table USERS if not exist
  var comp;
  connection.query(sql, (err, USER, fields)=>{
    if(err){
      console.log(err.message);      
    }
    var id=USER[0].id;
    var sqlPASS = `SELECT * FROM PASS WHERE id='${id}'`;
    connection.query(sqlPASS, (err, PASS, fields)=>{
      if(err){
        console.log(err.message);      
      }
      let pass=PASS[0].password;
      let private=PASS[0].privatekey;
      let decryptedPS = QuickEncrypt.decrypt(pass, private);
      comp = decryptedPS.localeCompare(body.password);      
    })
  })
  while(comp == undefined) {
      require('deasync').runLoopOnce();
  }
  return comp;
} 


module.exports = {
  insert: parserUser,
  compare: comparePASS
}