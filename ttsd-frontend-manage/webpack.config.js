var path = require('path');
var glob=require('glob');
var AssetsPlugin = require('assets-webpack-plugin');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var CopyWebpackPlugin = require('copy-webpack-plugin'); //复制文件
var CleanWebpackPlugin = require('clean-webpack-plugin');  //清空文件夹里的文件

var node_modules = path.resolve(__dirname, 'node_modules');
 //通过多进程模型，来加速代码构建
var HappyPack = require('happypack');
var happyThreadPool = HappyPack.ThreadPool({ size: 5 });

var staticServer = require('./getStaticServer.js');
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
	devServerPath=staticServer+'/',
	commonOptions={},
	webpackdevServer='',
	plugins=[];
var outFilename="[name].js";
var NODE_ENV=process.env.NODE_ENV;

/**
 * 动态查找所有入口文件
 */

var files = glob.sync(path.join(staticPath, '*/js/*.jsx'));
var Accountfiles = glob.sync(path.join(staticPath, '*/js/account/*.jsx'));
files=files.concat(Accountfiles);
var newEntries = {};

files.forEach(function(file){
	var substr = file.match(/resources\/static(\S*)\.jsx/)[1];
	var strObj=substr.split('/');
	if(strObj[1]=='public') {
		if(/globalFun_page/.test(strObj)) {
			var publicUrl=substr.replace(/\/js/g,'');
			newEntries[publicUrl] = file;
		}
	}
	else {
		newEntries[substr] = file;
	}
});
// console.log(newEntries);
commonOptions.entry = newEntries;

if(NODE_ENV=='production') {
	//生产环境
	var ParallelUglifyPlugin = require('webpack-parallel-uglify-plugin'); //压缩js，提高压缩速度
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
		exclude: ['plugins','.cache']
	}));

	//压缩
	// plugins.push(new webpack.optimize.UglifyJsPlugin({
	// 	compress: {
	// 		warnings: false,
	// 		drop_debugger: true,
	// 		drop_console: true
	// 	}
	// }));
	plugins.push(new ParallelUglifyPlugin({
		cacheDir: outputPath+'/.cache/',
		uglifyJS:{
			output: {
				comments: false
			},
			compress: {
				warnings: false
			}
		}
	}));

}
else if(NODE_ENV=='dev') {
	plugins.push(new ExtractTextPlugin("[name].css"));

	//打包之前先删除打包文件里的文件方便重新打包
	plugins.push(new CleanWebpackPlugin(['develop'], {
		root: basePath,
		verbose: true,
		dry: false,
		watch:true,
		exclude: ['public','json-ask.json','json-web.json']
	}));

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
		noInfo: false,
		stats: {
			chunks: false,
			colors: true
		}
		// proxy: {
		// 	'*': {
		// 		secure: false,
		// 		changeOrigin: true,
		// 		target: 'http://localhost:8080/'
		// 	}
		// }
	};
}
plugins.push(new CopyWebpackPlugin([
	{ from: publicPathJS+'/dllplugins',to: 'public/dllplugins'},
	// { from: publicPathJS+'/plugins',to: 'public/plugins'},
	{ from: staticPath+'/inlineImages',to: 'images'},
	{ from: publicPath+'/styles/plugins/skin',to: 'public/skin'}
]));
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

//happypack利用缓存使rebuild更快
plugins.push(createHappyPlugin('jsx', ['babel']));
plugins.push(createHappyPlugin('sass', ['css!sass']));

plugins.push(new webpack.DllReferencePlugin({
	context: __dirname,
	manifest: require(publicPathJS+'/dllplugins/jquery-manifest.json')
}));


function createHappyPlugin(id, loaders) {
	return new HappyPack({
		id: id,
		loaders: loaders,
		threadPool: happyThreadPool,

		// disable happy caching with HAPPY_CACHE=0
		cache: process.env.HAPPY_CACHE !== '0',

		// make happy more verbose with HAPPY_VERBOSE=1
		verbose: process.env.HAPPY_VERBOSE === '1',
	});
}

var myObject = objectAssign(commonOptions, {
	output: {
		filename:outFilename,
		path:outputPath,
		publicPath:devServerPath,
		chunkFilename:'chucks/[name].[chunkhash].js'
	},
	module: {
		loaders: [{
			test: /\.(js|jsx)$/,
			loaders: ['happypack/loader?id=jsx'],
			exclude: /node_modules/
		},{
			test: /\.css$/,
			loader: ExtractTextPlugin.extract("style-loader", "css-loader")
		},{
			test: /\.scss$/,
			loader: ExtractTextPlugin.extract("style", "happypack/loader?id=sass")
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
			webJsModule:path.join(webPath, 'js/module'),
			webStyle:path.join(webPath, 'styles'),
			webImages:path.join(webPath, 'images'),

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



module.exports = myObject;

