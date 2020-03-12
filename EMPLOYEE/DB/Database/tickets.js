const uniqid = require('uniqid');

const connection = require("./connection.js").connection;

var verifyTicket = function(body) {
  var sql, update;
  var result, status, checked;
  // result: 0 = verify false; 1 = verify success
  // status: -1 = no such ticket;   0 = ticket is not valid;   1 = ticket is valid
  update = `UPDATE TICKETS
           SET valid='false'
           WHERE id='${body.id}'`;
  sql = `SELECT * FROM TICKETS WHERE id='${body.id}'`;
  checked=1;

  while(checked==undefined) {
    require('deasync').runLoopOnce();
  }

  var found;
  if(status!=0){
    connection.query(sql, (err, results, fields)=>{
      if(err){
        console.log(err);
        status = -1, result=0;
        return;
      }
      found = results;
    })
  }

  while(found==undefined) {
    require('deasync').runLoopOnce();
  }

  if(found.length == 0){
    result = 0;
    status = -1;
    while(result==undefined || status==undefined) {
      require('deasync').runLoopOnce();
    }
    return {status, result};
  }else {
    if(!found[0].valid){
      status = 0, result=0;
    }else{
      connection.query(update, (err, results, fields)=>{
        if(err){
          console.log(err.message);
          status = 0;
          result = 0;
          return;
        }
        result = results;
        status = 1;
      });
    }

    while(result==undefined || status==undefined) {
      require('deasync').runLoopOnce();
    }
    return {status, result};
  }
}

var queryAllTicket = function() {
  var sql = `SELECT * FROM TICKETS`;
  var result = undefined;
  connection.query(sql, (err, results, fields)=>{
    if(err){
      console.log(err.message);
      status = 0;
      result = 0;
      return;
    }
    // console.log(results);
    result = results;
  });
  while(result==undefined) {
    require('deasync').runLoopOnce();
  }
  return result;
}


module.exports = {
  verify:verifyTicket,
  queryAll: queryAllTicket
}
