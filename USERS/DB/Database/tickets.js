const mysql = require('mysql');
const config = require("./config");
const uniqid = require('uniqid');

var connection = mysql.createConnection(config.db);

var grantTicket = function(body) {
  let createTICKET = `create table if not exists TICKETS(
    id varchar(255) primary key,
    email varchar(255) not null,
    value int not null,
    type int not null,
    expire DATE not null,
    valid boolean default true
  )`;
  connection.query(createTICKET, (err, results, fields)=>{
    if(err){
      console.log(err.message);
    }
  });
  var id = uniqid(), checked, result, identifier=id;
  let sql = `SELECT * FROM TICKETS WHERE id='${id}'`;
  connection.query(sql, (err, results, fields)=>{
    if(err){
      console.log(err.message);
      id = undefined;
      checked=0;
      result = 0;
      return;
    }
    if(results){
      id = uniqid();
      identifier= id;
    }
    checked=1;
  });

  while(checked==undefined) {
    require('deasync').runLoopOnce();
  }
  let insertTICKET = `INSERT INTO TICKETS (id, email, value, type, expire)VALUES('${id}', '${body.email}', '${body.value}','${body.type}','${body.expire}')`
  var response;
  if(checked==1){
    connection.query(insertTICKET, (err, results, fields)=>{
      if(err){
        console.log(err.message);
        response = 0;
        result = 0;
      }
      result=1;
    });
  }
  while(result==undefined) {
    require('deasync').runLoopOnce();
  }
  if(result==1){
    sql = `SELECT * FROM TICKETS WHERE id='${identifier}'`;
    connection.query(sql, (err, results, fields)=>{
      if(err){
        console.log(err.message);
        return;
      }
      response = results[0];
    });
  }
  while(response==undefined) {
    require('deasync').runLoopOnce();
  }
  return {result, response};
}

var queryUserTicket = function(body) {
  let sqlQuery = `SELECT * FROM TICKETS WHERE email='${body}'`;
  // let sqlQuery = `SELECT * FROM TICKETS WHERE id='59v7e52rwk3cnmzqv'`;
  var res;
  connection.query(sqlQuery, (err, results, fields)=>{
    if(err){
      console.log(err.message);
    }
    res = results;
  });

  while(res==undefined) {
    require('deasync').runLoopOnce();
  }

  return res;
}


module.exports = {
  grantTicket: grantTicket,
  queryUserTicket: queryUserTicket
}
