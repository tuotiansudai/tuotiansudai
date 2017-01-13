var path = require('path');
var glob=require('glob');
var AssetsPlugin = require('assets-webpack-plugin');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var CopyWebpackPlugin = require('copy-webpack-plugin'); //复制文件

var basePath = path.join(__dirname, 'resources'),
	staticPath = path.join(basePath, 'static'),
	publicPath=path.join(staticPath, 'public'),
	askPath=path.join(staticPath, 'ask'),
	webPath=path.join(staticPath, 'web'),
	pointPath=path.join(staticPath, 'point'),
	activityPath=path.join(staticPath, 'activity'),
	mobilePath=path.join(staticPath, 'mobile');

var outputPath=path.join(basePath, 'develop'),//打包文件路径
	devServerPath='/',
	commonOptions={},
	webpackdevServer='',
	plugins=[];
const NODE_ENV=process.env.NODE_ENV;

var publicPathJS=path.join(publicPath, 'js'),
	publicLibs=path.join(publicPathJS, 'libs');

/**
 * 动态查找所有入口文件
 */

var files = glob.sync(path.join(staticPath, '*/js/*.jsx'));
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

commonOptions.entry = newEntries;

plugins.push(new webpack.ProvidePlugin({
	$: "jquery",
	jQuery: "jquery",
	"window.jQuery": "jquery"
}));

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
plugins.push(new CopyWebpackPlugin([
	{ from: staticPath+'/inlineImages',to: 'images'}
]));

if(NODE_ENV=='production') {
	//生产环境
	var outFilename="[name].[chunkhash].js";
	outputPath=path.join(basePath, 'prod'); //打包文件路径
	//生成带hash的css
	plugins.push(new ExtractTextPlugin("[name].[chunkhash].css"));

	//打包之前先删除打包文件里的文件方便重新打包
	plugins.push(new CleanWebpackPlugin(['prod'], {
		root: basePath,
		verbose: true,
		dry: false,
		watch:true
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
	var outFilename="[name].js";
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
			mobileStyle:path.join(mobilePath, 'styles'),

			'layer':publicLibs+'/layer/layer.js',
			'autoNumeric':publicLibs+'/autoNumeric.js',
			'jquery.validate':publicLibs+'/jquery.validate-1.14.0.min.js'

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

