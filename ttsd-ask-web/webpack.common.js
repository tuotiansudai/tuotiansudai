var path = require('path');
var webpack = require('webpack');
var basePath = path.join(__dirname, 'src/main/webapp/ask');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

//var extractCSS = new ExtractTextPlugin('[name].css');
module.exports = {
    output: {
        filename: "[name].js",
        path: path.join(basePath, 'dist'),
        publicPath: '/js/'
        //上线后的文件目录
    },
    module:{
        loaders:[
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
    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        //provide $, jQuery and window.jQuery to every script
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            "window.jQuery": "jquery"
        }),
        new ExtractTextPlugin('main.css')
    ]

};
