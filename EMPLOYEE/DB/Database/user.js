const mysql = require('mysql');
const config = require("./config");
const QuickEncrypt = require('quick-encrypt');

var connection = mysql.createConnection(config.db);

var parserUser = function(body) {
  // Encrypt
  let keys = QuickEncrypt.generate(1024);
  let publicKey = keys.public;
  let encryptedPS = QuickEncrypt.encrypt( body.ps, publicKey);
  // USER table
  let createUSER = `create table if not exists ADMINUSERS(
    id int primary key auto_increment,
    username varchar(255) not null UNIQUE,
    email varchar(255) not null
  )`;
  // PASS table
  let createPASS = `create table if not exists ADMINPASS(
    id int primary key auto_increment,
    password varchar(1024) not null,
    privatekey varchar(1024) not null
  )`;
  // SQL
  let insertDATA = `INSERT INTO ADMINUSERS (username, email)VALUES('${body.username}', '${body.email}')`
  let insertPASS = `INSERT INTO ADMINPASS (password, privatekey)VALUES('${encryptedPS}', '${keys.private}')`;
  let sqlSelect = `SELECT * from ADMINUSERS where username='${body.username}'`
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

  var duplicate, res, user; // if success, res=1; if fail, res=0
  // check exist username, 1=exist; 0=not exist;
  connection.query(sqlSelect, (err, result) => {
    if(err){
      console.log(err);
      res = 0;
    }
    if(result.length==0){
      duplicate = 0;
    }else{
      duplicate = 1;
    }
  })
  while(duplicate==undefined) {
    require('deasync').runLoopOnce();
  }
  // insert an user row
  if(duplicate == 0){ // for username does not exist, insert one
    connection.query(insertDATA, (err, result)=>{
      if(err){
        console.log(err);
        res = 0;
      }
      user = result;
    });
    connection.query(insertPASS, (err, result)=>{
      if(err){
        console.log(err);
        res = 0;
      }
    });
    res = 1;
  }else{
    res = 0;
    user= 0;
  }
  while(user==undefined || res==undefined) {
    require('deasync').runLoopOnce();
  }
  return {res, user};
}

var comparePASS = function(body) {
  let sql = `SELECT * FROM USERS WHERE username='${body.username}'`;
  // create table USERS if not exist
  var comp, user, result; // success: result=1; deny: result=0; User not exist: result=-1;
  connection.query(sql, (err, USER, fields)=>{
    if(err){
      console.log(err.message);
      comp=-1; user=0;result=0;
      return;
    }
    while(USER == undefined) {
      require('deasync').runLoopOnce();
    }
    if(USER.length==0){
      comp = -1;
      user = -1;
      result= -1;
      return;
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
      comp = decryptedPS.localeCompare(body.ps);
      if(comp==0){
        result=1;
        user=USER[0];
      }else{
        result=0;
        user=0;
      }
    })
  })
  while(comp == undefined || user == undefined || result==undefined) {
      require('deasync').runLoopOnce();
  }
  return {result, user};
}


module.exports = {
  insert: parserUser,
  compare: comparePASS
}
