var webpack = require('webpack');
var sprite = require('sprite-css');
var packageRoute = require('./package.route.js');
sprite({
    src: [packageRoute.webPath+'images/homepage' ],
    spritePath: './src/spritecss/test.png',
    stylesheetPath: './src/spritecss/test.css'
}, function () {
    console.log('Sprite generated!');
});





console.log(packageRoute);