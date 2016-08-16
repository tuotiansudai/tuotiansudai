var path = require('path');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var basePath = path.join(__dirname, 'src/main/webapp/ask');

//var extractCSS = new ExtractTextPlugin('main.css');
//var extractLESS = new ExtractTextPlugin('[name].css');
module.exports = {
	entry: {
		app: path.resolve(basePath, 'js/mainSite.js'),
		//添加要打包在vendors里面的库
		vendor: ["jquery", "underscore"]
	},
	output: {
		filename: "main.[hash:8].min.js",
		path: path.join(basePath, '/dist/'),
		publicPath: '/ask/dist/'
	},
	module:{
		loaders:[
			{
				test: /\.css$/,
				loader: ExtractTextPlugin.extract("style-loader", "css-loader")
			},
			{
			    test: /\.scss$/,
			    loader: ExtractTextPlugin.extract("style-loader", "css-loader!sass-loader")
			},
			{
				test: /\.(png|jpg|gif|woff|woff2)$/,
				loader: 'url-loader?limit=8192'
			}
		]

	},
	plugins: [
		new webpack.HotModuleReplacementPlugin(),
		//provide $, jQuery and window.jQuery to every script
		new webpack.ProvidePlugin({
			$: "jquery",
			jQuery: "jquery",
			"window.jQuery": "jquery"
		}),
		new ExtractTextPlugin('main.css'),
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
		new webpack.optimize.CommonsChunkPlugin(/* chunkName= */"vendor", /* filename= */"vendor.bundle.js")
	],
	cache: false

};
