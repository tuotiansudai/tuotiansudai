var path = require('path');
var px2rem = require('postcss-px2rem');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var basePath = path.join(__dirname, 'resources'),
    staticPath = path.join(basePath, 'static'),
    publicPath=path.join(staticPath, 'public'),
    askPath=path.join(staticPath, 'ask'),
    webPath=path.join(staticPath, 'web'),
    pointPath=path.join(staticPath, 'point'),
    activityPath=path.join(staticPath, 'activity'),
    mobilePath=path.join(staticPath, 'mobile');

module.exports = {
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
        extensions: ['', '.js', '.jsx'],
        alias: {
            publicJs:path.join(publicPath, 'js'),
            publicJStyle:path.join(publicPath, 'styles')
            // components: path.join(basePath, 'js/components'),
            // js: path.join(basePath, 'js'),
            // styles: path.join(basePath, 'styles'),
            // images: path.join(basePath, 'images')
        }
    },
    postcss: function() {
        return [
            require('autoprefixer')({
                browsers: ['last 2 versions']
            }),
            px2rem({remUnit: 75})
        ];
    }
};