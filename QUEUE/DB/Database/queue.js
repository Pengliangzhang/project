const mysql = require('mysql');
const config = require("./config");
const uniqid = require('uniqid');

var connection = mysql.createConnection(config.db);

var enqueue = function(body) {
    let id=body.id, facility=body.facility;
    let dequeue = facility+'DE';

    let createQUEUE = `create table if not exists ${facility}(
        id int primary key auto_increment,
        ticketID varchar(255)
      )`;

    let createDEQUEUE = `create table if not exists ${dequeue}(
        id int primary key auto_increment,
        ticketID varchar(255)
      )`;

    let insertENQUEUE = `INSERT INTO ${facility} ( ticketID ) VALUES ('${id}')`;
    let queryQUEUE = `SELECT * FROM ${facility} WHERE ticketID='${id}'`;
    let queryFirstQUEUE = `SELECT * FROM ${facility} LIMIT 1`;
    let queryDEQUEUE = `SELECT * FROM ${dequeue} WHERE ticketID='${id}'`;
    var exist, res, result, first;;
    // exist: false=not exist; true=exist; -1=checked queryONE
    // res: -1=error; 0=already enqueue; 1=success

    connection.query(createQUEUE, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
    });
    connection.query(createDEQUEUE, (err, results, fields)=>{
        if(err){
          // console.log(err.message);
        }
    });

    connection.query(queryFirstQUEUE, (err, results, fields)=>{
        if(err){
          console.log(err.message);
          res = -1;
          result = -1;
        }
        if(results.length != 0){
            first = results[0];
        } else {
            first = 0;
        }

        res = 1;
    })


    connection.query(queryQUEUE, (err, results, fields)=>{
        if(err){
          console.log(err.message);
          res = -1;
          result = -1;
        }
        if(results.length == 0){
            exist = -1;
        }else {
            exist = true;
        }
    });


    while(exist==undefined || first==undefined) {
      require('deasync').runLoopOnce();
    }
    if (exist == -1){
      connection.query(queryDEQUEUE, (err, results, fields)=>{
          if(err){
            console.log(err.message);
            res = -1;
            result = -1;
          }
          if(results.length == 0){
              exist = false;
          }else{
              exist = true;
          }
      });
    }
    while(exist==undefined || exist==-1) {
      require('deasync').runLoopOnce();
    }

    if(!exist){
        connection.query(insertENQUEUE, (err, results, fields)=>{
            if(err){
              console.log(err.message);
              res = -1;
              result = -1;
            }
            console.log(first.id);
            console.log(results.insertId + " rowID " + first.id);
            console.log();
            result = results.insertId - first.id;
            res = 1;
        })
    }else{
        res = 0;
        result = -1;
    }
    while(result==undefined || res==undefined) {
      require('deasync').runLoopOnce();
    }
    return {res, result};
}


var queueTransfer = function() {
    let sqlDelete = 'delete from rollercoaster where rownum between 1 and 5'
    connection.query(sqlDelete, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
        console.log(results);
    })

}


module.exports = {
  enqueue: enqueue
}
