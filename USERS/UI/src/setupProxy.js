const proxy = require ('http-proxy-middleware');

const URL = "https://9923e08a.ngrok.io";

module.exports = function(app) {
    app.use(
        proxy("/userlogin", {
            target: URL,
            secure: false,
            changeOrigin:true
        })
    );

    app.use(
        proxy("/userinfo", {
            target: URL,
            changeOrigin:true
        })
    );

    app.use(
        proxy("/usersignup", {
            target: URL,
            secure: false,
            changeOrigin:true
        })
    );
};