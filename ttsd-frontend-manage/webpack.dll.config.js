var path    = require('path');
var webpack = require('webpack');

var path = require('path');
var basePath = path.join(__dirname, 'resources'),
    staticPath = path.join(basePath, 'static'),
    publicPath=path.join(staticPath, 'public');

var outputPath=path.join(basePath, 'develop');//打包文件路径

const NODE_ENV=process.env.NODE_ENV;
if(NODE_ENV=='production') {
    outputPath=path.join(basePath, 'prod'); //打包文件路径
}

module.exports = {
    entry: {
        'jquery': ['jquery','layer']
    },
    output: {
        path: path.join(outputPath, 'public/plugins'),
        filename: '[name].dll.js',
        library: '[name]_library'
    },
    plugins: [
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            "window.jQuery": "jquery"
        }),
        new webpack.DllPlugin({
            path: path.join(outputPath, 'public/plugins', '[name]-manifest.json'),
            name: '[name]_library',
            context: __dirname
        })
    ]
};