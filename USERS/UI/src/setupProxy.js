const proxy = require ('http-proxy-middleware');

const URL = "http://localhost:3002";

module.exports = function(app) {
    app.use(
        proxy("/login", {
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

    app.use(
        proxy("/puchasetickets", {
            target: URL,
            secure: false,
            changeOrigin:true
        })
    );
};
