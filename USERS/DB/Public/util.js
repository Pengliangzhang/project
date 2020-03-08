module.exports = {
    responseClient(res,httpCode, code,message,data) {
        let responseData = {};
        responseData.code = code;
        responseData.message = message;
        responseData.data = data;
        res.setHeader("Access-Control-Allow-Origin","*");
        res.status(httpCode).json(responseData)
    }
}
