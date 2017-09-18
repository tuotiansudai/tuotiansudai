var path    = require('path');
var webpack = require('webpack');
var packageRoute = require('./package.route.js');

module.exports = {
    entry: {
        'jquery': ['jquery','layer','underscore','iscroll']
    },
    output: {
        path: path.join(packageRoute.publicPath, 'js/dllplugins'),
        filename: '[name].dll.js',
        library: '[name]_library',
        libraryTarget: 'umd',
        umdNamedDefine: true
    },
    plugins: [
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            "window.jQuery": "jquery"
        }),
        new webpack.DllPlugin({
            path: path.join(packageRoute.publicPath, 'js/dllplugins', '[name]-manifest.json'),
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