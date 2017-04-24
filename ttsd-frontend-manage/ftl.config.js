var path = require('path');
var webTemplatePath='../ttsd-web/src/main/webapp/templates/'; //web站点ftl目录
var activityTemplatePath='../ttsd-activity-web/src/main/webapp/templates/'; //activity站点ftl目录
var askTemplatePath='../ttsd-ask-web/src/main/webapp/ask/templates/'; //ask站点ftl目录
var pointTemplatePath='../ttsd-point-web/src/main/webapp/templates/'; //point站点ftl目录

var basePath = path.join(__dirname, 'resources'),
    outputPath=path.join(basePath, 'develop'); //默认打包路径

var toolsUrl = path.join(basePath, 'tools');

module.exports = {
    public: outputPath,
    port: '3010',
    hot: true,
    // watch: [require.resolve('weChat/start'), 'E:\\ftlServer\page.mock'],
    //配置freemarker的解析
    ftl: {
        base: webTemplatePath,
        // dataFiles: ['testdata.ftl'],
        global: { },
        'app-download.ftl': function(req, res) {
            return {
                saleActivityMap: {
                    "000008": {
                        activityStatus: 'actived'
                    }
                }
            }
        }

    },
    mock:[toolsUrl+'/mock.js'],

    // proxy: [{
    //     path: '/proxy',       //表示需要反向代理的请求path
    //     target: 'http://localhost:3000'  //表示代理的目标地址
    // }]

}