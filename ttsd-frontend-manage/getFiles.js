var fs = require('fs');
var path = require('path');
var basePath = path.join(__dirname, 'resources');
//遍历文件夹，获取所有文件夹里面的文件信息

function geFileList(folderPath,fileName)
{
    this.folderPath=folderPath; //文件夹路径
    this.fileName=fileName;
    this.filesList = [];
    //遍历读取文件
    this.readFile=function(path) {
        var filesList=this.filesList;

        var files = fs.readdirSync(path);//需要用到同步读取
        files.forEach(function(file) {
            var states = fs.statSync(path+'/'+file);
            if(states.isDirectory())
            {
                this.readFile(path+'/'+file,filesList);
            }
            else
            {
                //创建一个对象保存信息
                var obj = new Object();
                obj.size = states.size;//文件大小，以字节为单位
                obj.name = file;//文件名
                obj.path = path+'/'+file; //文件绝对路径
                this.filesList.push(obj);
            }
        }.bind(this));
    }

    //写入文件utf-8格式
    this.writeFile=function(data) {
        fs.writeFile(this.fileName,data,'utf-8',function() {
            console.log("文件生成成功");
        });
    }
    this.formatHandler=function() {
        var filesList=this.filesList;
        var strJSON={
            "jsFile":{},
            "cssFile":{}
        };
        for(var i=0;i<filesList.length;i++)
        {
            var item = filesList[i],
                thisName=item.name,
                nameNoSuffix;

            var itemPath = item.path.match(/ttsd-frontend-manage\/resources\/(\S*)/)[1];
            var pathObj=itemPath.split('/');
            pathObj.shift();

            if(/\.js$/.test(thisName)) {
                //判断是否为js文件
                nameNoSuffix=thisName.split('.')[0];
                strJSON["jsFile"][nameNoSuffix]=pathObj.join('/');
                // strJSON["jsFile"]["size"]=(item.size/1024).toFixed(2) +"/kb";
            }
            else if(/\.css$/.test(thisName)) {
                //判断是否为css文件
                nameNoSuffix=thisName.split('.')[0];
                strJSON["cssFile"][nameNoSuffix]=pathObj.join('/');
                // strJSON["cssFile"]["size"]=(item.size/1024).toFixed(2) +"/kb";
            }
        }

        var strJsonObj=JSON.stringify(strJSON);
        this.writeFile(strJsonObj);
    }
    this.init=function() {
        var that=this;
        //判断打包的时候文件路径是否存在
        fs.exists(this.folderPath, function (exists) {
            if(exists) {
              that.readFile(that.folderPath);
              that.readFile(basePath+'/develop/public');
              that.formatHandler();
            }
        });
    }
}

module.exports=geFileList;






