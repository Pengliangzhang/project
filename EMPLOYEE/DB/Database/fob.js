const QuickEncrypt = require('quick-encrypt');

const connection = require("./connection.js").connection;

var activateFOB = function(body) {

  let createFOBTable = `create table if not exists activedFOB(
    fobID int primary key,
    ticketID varchar(255) not null
  )`;

  let insertDATA = `INSERT INTO activedFOB (ticketID, fobID)VALUES('${body.ticketID}', '${body.fobid}')`
  let sqlSelect = `SELECT * from activedFOB where fobID='${body.fobid}'`

  connection.query(createFOBTable, (err, results, fields)=>{
    if(err){
      console.log(err.message);
    }
  });

  var duplicate, res, fob;
  // {res} if success, res=1; if fail, res=0
  // {duplicate} check exist username, 1=exist; 0=not exist;

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
  });

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
      fob = result;
    });
    res = 1;
  }else{
    res = 0;
    fob= 0;
  }

  while(fob==undefined || res==undefined) {
    require('deasync').runLoopOnce();
  }
  return {res, fob};
}

var deactivateFOB = function(body) {
    let sqlDelete = `DELETE from activedFOB where fobID='${body.fobid}'`
    var res;
    connection.query(sqlDelete, (err, result) => {
      if(err){
        console.log(err);
        res = 0;
      }
      if(result.affectedRows == 1){
        res = 1;
      }else{
        res = 0
      }
    });

    while(res==undefined) {
      require('deasync').runLoopOnce();
    }
    return {res};
}

module.exports = {
  activate: activateFOB,
  deactivated: deactivateFOB
}
