var path = require('path');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');

var basePath = path.join(__dirname, 'src/main/webapp/');

module.exports = objectAssign(commonOptions, {
	entry: {
		app: path.resolve(basePath, 'js/mainSite.js'),
		//添加要打包在vendors里面的库
		vendors: ['jquery']
	},
	output: {
		filename: "main.[hash:8].min.js",
		path: path.join(basePath, 'online'),
		publicPath: '/assets/'
	},
	plugins: [
		new webpack.DefinePlugin({
	      'process.env': {
	        'NODE_ENV': '"production"'
	      }
	    }),
		new webpack.optimize.UglifyJsPlugin(),
		new webpack.optimize.OccurenceOrderPlugin(),
		new webpack.optimize.AggressiveMergingPlugin(),
		new webpack.NoErrorsPlugin(),
		//把入口文件里面的数组打包成verdors.js
		new webpack.optimize.CommonsChunkPlugin('vendors', 'vendors.js'),
	],
	cache: false
});