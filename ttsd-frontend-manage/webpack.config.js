var path = require('path');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var basePath = path.join(__dirname, 'resources'),
	staticPath = path.join(basePath, 'static'),
	askPath=path.join(staticPath, 'ask'),
	plugins=[],
	publicPath='http://localhost:3008/develop/',
	outputPath=path.join(basePath, 'develop');

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
		test: path.join(askPath, 'test.jsx'),
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

