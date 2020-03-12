const mysql = require('mysql');
const config = require("./config");
const configAli = require("./configAli");
const uniqid = require('uniqid');

var connection = mysql.createConnection(config.db);
// var connection = mysql.createConnection(configAli.db);

var queryUSERs = function(body) {
    var email = body;
    var sqlQueryAllTicket = `SELECT id FROM (SELECT * FROM TICKETS RIGHT JOIN activedFOB on TICKETS.id = activedFOB.ticketID) AS AllActivedTICKETS WHERE email = '${email}' `;

    var res;
    connection.query(sqlQueryAllTicket, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
        res = results;
    });

    while(res==undefined) {
        require('deasync').runLoopOnce();
    }
    return {results: res};
}

var updateposition = function(inputID, inputFacility){
    let id=inputID, facility=inputFacility, facilityDB = facility+'DE';
    let queryFirstQUEUE = `SELECT * FROM ${facility} LIMIT 1`;
    let queryCurrentQUEUE = `SELECT * FROM ${facility} WHERE ticketID='${id}'`;
    let queryWaittingZone = `SELECT * FROM ${facilityDB} WHERE ticketID='${id}'`;

    var first, current, wait, result;
    // result: if (result >= 0), update how many people in front of
    //         if (result = -1), 
    // query first person in the facility
    connection.query(queryFirstQUEUE, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
        if(results.length != 0){
          first = results[0].id;
        }else{
          first = 0;
        }
    })

    // try to query whether the ticketID in the queue 
    connection.query(queryCurrentQUEUE, (err, results, fields)=>{
        if(err){
          console.log(err.message);
        }
        if(results.length != 0 ){
          current = results[0].id;
        } else {
          current = -1;
        }
    })

    while(first==undefined || current==undefined) {
      require('deasync').runLoopOnce();
    }
    

    if(current == -1){
        // try to query whether the ticketID in the waitting zone, when the ticket not in the queue
        console.log(current)
        connection.query(queryWaittingZone, (err, results, fields)=>{
            if(err){
              console.log(err.message);
            }
            if(results.length != 0 ){
              wait = 1; // in the waitting zone
            } else {
              wait = -2; // not in the waitting zone
            }
        })
    }else {
        wait = -2; // not in the waitting zone
    }

    while(wait==undefined) {
      require('deasync').runLoopOnce();
    }

    console.log("First: " + first);
    console.log("Wait: " + wait);
    console.log("Current: " + current);
    if(current != -1){ // if already in the queue
      result = current - first; // result is the num of people in front of
    }else if (wait == 1){ // if already in the waitting zone
      result = -2;            
    }else if (current == -1) { // not in the queue and waitting zone
      result = current;
    } else {
      result = -3;
    }
    // console.log(result);
    return {result};
}

module.exports = {
    queryUSERs: queryUSERs,
    updateposition: updateposition
}  
