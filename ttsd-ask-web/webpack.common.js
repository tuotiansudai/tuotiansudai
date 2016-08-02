var path = require('path');
var webpack = require('webpack');
var basePath = path.join(__dirname, 'src/main/webapp/ask');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    output: {
        filename: "[name].js",
        path: path.join(basePath, 'dist'),
        publicPath: '/js/'
        //publicPath是静态资源图片的访问路径
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
