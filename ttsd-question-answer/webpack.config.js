var path = require('path');
var os = require('os');
var webpack = require('webpack');
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

module.exports = {
    entry: [
        'webpack/hot/dev-server',
        'webpack-dev-server/client?http://' + IP + ':' + port,
        path.join(basePath, 'js/main.js')
    ],
    output: {
        filename: 'bundle.js',
        path: path.join(basePath, 'dist'),
        publicPath: '/assets/'
    },
    plugins: [
        new webpack.HotModuleReplacementPlugin()
      ],
    cache: true,
    devServer: {
        contentBase: basePath,
        historyApiFallback: true,
        watchOptions: {
           aggregateTimeout: 300,
           poll: 1000
        },
        hot: true,
        port: port,
        publicPath: '/templates/',
        noInfo: false,
        proxy: {
          '/src/main*': {
            target: 'https://other-server.example.com',
            secure: false,
            bypass: function(req, res, proxyOptions) {
              if (req.headers.accept.indexOf('html') !== -1) {
                console.log('Skipping proxy for browser request.');
                return '/index.html';
            }
          }
        }
    }
};
