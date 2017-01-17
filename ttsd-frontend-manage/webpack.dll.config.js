var path    = require('path');
var webpack = require('webpack');

var path = require('path');
var basePath = path.join(__dirname, 'resources'),
    staticPath = path.join(basePath, 'static'),
    publicPath=path.join(staticPath, 'public');

module.exports = {
    entry: {
        'jquery': ['jquery','layer']
    },
    output: {
        path: path.join(publicPath, 'plugins'),
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
            path: path.join(publicPath, 'plugins', '[name]-manifest.json'),
            name: '[name]_library',
            context: __dirname
        }),
        //压缩
        new webpack.optimize.UglifyJsPlugin({
            compress: {
                warnings: false,
                drop_debugger: true,
                drop_console: true
            }
        })
    ]
};



