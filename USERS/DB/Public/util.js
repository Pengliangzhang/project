module.exports = {
    responseClient(res,httpCode, code,message,data) {
        let responseData = {};
        responseData.code = code;
        responseData.message = message;
        responseData.data = data;
        res.status(httpCode).json(responseData)
    }
}