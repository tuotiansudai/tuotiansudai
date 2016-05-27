var path = require('path');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');
var os = require('os');

var port = 8888;
var basePath = path.join(__dirname, 'src/main/webapp/static');
var getIP = function() {
	var ipList = os.networkInterfaces();
	var result = 'localhost';
	for (var key in ipList) {
		if (/^e/.test(key)) {
			for (var i = ipList[key].length - 1; i >= 0; i--) {
				if (/192\.168/.test(ipList[key][i].address)) {
					result = ipList[key][i].address;
					break;
				}
			}
		}
	}
	return result;
};
var IP = getIP();
console.log('IP:', IP);
// proxy logic
var proxyList = ['/media-center*'];
var proxyObj = {};
proxyList.forEach(function(value) {
	proxyObj[value] = {
		target: 'http://' + IP + ':8889',
		secure: false
	};
});

module.exports = objectAssign(commonOptions, {
	entry: [
		'webpack-dev-server/client?http://' + IP + ':' + port,
		'webpack/hot/only-dev-server',
		path.join(basePath, 'js/index.jsx')
	],
	plugins: [
		new webpack.HotModuleReplacementPlugin()
	],
	cache: true,
	devtool: 'eval',
	devServer: {
		contentBase: basePath,
		historyApiFallback: true,
		watchOptions: {
		   aggregateTimeout: 300,
		   poll: 1000
		},
		hot: true,
		host: '0.0.0.0',
		port: port,
		publicPath: '/assets/',
		noInfo: false,
		proxy: proxyObj
	}
});