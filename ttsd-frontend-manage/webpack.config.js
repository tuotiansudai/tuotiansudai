var path = require('path');
var glob=require('glob');
var AssetsPlugin = require('assets-webpack-plugin');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var CopyWebpackPlugin = require('copy-webpack-plugin'); //复制文件
var CleanWebpackPlugin = require('clean-webpack-plugin');  //清空文件夹里的文件

var basePath = path.join(__dirname, 'resources'),
	staticPath = path.join(basePath, 'static'),
	publicPath=path.join(staticPath, 'public'),
	askPath=path.join(staticPath, 'ask'),
	webPath=path.join(staticPath, 'web'),
	pointPath=path.join(staticPath, 'point'),
	activityPath=path.join(staticPath, 'activity'),
	mobilePath=path.join(staticPath, 'mobile');

var publicPathJS=path.join(publicPath, 'js');

var outputPath=path.join(basePath, 'develop'),//打包文件路径
	devServerPath='/',
	commonOptions={},
	webpackdevServer='',
	plugins=[];
var outFilename="[name].js";
var NODE_ENV=process.env.NODE_ENV;
/**
 * 动态查找所有入口文件
 */

var files = glob.sync(path.join(staticPath, '*/js/*.jsx'));
var pluginFiles = glob.sync(path.join(staticPath, '*/plugins/*.js'));
var newEntries = {};

files.forEach(function(file){
	var substr = file.match(/resources\/static(\S*)\.jsx/)[1];
	var strObj=substr.split('/');
	if(strObj[1]=='public') {
		if(/global_page/.test(strObj)) {
			var publicUrl=substr.replace(/\/js/g,'');
			newEntries[publicUrl] = file;
		}
	}
	else {
		newEntries[substr] = file;
	}
});

pluginFiles.forEach(function(file) {
	var substr = file.match(/resources\/static(\S*)\.dll\.js/)[1];
	console.log(substr);
	newEntries[substr] = file;

});

commonOptions.entry = newEntries;

plugins.push(new webpack.DefinePlugin({
	'process.env': {
		'NODE_ENV': '""'
	}
}));

if(NODE_ENV=='production') {
	//生产环境
	outFilename="[name].[chunkhash].js";
	outputPath=path.join(basePath, 'prod'); //打包文件路径
	//生成带hash的css
	plugins.push(new ExtractTextPlugin("[name].[chunkhash].css"));

	//打包之前先删除打包文件里的文件方便重新打包
	plugins.push(new CleanWebpackPlugin(['prod'], {
		root: basePath,
		verbose: true,
		dry: false,
		watch:true,
		exclude: ['plugins']
	}));

	//压缩
	plugins.push(new webpack.optimize.UglifyJsPlugin({
		compress: {
			warnings: false,
			drop_debugger: true,
			drop_console: true
		}
	}));

}
else if(NODE_ENV=='dev') {
	plugins.push(new ExtractTextPlugin("[name].css"));
	//开发环境
	plugins.push(new webpack.HotModuleReplacementPlugin());
	webpackdevServer={
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
	};

}

//生成json文件的列表索引插件
plugins.push(new AssetsPlugin({
	filename: 'assets-resources.json',
	fullPath: false,
	includeManifest: 'manifest',
	prettyPrint: true,
	update: true,
	path: outputPath,
	metadata: {version: 123}
}));

plugins.push(new webpack.DllReferencePlugin({
	context: __dirname,
	manifest: require(publicPath+'/plugins/jquery-manifest.json')
}));

module.exports = objectAssign(commonOptions, {
	output: {
		filename:outFilename,
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
			loader: 'url-loader?limit=5120&name=images/[name].[hash:8].[ext]'
		}]
	},
	resolve: {
		extensions: ['', '.js', '.jsx'],
		alias: {
			publicJs:publicPathJS,
			publicStyle:path.join(publicPath, 'styles'),
			publicLib:path.join(publicPathJS, 'libs'),

			askJs:path.join(askPath, 'js'),
			askStyle:path.join(askPath, 'styles'),

			webJs:path.join(webPath, 'js'),
			webModule:path.join(webPath, 'js/module'),
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
	devServer:webpackdevServer
});

