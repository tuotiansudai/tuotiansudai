var path = require('path');
var os = require('os');
var webpack = require('webpack');
var ROOT_PATH=path.resolve(__dirname);
var basePath = path.join(ROOT_PATH, 'src/main/webapp/');
var JS_PATH=path.resolve(basePath,'js');
var STYLE_PATH=path.resolve(basePath,'style');
var TEMPLATE_PATH=path.resolve(basePath,'templates');

var ExtractTextPlugin = require('extract-text-webpack-plugin');
var devFlagPlugin = new webpack.DefinePlugin({
  __DEV__: JSON.stringify(JSON.parse(process.env.DEBUG || 'false'))
});


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
        path.join(JS_PATH, 'mainSite.js'),

    ],
    output: {
        filename: "[name].js",
        //filename: 'bundle.js',
        path: path.join(basePath, 'dist'),
        publicPath: '/js/'
    },
    module:{
        loaders:[
            //{
            //    test: /\.js[x]?$/,
            //    exclude: /node_modules/,
            //    loader: 'babel-loader?presets[]=es2015&presets[]=react',
            //  },
            {
                test: /\.css$/,
                loader: ExtractTextPlugin.extract("style-loader", "css-loader")
            },
            {
                test: /\.less$/,
                loader: ExtractTextPlugin.extract("style-loader", "css-loader!less-loader")
            },
            {
                test: /\.scss$/,
                loader: ExtractTextPlugin.extract("style-loader", "css-loader!sass-loader")
            },

            {
                 test: /\.(png|jpg|gif|woff|woff2)$/,
                 loader: 'url-loader?limit=8192'
            }
        ]
        
    },
    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        new webpack.ProvidePlugin({
          $: "jquery",
          jQuery: "jquery",
          "window.jQuery": "jquery"
        }),
        new ExtractTextPlugin("[name].css")

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
    }
};
