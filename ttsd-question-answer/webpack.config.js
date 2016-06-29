module.exports = {
    entry: {
        app: ['webpack/hot/dev-server', './main.js']
    },
    output: {
        path: __dirname,
        filename: 'bundle.js'
    },
    module: {
        loaders: [
            {
                test: /\.(js|jsx)$/,
                loader: 'babel',
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                loaders: ['style', 'css']
            }
        ]
    }
};