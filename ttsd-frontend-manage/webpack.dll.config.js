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

// var publicPathJS=path.join(publicPath, 'js'),
//     publicLibs=path.join(publicPathJS, 'libs'),
//     libsObj={
//         'publicJsPath':publicPathJS,
//         'text': publicLibs + '/text-2.0.14.js',
//         // 'clipboard': publicLibs+'/clipboard.min.js',
//         // 'md5': publicLibs+'/jQuery.md5.js',
//         'qrcode': publicLibs+'/jquery.qrcode.min.js',
//             'jqueryValidate': publicLibs + '/jquery.validate-1.14.0.min.js',
//             'jqueryForm': publicLibs + '/jquery.form-3.51.0.min.js',
//         'autoNumeric': publicLibs + '/autoNumeric.js',
//             'mustache': publicLibs + '/mustache-2.1.3.min.js',
//         'moment': publicLibs + '/moment-2.10.6.min.js',
//         'daterangepicker': publicLibs + '/jquery.daterangepicker-0.0.7.js',
//         // 'layer': publicLibs + '/layer/',
//         // 'echarts': publicLibs + '/echarts/dist/echarts.min.js',
//         // 'swiper':publicLibs+'/swiper-3.2.7.jquery.min.js',
//         'rotate': publicLibs+'/jqueryrotate.min.js',
//         'template':publicLibs+'/template.min.js',
//         // 'fancybox':publicLibs+'/jquery.fancybox.min.js'
//     };


module.exports = {
    entry: {
        'jquery': ['jquery'],
        'echarts':['echarts'],
        'layer':['layer'],
        'jqueryPlugin':['clipboard','md5','swiper','fancybox']
    },
    output: {
        path: path.join(outputPath, 'public/plugins'),
        filename: '[name].dll.js',
        library: '[name]_library'
    },
    plugins: [
        new webpack.DllPlugin({
            path: path.join(outputPath, 'public/plugins', '[name]-manifest.json'),
            name: '[name]_library'
        })
    ]
};