const mysql = require('mysql');
const config = require("./config");
const configAli = require("./configAli");
const uniqid = require('uniqid');

// var connection = mysql.createConnection(config.db);
var connection = mysql.createConnection(configAli.db);

var enqueue = function(body) {
    let id=body.id, facility=body.facility;
    let dequeue = facility+'DE';

    let insertENQUEUE = `INSERT INTO ${facility} ( ticketID) VALUES ('${id}')`;
    let queryQUEUE = `SELECT * FROM ${facility} WHERE ticketID='${id}'`;
    let queryFirstQUEUE = `SELECT * FROM ${facility} LIMIT 1`;
    let queryDEQUEUE = `SELECT * FROM ${dequeue} WHERE ticketID='${id}'`;
    var exist, res, result, first;;
    // exist: false=not exist; true=exist; -1=checked queryONE
    // res: -1=error; 0=already enqueue; 1=success

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


    while(exist==undefined) {
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
            result = results.insertId;
            res = 1;
        })
    }else{
        res = 0;
        result = -1;
    }

    connection.query(queryFirstQUEUE, (err, results, fields)=>{
        if(err){
          console.log(err.message);
          res = -1;
          result = -1;
        }
        if(results.length > 0){
          first = results[0].id;
        }else{
          first = -1;
        }
    })

    while(result==undefined || res==undefined || first == undefined) {
      require('deasync').runLoopOnce();
    }
    result = result-first;
    return {res, result};
}

// @ Dequeue from waitting zone
var dequeueFROMwait = function(body) {
    var id = body.id
    let sqlDelete = `delete from rollercoasterDE where ticketID='${id}' `
    var response;
    connection.query(sqlDelete, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
        response = results;
    })
    while(response==undefined) {
      require('deasync').runLoopOnce();
    }
    return {response}
}


var queueTransferToWait = function() {
    let sqlTransferToWait = 'SELECT * FROM rollercoaster LIMIT 3'
    let sqlTransferToQueue = 'SELECT * FROM rollercoasterDE'
    var deleteMAX;
    let sqlMAXcount = `delete from rollercoasterDE where count>2 `
    connection.query(sqlMAXcount, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
        deleteMAX = results;
    })
    while(deleteMAX==undefined) {
      require('deasync').runLoopOnce();
    }


    var listWAIT, emptyWAIT;
    connection.query(sqlTransferToQueue, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
        listWAIT = results;
    })
    while(listWAIT==undefined) {
      require('deasync').runLoopOnce();
    }


    if(listWAIT.length==0){emptyWAIT = true;}
    for(var i=0; i<listWAIT.length; i++){
        var count = listWAIT[i].count;
        let insertENQUEUE = `INSERT INTO rollercoaster ( ticketID, count ) VALUES ('${listWAIT[i].ticketID}', ${count})`;
        let sqlDelete = `delete from rollercoasterDE where id='${listWAIT[i].id}' `
        var insertStatus, deleteStatus;
        connection.query(insertENQUEUE, (err, results, fields)=>{
            if(err){
              console.log(err.message);
            }
            insertStatus = results;
        })

        connection.query(sqlDelete, (err, results, fields)=>{
            if(err){
              console.log(err.message);
            }
            deleteStatus = results;
        })
        while(insertStatus==undefined || deleteStatus==undefined) {
          require('deasync').runLoopOnce();
        }
        if(i+1 == listWAIT.length){
            emptyWAIT = true;
        }
    }

    while(emptyWAIT==undefined) {
      require('deasync').runLoopOnce();
    }

    var listENQUQUQ;
    connection.query(sqlTransferToWait, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
        listENQUQUQ = results;
    })
    while(listENQUQUQ==undefined) {
      require('deasync').runLoopOnce();
    }
    for(var i=0; i<listENQUQUQ.length; i++){
        var count = listENQUQUQ[i].count + 1;
        let insertENQUEUE = `INSERT INTO rollercoasterDE ( ticketID, count) VALUES ('${listENQUQUQ[i].ticketID}', ${count})`;
        let sqlDelete = `delete from rollercoaster where id='${listENQUQUQ[i].id}' `
        connection.query(insertENQUEUE, (err, results, fields)=>{
            if(err){
              console.log(err.message);
            }
        })

        connection.query(sqlDelete, (err, results, fields)=>{
            if(err){
              console.log(err.message);
            }
        })
    }
    return listENQUQUQ;
}


var newTables = function() {
    let droproller = `DROP TABLE rollercoaster`;
    let droprollerDE = `DROP TABLE rollercoasterDE`;

    let createQUEUE = `create table if not exists rollercoaster(
        id int primary key auto_increment,
        ticketID varchar(255),
        count int NOT NULL DEFAULT 0
      )`;

    let createWaiting = `create table if not exists rollercoasterDE(
        id int primary key auto_increment,
        ticketID varchar(255),
        count int NOT NULL DEFAULT 0
      )`;

    connection.query(droproller, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
    });
    connection.query(droprollerDE, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
    });

    connection.query(createQUEUE, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
    });
    connection.query(createWaiting, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
    });
}

var queryQueueAndWaitting = function(){
    let queryQUEUE = 'SELECT * FROM rollercoaster'
    let queryWaitting = 'SELECT * FROM rollercoasterDE'

    var queue, waitting;

    connection.query(queryQUEUE, (err, results, fields)=>{
      if(err){
        console.log(err.message);
      }
      queue = results;
    })

    connection.query(queryWaitting, (err, results, fields)=>{
      if(err){
        console.log(err.message);
      }
      waitting = results;
    })

    while(queue==undefined || waitting==undefined) {
      require('deasync').runLoopOnce();
    }

    return {
      "queue" : queue,
      "waitting" : waitting
    }

}

module.exports = {
  enqueue: enqueue,
  queueTransferToWait: queueTransferToWait,
  dequeueFROMwait: dequeueFROMwait,
  newTables: newTables,
  queryQueue: queryQueueAndWaitting
}
