var path    = require('path');
var webpack = require('webpack');

var path = require('path');
var basePath = path.join(__dirname, 'resources'),
    staticPath = path.join(basePath, 'static'),
    publicPath=path.join(staticPath, 'public');

module.exports = {
    entry: {
        'jquery': ['jquery','layer','underscore'],
        'react':['react','react-dom','react-router','react-tap-event-plugin','react-addons-shallow-compare','classnames']
    },
    output: {
        path: path.join(publicPath, 'js/dllplugins'),
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
            path: path.join(publicPath, 'js/dllplugins', '[name]-manifest.json'),
            name: '[name]_library',
            context: __dirname
        },{
            path: path.join(publicPath, 'js/dllplugins', '[name]-manifest.json'),
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