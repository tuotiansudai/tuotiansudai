var webpack = require('webpack');
var path = require('path');
var fs = require('fs');
var sprite = require('sprite-css');
var glob=require('glob');
var packageRoute = require('./package.route.js');

function generateSpriteCss(option) {

    var thisOption = Object.assign({},option);
    var styleObjUrl = {
        'web':packageRoute.webStyle,
        'ask':packageRoute.askStyle,
        'activity':packageRoute.activityStyle,
        'mobile':packageRoute.mobileStyle,
        'point':packageRoute.pointStyle

    }

    this.formPngImgs=glob.sync(path.join(packageRoute.staticPath, thisOption.siteFrom+'/images/'+thisOption.formPngFolder+'/*.png'));

    this.toSpriteImgPath = styleObjUrl[thisOption.siteFrom]+'/spritecss/'+thisOption.toSpriteImg+'.png' ;
    this.toSpriteCssPath = styleObjUrl[thisOption.siteFrom]+'/spritecss/'+thisOption.toSpriteCss+'.css';
}

generateSpriteCss.prototype.initSprite = function() {

      //文件大小，以字节为单位转为k
    //图片大于2k小于10K才会生成雪碧图
    var filterImages = this.formPngImgs.filter(function(element, index, array) {
        var states = fs.statSync(element);

        var imgSize = parseFloat(states.size/1024).toFixed(2);
        // console.log(index+':'+imgSize);
        return imgSize > 2 && imgSize<10;
    });

    console.log(filterImages.length);
    sprite({
        src:filterImages,
        spritePath:this.toSpriteImgPath,
        stylesheetPath:this.toSpriteCssPath,
        layoutOptions: {
            padding: 10
        },
        compositorOptions:{
            compressionLevel:5
        },
        stylesheetOptions: {
            prefix: 'sprite-',
            pixelRatio: 2
        }
    }, function () {
        console.log('Sprite generated!');
    });
}

//生成首页 sprite 图

var homePageSprite  = new generateSpriteCss({
    siteFrom:'web',
    formPngFolder:'homepage',
    toSpriteImg:'homepage',
    toSpriteCss:'homepage'
});

homePageSprite.initSprite();
