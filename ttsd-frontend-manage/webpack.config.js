var path = require('path');
var glob=require('glob');
var webpack = require('webpack');
var WebpackMd5Hash = require('webpack-md5-hash');
var objectAssign = require('object-assign');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var basePath = path.join(__dirname, 'resources'),
	staticPath = path.join(basePath, 'static'),
	publicPath=path.join(staticPath, 'public'),
	askPath=path.join(staticPath, 'ask'),
	webPath=path.join(staticPath, 'web'),
	pointPath=path.join(staticPath, 'point'),
	activityPath=path.join(staticPath, 'activity'),
	mobilePath=path.join(staticPath, 'mobile');

var outputPath=path.join(basePath, 'develop'),
	devServerPath='/',
	commonOptions={},
	plugins=[];


/**
 * 动态查找所有入口文件
 */

var files = glob.sync(path.join(staticPath, '*/js/*.jsx'));
var newEntries = {};

files.forEach(function(file){
	// console.log(file);
	var substr = file.match(/resources\/static(\S*)\.jsx/)[1];
	newEntries[substr] = file;

});

//添加要打包在vendors里面的库
newEntries['vendor']=["jquery", "underscore"];

commonOptions.entry = newEntries;

plugins.push(new webpack.ProvidePlugin({
	$: "jquery",
	jQuery: "jquery",
	"window.jQuery": "jquery"
}));
plugins.push(new ExtractTextPlugin("[name].css"));

//把入口文件里面的数组打包成verdors.js
plugins.push(new webpack.optimize.CommonsChunkPlugin({
	name: "vendor",//和上面配置的入口对应
	filename: "public/js/vendorFun.js"//导出的文件的名称
}));

//开发模式
plugins.push(new webpack.HotModuleReplacementPlugin());
plugins.push(new WebpackMd5Hash());

module.exports = objectAssign(commonOptions, {
	output: {
		filename:"[name].js",
		path:outputPath,
		publicPath:devServerPath
	},
	module: {
		loaders: [{
			test: /\.(js|jsx)$/,
			loaders: ['babel'],
			exclude: /(node_modules)/
		},{
			test: /\.css$/,
			loader: ExtractTextPlugin.extract("style-loader", "css-loader")
		},{
			test: /\.scss$/,
			loader: ExtractTextPlugin.extract("style-loader", "css-loader!sass-loader")
		},{
			test: /\.(png|jpg|gif|woff|woff2)$/,
			loader: 'url-loader?limit=5120&name=ask/images/[name].[ext]'

		}]
	},
	resolve: {
		extensions: ['', '.js', '.jsx'],
		alias: {
			publicJs:path.join(publicPath, 'js'),
			publicStyle:path.join(publicPath, 'styles'),

			askJs:path.join(askPath, 'js'),
			askStyle:path.join(askPath, 'styles'),

			webJs:path.join(webPath, 'js'),
			webStyle:path.join(webPath, 'styles'),

			activityJs:path.join(activityPath, 'js'),
			activityStyle:path.join(activityPath, 'styles'),

			pointJs:path.join(pointPath, 'js'),
			pointStyle:path.join(pointPath, 'styles'),

			mobileJs:path.join(mobilePath, 'js'),
			mobileStyle:path.join(mobilePath, 'styles')

		}
	},
	postcss: function() {
		return [
			require('autoprefixer')({
				browsers: ['last 2 versions']
			}),
			px2rem({remUnit: 75})
		];
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

