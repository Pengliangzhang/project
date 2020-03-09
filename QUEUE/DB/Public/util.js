module.exports = {
    responseClient(res,httpCode, code,message,data) {
        let responseData = {};
        responseData.code = code;
        responseData.message = message;
        responseData.data = data;

        // res.setHeader("Access-Control-Allow-Origin","*");
        // res.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,HEAD,PUT,PATCH");
        // res.setHeader("Access-Control-Max-Age", "36000");
        // res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization,authorization");
        // res.setHeader("Access-Control-Allow-Credentials","true");
        res.status(httpCode).json(responseData)
    }
}
