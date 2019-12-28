const proxy = require ('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        proxy("/userlogin", {
            target: "http://192.168.0.23:3000",
            secure: false,
            changeOrigin:true
        })
    );

    app.use(
        proxy("/userinfo", {
            target: "http://192.168.0.23:3000",
            changeOrigin:true
        })
    );
};