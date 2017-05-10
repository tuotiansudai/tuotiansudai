var fs = require('fs');
var path = require('path');
 //默认打包路径
var PLATFORM=process.env.PLAT;
var platCase= PLATFORM && PLATFORM.toUpperCase(),
    ttsdConfigFile;

var configPath='../ttsd-config/src/main/resources/ttsd-env.properties';
if(platCase) {
    if(/QA/.test(platCase) || /FT/.test(platCase) || /CI/.test(platCase)) {
        configPath='../ttsd-config/src/main/resources/envs/'+platCase+'.properties';
        ttsdConfigFile=path.resolve(__dirname, configPath);
    } else if(/PROD/.test(platCase)) {
        ttsdConfigFile='/workspace/deploy-config/ttsd-config/ttsd-env.properties'
    }
}
else {
    ttsdConfigFile=path.resolve(__dirname, configPath);
}

var configFile = fs.readFileSync(ttsdConfigFile, 'utf8');

var commonStaticServer=configFile.split('common.static.server=')[1].split('\n')[0];

module.exports=commonStaticServer;


