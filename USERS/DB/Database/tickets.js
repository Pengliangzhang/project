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

var issueTicket = function(body) {
  var result, status, sql, update, checked;
  if(body.id){
    update = `UPDATE TICKETS 
           SET valid='false'
           WHERE id='${body.id}'`;
    sql = `SELECT * FROM TICKETS WHERE id='${body.id}'`;
    checked=1;
  }else if(body.email){
    update = `UPDATE TICKETS 
           SET valid='false'
           WHERE email=${body.email}`;
    sql = `SELECT * FROM TICKETS WHERE email='${body.email}'`;
    checked=1;
  }else{
    status = 0, result=0;
    checked=1;
  }
  while(checked==undefined) {
    require('deasync').runLoopOnce();
  }
  var found;
  if(status!=0){
    connection.query(sql, (err, results, fields)=>{
      if(err){
        console.log(err);
        status = 0, result=0;
        return;
      }
      found = results;
    })
  }
  while(found==undefined) {
    require('deasync').runLoopOnce();
  }
  if(!found[0].valid){
    status = 0, result=0;
  }
  connection.query(sql, (err, results, fields)=>{
    if(err){
      console.log(err.message);
      status = 0;
      result = 0;
      return;      
    }
    // console.log(results)
    result = results;
    status = 1;
  });
  
  while(result==undefined || status==undefined) {
    require('deasync').runLoopOnce();
  }
  console.log(status+ " " + result)
  return {status, result};
}


module.exports = {
  grantTicket: grantTicket,
  issue:issueTicket
}

