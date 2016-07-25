var path = require('path');
var os = require('os');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');

var basePath = path.join(__dirname, 'src/main/webapp/');

var port = 8080;
var getIP = function() {
    var ipList = os.networkInterfaces();
    var result = 'localhost';
    for (var key in ipList) {
        if (/^e/.test(key)) {
            for (var i = ipList[key].length - 1; i >= 0; i--) {
                if (/192\.168/.test(ipList[key][i].address)) {
                    result = ipList[key][i].address;
                    break;
                }
            }
        }
    }
    return result;
};

var IP = getIP();
console.log('IP:', IP);
IP='localhost';

module.exports = objectAssign(commonOptions, {
    entry: [
        'webpack/hot/dev-server',
        'webpack-dev-server/client?http://'+IP+':'+port,
        path.join(basePath, 'js/mainSite.js'),
    ],
    cache: true,
    devtool: 'eval-source-map',
    devServer: {
        contentBase:basePath,
        port:port,
        historyApiFallback: true,
        hot: true,
        inline:true,
        progress:true,
        publicPath: '/js/'
        //线下调试的虚拟目录，本地用
    }
});
