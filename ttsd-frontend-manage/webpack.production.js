var path = require('path');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var basePath = path.join(__dirname, 'resources'),
    plugins=[],
    publicPath='/build/',
    outputPath=path.join(basePath, 'build');

plugins.push(new webpack.ProvidePlugin({
    $: "jquery",
    jQuery: "jquery",
    "window.jQuery": "jquery"
}));
plugins.push(new ExtractTextPlugin("css/[name].css"));

//把入口文件里面的数组打包成verdors.js
plugins.push(new webpack.optimize.CommonsChunkPlugin({
    name: "vendor",//和上面配置的入口对应
    filename: "vendorFun.js"//导出的文件的名称
}));

//压缩
plugins.push(new webpack.optimize.UglifyJsPlugin({
    compress: {
        warnings: false,
        drop_debugger: true,
        drop_console: true
    }
}));

module.exports = objectAssign(commonOptions, {
    entry: {
        test: path.join(basePath, 'js/test.jsx'),
        //添加要打包在vendors里面的库
        vendor: ["jquery", "underscore"]
    },
    output: {
        filename:"[name].js",
        path:outputPath,
        publicPath:publicPath
    },
    cache: true,
    plugins: plugins
});

