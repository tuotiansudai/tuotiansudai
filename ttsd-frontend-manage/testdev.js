var path = require('path');
var webpack = require('webpack');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var basePath = path.join(__dirname, 'resources/static'),
    plugins=[],
    publicPath,
    devServer,
    outputPath;

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

    publicPath='http://localhost:3008/distdev/';
    outputPath=path.join(basePath, 'distdev');
    //开发模式
    plugins.push(new webpack.HotModuleReplacementPlugin());
    devServer={
        contentBase: basePath,
        historyApiFallback: true,
        hot: true,
        devtool: 'eval',
        host: '0.0.0.0',
        port: 3008,
        inline: true,
        noInfo: false,
        proxy: {
            '*': {
                target: 'http://localhost:8088',
                secure: false
                // changeOrigin: true
            }
        }
    };

module.exports = {
    entry: {
        test: path.join(basePath, 'ask/test.jsx'),
        //添加要打包在vendors里面的库
        vendor: ["jquery", "underscore"]
    },
    output: {
        filename:"[name].js",
        path:outputPath,
        publicPath:publicPath
    },
    module: {
        loaders: [{
            test: /\.(js|jsx)$/,
            loaders: ['babel'],
            exclude: /(node_modules)/
        },{
            test: /\.css$/,
            loader: ExtractTextPlugin.extract("style-loader", "css-loader")
        },{
            test: /\.scss$/,
            loader: ExtractTextPlugin.extract("style-loader", "css-loader!sass-loader")
        },{
            test: /\.(png|jpg|gif|woff|woff2)$/,
            // loader: 'url-loader?limit=5120&name=images/[hash:8].[name].[ext]'
            loader: 'url-loader?limit=5120&name=images/[name].[ext]'
        }]
    },
    resolve: {
        extensions: ['', '.js', '.jsx']
    },

    cache: true,
    plugins: plugins,
    devServer:devServer
};

