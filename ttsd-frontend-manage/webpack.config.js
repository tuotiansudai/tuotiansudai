var path = require('path');
var glob=require('glob');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var basePath = path.join(__dirname, 'resources'),
	staticPath = path.join(basePath, 'static'),
	publicPath=path.join(staticPath, 'public'),
	askPath=path.join(staticPath, 'ask'),
	outputPath=path.join(basePath, 'develop'),
	devServerPath='http://localhost:3008/develop/',
	plugins=[];

/**
 * 动态查找所有入口文件
 */

var files = glob.sync(path.join(staticPath, '*/js/*.jsx'));
var newEntries = {};

console.log('files '+ files);
console.log('fileslength '+ files.length);

files.forEach(function(file){
	console.log(file);
	// var name = /.*\/(apps\/.*?\/index)\.js/.exec(f)[1];//得到apps/question/index这样的文件名
	// newEntries[name] = f;
});
//
// commonOptions.entry = Object.assign({}, commonOptions.entry, newEntries);

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

//开发模式
plugins.push(new webpack.HotModuleReplacementPlugin());

module.exports = objectAssign(commonOptions, {
	entry: {
		global_page: path.join(publicPath, 'js/global_page.jsx'),
		'./ask/ask_main': path.resolve(askPath, 'js/mainSite.jsx'),
		//添加要打包在vendors里面的库
		vendor: ["jquery", "underscore"]
	},
	output: {
		filename:"[name].js",
		path:outputPath,
		publicPath:devServerPath
	},
	cache: true,
	plugins: plugins,
	devServer:{
		contentBase: basePath,
		historyApiFallback: true,
		hot: true,
		devtool: 'eval',
		host: '0.0.0.0',
		port: 3008,
		inline: true,
		noInfo: false
		// proxy: {
		// 	'*': {
		// 		target: 'http://localhost:8088',
		// 		secure: false
		// 	}
		// }
	}
});

