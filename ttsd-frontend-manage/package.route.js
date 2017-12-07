var path = require('path');

var basePath = path.join(__dirname, 'resources'),
    staticPath = path.join(basePath, 'static'),
    publicPath=path.join(staticPath, 'public'),
    askPath=path.join(staticPath, 'ask'),
    webPath=path.join(staticPath, 'web'),
    pointPath=path.join(staticPath, 'point'),
    activityPath=path.join(staticPath, 'activity'),
    mobilePath=path.join(staticPath, 'mobile'),
    mWebPath = path.join(staticPath, 'm-web');

var publicPathJS=path.join(publicPath, 'js');
var outputPath=path.join(basePath, 'develop');  //打包文件路径
var dllplugins=path.join(staticPath, 'public/js/dllplugins');

var publicStyle=path.join(publicPath, 'styles'),

    askJs=path.join(askPath, 'js'),
    askStyle=path.join(askPath, 'styles'),

    webJs=path.join(webPath, 'js'),
    webJsModule=path.join(webPath, 'js/module'),
    webStyle=path.join(webPath, 'styles'),
    webImages=path.join(webPath, 'images'),

    activityJs=path.join(activityPath, 'js'),
    activityJsModule=path.join(activityPath, 'js/module'),
    activityStyle=path.join(activityPath, 'styles'),

    pointJs=path.join(pointPath, 'js'),
    pointJsModule=path.join(pointPath, 'js/module'),
    pointStyle=path.join(pointPath, 'styles'),
    pointImages=path.join(pointPath, 'images'),

    mobileJs=path.join(mobilePath, 'js'),
    mobileJsModule=path.join(mobilePath, 'js/components'),
    mobileStyle=path.join(mobilePath, 'styles'),
    mobileImages=path.join(mobilePath, 'images'),

    mWebJs = path.join(mWebPath, 'js'),
    mWebJsModule=path.join(mWebPath, 'js/module'),
    mWebStyle=path.join(mWebPath, 'styles'),
    mWebImages=path.join(mWebPath, 'images');

var allPath = {
    basePath:basePath,
    staticPath:staticPath,
    publicPath:publicPath,
    askPath:askPath,
    webPath:webPath,
    pointPath:pointPath,
    activityPath:activityPath,
    mobilePath:mobilePath,
    publicPathJS:publicPathJS,
    outputPath:outputPath,
    dllplugins:dllplugins,
    publicStyle:publicStyle,
    askJs:askJs,
    askStyle:askStyle,
    webJs:webJs,
    webJsModule:webJsModule,
    webStyle:webStyle,
    webImages:webImages,
    activityJs:activityJs,
    activityJsModule:activityJsModule,
    activityStyle:activityStyle,
    pointJs:pointJs,
    pointJsModule:pointJsModule,
    pointStyle:pointStyle,
    pointImages:pointImages,
    mobileJs:mobileJs,
    mobileJsModule:mobileJsModule,
    mobileStyle:mobileStyle,
    mobileImages:mobileImages,

    mWebJs:mWebJs,
    mWebJsModule:mWebJsModule,
    mWebStyle:mWebStyle,
    mWebImages:mWebImages
};

module.exports = allPath;



