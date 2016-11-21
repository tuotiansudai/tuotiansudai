var path = require('path');
var webpack = require('webpack');
var basePath = path.join(__dirname, 'src/main/webapp/ask');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

//var extractCSS = new ExtractTextPlugin('[name].css');
module.exports = {
    output: {
        filename: "main.js",
        path: path.join(basePath, '/dist/'),
        publicPath: '/ask/dist/'
        //上线后的文件目录
    },
    module:{
        loaders:[
            {
                test: /\.(js|jsx)$/,
                loader: 'babel-loader',
                query: {
                    presets: ['es2015']
                }
            },
            {
                test: /\.css$/,
                loader: ExtractTextPlugin.extract("style-loader", "css-loader")
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
    resolve: {
        extensions: ['', '.js', '.jsx'],
        alias: {
            // components: path.join(basePath, 'js/components'),
            utils: path.join(basePath, 'js/utils'),
            css: path.join(basePath, 'style/sass'),
            image: path.join(basePath, 'images')
        }
    },
    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        //provide $, jQuery and window.jQuery to every script
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            "window.jQuery": "jquery"
        }),
        new ExtractTextPlugin('main.css'),
        // new webpack.optimize.UglifyJsPlugin(),
        // new webpack.optimize.OccurenceOrderPlugin(),
        // new webpack.optimize.AggressiveMergingPlugin(),
        // new webpack.NoErrorsPlugin()
    ]
};
