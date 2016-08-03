var path = require('path');
var os = require('os');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');

var basePath = path.join(__dirname, 'src/main/webapp/ask');

var port = 8087;
var IP = 'localhost';

module.exports = objectAssign(commonOptions, {
    entry: [
        'webpack/hot/dev-server',
        'webpack-dev-server/client?http://' + IP + ':' + port,
        path.join(basePath, '/js/mainSite.js')
    ],
    cache: true,
    devtool: 'eval-source-map',
    devServer: {
        contentBase: basePath,
        port: port,
        historyApiFallback: true,
        hot: true,
        inline: true,
        progress: true,
        publicPath: '/ask/dist/'
    //线下调试的虚拟目录，本地用,需要与服务器环境配置保持一致

    }
});
