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

var commonStaticServer = process.env.STATIC_SERVER || 'http://localhost:3008';
if (!commonStaticServer.endsWith('/')) {
    commonStaticServer += '/'
}
var packageRoute = require('./package.route.js');

var commonOptions={},
	webpackdevServer='',
	plugins=[];
var outFilename="[name].js";
var NODE_ENV=process.env.NODE_ENV;


/**
 * 动态查找所有入口文件
 */

var files = glob.sync(path.join(packageRoute.staticPath, '*/js/*.jsx'));

var Accountfiles = glob.sync(path.join(packageRoute.staticPath, '*/js/account/*.jsx'));
var Investmentfiles = glob.sync(path.join(packageRoute.staticPath, '*/js/investment/*.jsx'));
var wechatfiles = glob.sync(path.join(packageRoute.staticPath, '*/js/wechat/*.jsx'));
files=files.concat(Accountfiles).concat(wechatfiles).concat(Investmentfiles);

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
				warnings: false,
				drop_console: true,
				drop_debugger: true
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
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
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

plugins.push(new webpack.DllReferencePlugin({
	context: __dirname,
	manifest: require(packageRoute.publicPathJS+'/dllplugins/jquery-manifest.json')
}));

function createHappyPlugin(id, loaders) {
	return new HappyPack({
		id: id,
		loaders: loaders,
		threadPool: happyThreadPool,
		cache: true,
		verbose: true
	});
}

//happypack利用缓存使rebuild更快
plugins.push(createHappyPlugin('jsx', ['babel?cacheDirectory=true']));

plugins.push(createHappyPlugin('sass', ['css!postcss!sass']));

// image-webpack-loader,图片压缩，目前项目不用
// if(NODE_ENV=='production') {
// 	var loaderObj = [
// 		'url?limit=2048&name=images/[name].[hash:8].[ext]',
// 		'image-webpack-loader?{gifsicle: {interlaced: true}, optipng: {optimizationLevel: 8}, pngquant:{quality: "85", speed: 9}}']
// }
plugins.push(createHappyPlugin('image', ['url-loader?limit=2048&name=images/[name].[hash:8].[ext]']));

var myObject = objectAssign(commonOptions, {
	output: {
		filename:outFilename,
		path:packageRoute.outputPath,
		publicPath:commonStaticServer,
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
			loader: ExtractTextPlugin.extract('style','happypack/loader?id=sass')
		},
		{
			test: /\.(jpe?g|png|gif|svg)$/i,
			loaders:['url?limit=2048&name=images/[name].[hash:8].[ext]']
			// loaders:['happypack/loader?id=image']

		}]
	},
	resolve: {
		extensions: ['', '.js', '.jsx'],
		alias: {
			publicJs:packageRoute.publicPathJS,
			publicStyle:packageRoute.publicStyle,

			askJs:packageRoute.askJs,
			askStyle:packageRoute.askStyle,

			webJs:packageRoute.webJs,
			webJsModule:packageRoute.webJsModule,
			webStyle:packageRoute.webStyle,
			webImages:packageRoute.webImages,

			activityJs:packageRoute.activityJs,
			activityJsModule:packageRoute.activityJsModule,
			activityStyle:packageRoute.activityStyle,

			pointJs:packageRoute.pointJs,
            pointJsModule:packageRoute.pointJsModule,
			pointStyle:packageRoute.pointStyle,
            pointImages:packageRoute.pointImages,

			mobileJs:packageRoute.mobileJs,
			mobileJsModule:packageRoute.mobileJsModule,
			mobileStyle:packageRoute.mobileStyle,
			mobileImages:packageRoute.mobileImages,

            mWebJs:packageRoute.mWebJs,
            mWebJsModule:packageRoute.mWebJsModule,
            mWebStyle:packageRoute.mWebStyle,
            mWebImages:packageRoute.mWebImages

		}
	},

	cache: true,
	plugins: plugins,
	devServer:webpackdevServer
});



module.exports = myObject;

