var path = require('path');
var webTemplatePath='../ttsd-web/src/main/webapp/templates/'; //web站点ftl目录
var activityTemplatePath='../ttsd-activity-web/src/main/webapp/templates/'; //activity站点ftl目录
var askTemplatePath='../ttsd-ask-web/src/main/webapp/ask/templates/'; //ask站点ftl目录
var pointTemplatePath='../ttsd-point-web/src/main/webapp/templates/'; //point站点ftl目录

var wapTemplatePath='../ttsd-wap-site/src/main/webapp/templates/';

var basePath = path.join(__dirname, 'resources'),
    outputPath=path.join(basePath, 'develop'); //默认打包路径

var toolsUrl = path.join(basePath, 'tools');

module.exports = {
    public: outputPath, //静态文件目录
    port: '3010',
    hot: true,
    //配置freemarker的解析
    ftl: {
        base: wapTemplatePath,
        global: { }

    },
    mock:[toolsUrl+'/mock.js'],

    // proxy: [{
    //     path: '/register',       //表示需要反向代理的请求path
    //     target: 'http://localhost:8080'  //表示代理的目标地址
    // }]

}