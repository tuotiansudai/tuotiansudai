var fs = require('fs');
var path = require('path');
 //默认打包路径

var ttsdConfigFile=path.resolve(__dirname, '../ttsd-config/src/main/resources/ttsd-env.properties');
var configFile = fs.readFileSync(ttsdConfigFile, 'utf8');
var staticServer=configFile.split('common.static.server=')[1].split('\n')[0];

module.exports=staticServer;


