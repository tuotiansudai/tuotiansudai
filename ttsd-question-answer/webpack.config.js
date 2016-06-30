var path = require('path');
var os = require('os');
var webpack = require('webpack');
var ROOT_PATH=path.resolve(__dirname);
var basePath = path.join(ROOT_PATH, 'src/main/webapp/');
var JS_PATH=path.resolve(basePath,'js');
var STYLE_PATH=path.resolve(basePath,'style');
var TEMPLATE_PATH=path.resolve(basePath,'templates');

var HtmlwebpackPlugin=require('html-webpack-plugin');

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
module.exports = {
    entry: [
        'webpack/hot/dev-server',
        'webpack-dev-server/client?http://'+IP+':'+port,
        path.join(JS_PATH, 'main.jsx')
    ],
    output: {
        filename: 'bundle.js',
        path: path.join(basePath, 'dist'),
        publicPath: '/assets/'
    },
    module:{
        loaders:[
            {
                test: /\.js[x]?$/,
                exclude: /node_modules/,
                loader: 'babel-loader?presets[]=es2015&presets[]=react',
              }, 
            {
                test: /\.css$/,
                loader: 'style-loader!css-loader?modules!postcss-loader'
            }, 
            {
                test: /\.scss/,
                loader: 'style-loader!css-loader?modules!postcss-loader!sass-loader?outputStyle=expanded'
            }, 
            {
                 test: /\.(png|jpg|gif|woff|woff2)$/,
                 loader: 'url-loader?limit=8192'
            }
        ]
        
    },
    plugins: [
        new HtmlwebpackPlugin({
            title:'hello,how are you!'
        }),
        new webpack.HotModuleReplacementPlugin(),
        new webpack.ProvidePlugin({
          $: "jquery",
          jQuery: "jquery",
          "window.jQuery": "jquery"
        })
      ],
    cache: true,
    devtool: 'eval-source-map',
    devServer: {
        contentBase:TEMPLATE_PATH,
        port:port,
        historyApiFallback: true,
        hot: true,
        inline:true,
        progress:true
    }
};
