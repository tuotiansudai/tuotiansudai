var path = require('path');
var webpack = require('webpack');
var objectAssign = require('object-assign');
var commonOptions = require('./webpack.common');

var basePath = path.join(__dirname, 'src/main/webapp/feSrc');

module.exports = objectAssign(commonOptions, {
	entry: [path.join(basePath, 'js/index.jsx')],
	output: {
		filename: "index.[hash:8].min.js",
		path: path.join(basePath, '../api/js'),
		publicPath: '/api/js/'
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
		new webpack.NoErrorsPlugin()
	],
	cache: false
});