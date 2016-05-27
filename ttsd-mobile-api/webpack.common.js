var path = require('path');
var px2rem = require('postcss-px2rem');

var basePath = path.join(__dirname, 'src/main/webapp/static');

module.exports = {
	output: {
		filename: 'index.js',
		path: path.join(basePath, 'dist'),
		publicPath: '/assets/'
	},
	module: {
		loaders: [{
			test: /\.(js|jsx)$/,
			loaders: ['babel'],
			exclude: /(node_modules|bower_components)/
		}, {
            test: /\.css$/,
            loader: 'style-loader!css-loader?modules!postcss-loader'
        }, {
            test: /\.scss/,
            loader: 'style-loader!css-loader?modules!postcss-loader!sass-loader?outputStyle=expanded'
        }, {
            test: /\.(png|jpg|gif|woff|woff2)$/,
            loader: 'url-loader?limit=8192'
        }, {
            test: /\.(mp4|ogg|svg)$/,
            loader: 'file-loader'
        }]
	},
	resolve: {
		extensions: ['', '.js', '.jsx'],
		alias: {
			components: path.join(basePath, 'js/components'),
			utils: path.join(basePath, 'js/utils'),
			css: path.join(basePath, 'css'),
			image: path.join(basePath, 'image')
		}
	},
	postcss: function() {
        return [
            require('autoprefixer')({
                browsers: ['last 2 versions']
            }),
            px2rem({remUnit: 75})
        ];
    }
};