var webpack = require('webpack');
var sprite = require('sprite-css');
var packageRoute = require('./package.route.js');

sprite({
    src: [packageRoute.webImages+'/homepage' ],
    spritePath: packageRoute.webImages+'/homepage/homepage.png',
    stylesheetPath: packageRoute.webStyle+'/spritecss/homepage.css'
}, function () {
    console.log('Sprite generated!');
});

