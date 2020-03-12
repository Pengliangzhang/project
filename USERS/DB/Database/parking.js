const uniqid = require('uniqid');

const connection = require("./connection");


var buyParkingSPOT = function(body) {
    let createTICKET = `create table if not exists PARKING(
      ticketid varchar(255) primary key,
      username varchar(255),
      plate varchar(255) not null,
      FOREIGN KEY (username) REFERENCES USERS(username)
    )`;
    
    var id = uniqid(), response, result;
    // result: 1=success, 0=fail
    // response sopt info

    let insertPLATE = `INSERT INTO PARKING (ticketid, username, plate)VALUES('${id}', '${body.username}', '${body.plate}')`

    connection.query(createTICKET, (err, results, fields)=>{
      if(err){
        console.log(err.message);      
      }
    });

    connection.query(insertPLATE, (err, results, fields)=>{
      if(err){
        console.log(err.message);
        result = 0;
        response = null;
      }
      result = 1;
      response = results;
    });
    while(result==undefined || response==undefined) {
        require('deasync').runLoopOnce();
        }
    return {result, response};
  }

  module.exports = {
    buyParking: buyParkingSPOT
  }