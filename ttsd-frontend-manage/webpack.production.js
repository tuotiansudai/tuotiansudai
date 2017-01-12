var path = require('path');
var glob=require('glob');
var AssetsPlugin = require('assets-webpack-plugin');
var webpack = require('webpack');
// var WebpackMd5Hash = require('webpack-md5-hash');
var objectAssign = require('object-assign');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var basePath = path.join(__dirname, 'resources'),
    staticPath = path.join(basePath, 'static'),
    publicPath=path.join(staticPath, 'public'),
    askPath=path.join(staticPath, 'ask'),
    webPath=path.join(staticPath, 'web'),
    pointPath=path.join(staticPath, 'point'),
    activityPath=path.join(staticPath, 'activity'),
    mobilePath=path.join(staticPath, 'mobile');

var outputPath=path.join(basePath, 'prod'),
    devServerPath='/',
    commonOptions={},
    plugins=[];

/**
 * 动态查找所有入口文件
 */

var files = glob.sync(path.join(staticPath, '*/js/*.jsx'));
var newEntries = {};

files.forEach(function(file){
    var substr = file.match(/resources\/static(\S*)\.jsx/)[1];
    var strObj=substr.split('/');
    if(strObj[1]=='public') {
        if(/global_page/.test(strObj)) {
            var publicUrl=substr.replace(/\/js/g,'');
            newEntries[publicUrl] = file;
        }
    }
    else {
        newEntries[substr] = file;
    }
});
commonOptions.entry = newEntries;


plugins.push(new ExtractTextPlugin("[name].[chunkhash].css"));
// plugins.push(new WebpackMd5Hash());

//压缩
plugins.push(new webpack.optimize.UglifyJsPlugin({
    compress: {
        warnings: false,
        drop_debugger: true,
        drop_console: true
    }
}));

//生成json文件的列表索引插件
plugins.push(new AssetsPlugin({
    filename: 'assets-resources.json',
    fullPath: false,
    includeManifest: 'manifest',
    prettyPrint: true,
    update: true,
    path: outputPath
}));

module.exports = objectAssign(commonOptions, {
    output: {
        filename:"[name].[chunkhash].js",
        path:outputPath,
        publicPath:devServerPath
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
            loader: 'url-loader?limit=5120&name=images/[name].[hash:8].[ext]'
        }]
    },
    resolve: {
        extensions: ['', '.js', '.jsx'],
        alias: {
            publicJs:path.join(publicPath, 'js'),
            publicStyle:path.join(publicPath, 'styles'),

            askJs:path.join(askPath, 'js'),
            askStyle:path.join(askPath, 'styles'),

            webJs:path.join(webPath, 'js'),
            webStyle:path.join(webPath, 'styles'),

            activityJs:path.join(activityPath, 'js'),
            activityStyle:path.join(activityPath, 'styles'),

            pointJs:path.join(pointPath, 'js'),
            pointStyle:path.join(pointPath, 'styles'),

            mobileJs:path.join(mobilePath, 'js'),
            mobileStyle:path.join(mobilePath, 'styles')

        }
    },
    postcss: function() {
        return [
            require('autoprefixer')({
                browsers: ['last 2 versions']
            }),
            px2rem({remUnit: 75})
        ];
    },
    cache: true,
    plugins: plugins
});



