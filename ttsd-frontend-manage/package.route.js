var path = require('path');

var basePath = path.join(__dirname, 'resources'),
    staticPath = path.join(basePath, 'static'),
    publicPath=path.join(staticPath, 'public'),
    askPath=path.join(staticPath, 'ask'),
    webPath=path.join(staticPath, 'web'),
    pointPath=path.join(staticPath, 'point'),
    activityPath=path.join(staticPath, 'activity'),
    mobilePath=path.join(staticPath, 'mobile');

var publicPathJS=path.join(publicPath, 'js');
var outputPath=path.join(basePath, 'develop');  //打包文件路径
var dllplugins=path.join(staticPath, 'public/js/dllplugins');

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
    dllplugins:dllplugins

};

module.exports = allPath;



