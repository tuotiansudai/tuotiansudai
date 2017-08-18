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

var commonStaticServer = require('./getStaticServer.js');
var packageRoute = require('./package.route.js');

var devServerPath=commonStaticServer+'/',
	commonOptions={},
	webpackdevServer='',
	plugins=[];
var outFilename="[name].js";
var NODE_ENV=process.env.NODE_ENV;


/**
 * 动态查找所有入口文件
 */

var files = glob.sync(path.join(packageRoute.staticPath, '*/js/*.jsx'));

var Accountfiles = glob.sync(path.join(packageRoute.staticPath, '*/js/account/*.jsx'));
var wechatfiles = glob.sync(path.join(packageRoute.staticPath, '*/js/wechat/*.jsx'));
files=files.concat(Accountfiles).concat(wechatfiles);
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

commonOptions.entry = newEntries;

if(NODE_ENV=='production') {
	//生产环境
	commonOptions.postcss = [
		require('autoprefixer')({
			browsers: ['last 10 Chrome versions', 'last 5 Firefox versions', 'Safari >= 6', 'ie > 8']
		})
	];
	var ParallelUglifyPlugin = require('webpack-parallel-uglify-plugin'); //压缩js，提高压缩速度
	outFilename="[name].[chunkhash].js";
	packageRoute.outputPath=path.join(packageRoute.basePath, 'prod'); //打包文件路径
	//生成带hash的css
	plugins.push(new ExtractTextPlugin("[name].[chunkhash].css"));

	//打包之前先删除打包文件里的文件方便重新打包
	plugins.push(new CleanWebpackPlugin(['prod'], {
		root: packageRoute.basePath,
		verbose: true,
		dry: false,
		watch:true,
		exclude: ['plugins']
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
		cacheDir: packageRoute.outputPath+'/.cache/',
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
	
	//打包之前先删除打包文件里的图片文件方便重新打包
	// plugins.push(new CleanWebpackPlugin(['develop'], {
	// 	root: packageRoute.basePath,
	// 	verbose: true,
	// 	dry: false,
	// 	watch:true,
	// 	exclude: ['public','json-ask.json','json-web.json','json-point.json']
	// }));

	//开发环境
	plugins.push(new webpack.HotModuleReplacementPlugin());

	// 接口代理,目前用ftl-server模拟假数据
	// var proxyList = ['media-center/*','task-center/*'];
	// var proxyObj = {};
	// proxyList.forEach(function(value) {
	// 	proxyObj[value] = {
	// 		target: 'http://localhost:3009',
	// 		changeOrigin: true,
	// 		secure: false
	// 	};
	// });

	webpackdevServer={
		contentBase: packageRoute.basePath,
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
		},
		// proxy:proxyObj
	};
}

plugins.push(new CopyWebpackPlugin([
	{ from: packageRoute.publicPathJS+'/dllplugins',to: 'public/dllplugins'},
	{ from: packageRoute.staticPath+'/inlineImages',to: 'images'},
	{ from: packageRoute.publicPath+'/styles/plugins/skin',to: 'public/skin'}
]));
//生成json文件的列表索引插件
plugins.push(new AssetsPlugin({
	filename: 'assets-resources.json',
	fullPath: false,
	includeManifest: 'manifest',
	prettyPrint: true,
	update: true,
	path: packageRoute.outputPath,
	metadata: {version: 123}
}));

//happypack利用缓存使rebuild更快
plugins.push(createHappyPlugin('jsx', ['babel?cacheDirectory=true']));

plugins.push(createHappyPlugin('sass', ['css!sass']));
// plugins.push(createHappyPlugin('sass', ['css-loader?modules!postcss-loader!sass-loader?outputStyle=expanded']));
plugins.push(new webpack.DllReferencePlugin({
	context: __dirname,
	manifest: require(packageRoute.publicPathJS+'/dllplugins/jquery-manifest.json')
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
		path:packageRoute.outputPath,
		publicPath:devServerPath,
		chunkFilename:'chucks/[name].[chunkhash].js'
	},
	module: {
		loaders: [{
			test: /\.(js|jsx)$/,
			loaders: ['happypack/loader?id=jsx'],
			exclude: /node_modules/
		},
		{
			test: /\.(css|scss)$/,
			loader: ExtractTextPlugin.extract('style',(NODE_ENV=='dev')?'happypack/loader?id=sass':'css!postcss!sass')
		},
		{
			test: /\.(png|jpg|gif|woff|woff2)$/,
			loader: 'url-loader?limit=3072&name=images/[name].[hash:8].[ext]'
		}]
	},
	resolve: {
		extensions: ['', '.js', '.jsx'],
		alias: {
			publicJs:packageRoute.publicPathJS,
			publicStyle:path.join(packageRoute.publicPath, 'styles'),

			askJs:path.join(packageRoute.askPath, 'js'),
			askStyle:path.join(packageRoute.askPath, 'styles'),

			webJs:path.join(packageRoute.webPath, 'js'),
			webJsModule:path.join(packageRoute.webPath, 'js/module'),
			webStyle:path.join(packageRoute.webPath, 'styles'),
			webImages:path.join(packageRoute.webPath, 'images'),

			activityJs:path.join(packageRoute.activityPath, 'js'),
			activityJsModule:path.join(packageRoute.activityPath, 'js/module'),
			activityStyle:path.join(packageRoute.activityPath, 'styles'),

			pointJs:path.join(packageRoute.pointPath, 'js'),
            pointJsModule:path.join(packageRoute.pointPath, 'js/module'),
			pointStyle:path.join(packageRoute.pointPath, 'styles'),
            pointImages:path.join(packageRoute.pointPath, 'images'),

			mobileJs:path.join(packageRoute.mobilePath, 'js'),
			mobileJsModule:path.join(packageRoute.mobilePath, 'js/components'),
			mobileStyle:path.join(packageRoute.mobilePath, 'styles'),
			mobileImages:path.join(packageRoute.mobilePath, 'images')
		}
	},

	cache: true,
	plugins: plugins,
	devServer:webpackdevServer
});



module.exports = myObject;

