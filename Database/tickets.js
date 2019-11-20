var db = require('./connect');

var user = (err, con) =>{
    if (err) {
        console.log(err);
        throw err;
    }
    console.log("Connected!");
    var sql = "CREATE TABLE customers (name VARCHAR(255), address VARCHAR(255))";
    db.query(sql, function (err, result) {
      if (err){
        console.log(err);
        throw err;
      } 
      console.log("Table created");
    });
};


module.exports={user};

