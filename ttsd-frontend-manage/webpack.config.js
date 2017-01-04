var path = require('path');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var basePath = path.join(__dirname, 'resources/static'),
	publicResourcesPath = path.join(__dirname, 'resources/public'),
	plugins=[],
	publicPath='http://localhost:1000/distdev/',
	outputPath=path.join(basePath, 'distdev');

plugins.push(new webpack.ProvidePlugin({
	$: "jquery",
	jQuery: "jquery",
	"window.jQuery": "jquery"
}));
plugins.push(new ExtractTextPlugin("css/[name].css"));

//把入口文件里面的数组打包成verdors.js
plugins.push(new webpack.optimize.CommonsChunkPlugin({
	name: "vendor",//和上面配置的入口对应
	filename: "public/js/vendorFun.js"//导出的文件的名称
}));

//开发模式
plugins.push(new webpack.HotModuleReplacementPlugin());
var devServer={
	contentBase: basePath,
	historyApiFallback: true,
	hot: true,
	devtool: 'eval',
	host: '0.0.0.0',
	port: 1000,
	inline: true,
	noInfo: false
	// proxy: {
	// 	'*': {
	// 		target: 'http://localhost:8088',
	// 		secure: false
	// 	}
	// }
};

module.exports = objectAssign(commonOptions, {
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
	cache: true,
	plugins: plugins,
	devServer:devServer
});

