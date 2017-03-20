var fs = require('fs');
var path = require('path');
var basePath = path.join(__dirname, '../../resources'),
    staticPath = path.join(basePath, 'static'),
    askImagePath=path.join(staticPath, 'ask/images'),
    webImagePath=path.join(staticPath, 'web/images'),
    activityImagePath=path.join(staticPath, 'activity/images'),
    pointImagePath=path.join(staticPath, 'point/images'),
    mobileImagePath=path.join(staticPath, 'mobile/images'),
    publicImagePath=path.join(staticPath, 'public/images'); //默认打包路径
var staticServer ='http://localhost:3008/';
var packfolderPath = path.join(basePath,'develop/images')

function getImagesFiles(devfolderPath,packfolderPath)
{
    this.devfolderPath=devfolderPath; //开发图片文件夹路径
    this.packfolderPath=packfolderPath; // 打包图片文件夹路径
    this.devImagesList = [];  //开发文件里的 图片
    this.packImagesList = [];  //打包文件里的图片
    this.toolSrc = basePath+'/tools/';
}
getImagesFiles.prototype.readImgFile =function(path) {
    //遍历读取图片文件
    var files = fs.readdirSync(path);//需要用到同步读取

    files.forEach(function(file) {
        var states = fs.statSync(path+'/'+file);
        if(states.isDirectory()) {
            this.readImgFile(path+'/'+file,this.devImagesList);
        }
        else {
            //创建一个对象保存信息
            var obj = new Object();
            obj.size = parseFloat(states.size/1024).toFixed(2)+'K';//文件大小，以字节为单位
            obj.name = file;//文件名
            obj.path = path+'/'+file; //文件绝对路径
            this.devImagesList.push(obj);
        }
    }.bind(this));
}

//项目中已经用到的图片
getImagesFiles.prototype.readPackImages = function(path) {

    //遍历读取图片文件
    var packImagesList=this.packImagesList;
    var packImages = fs.readdirSync(path);//需要用到同步读取

    packImages.forEach(function(file) {
        var states = fs.statSync(path+'/'+file);
        if(states.isDirectory()) {
            this.readPackImages(path+'/'+file,packImagesList);
        }
        else {
            //创建一个对象保存信息
            var objImg = new Object();
            objImg.name = file;//文件名
            objImg.path = path+'/'+file; //文件绝对路径
            this.packImagesList.push(objImg);
        }
    }.bind(this));
}

//遍历删除多余的图片,inlineImages,layer,fancybox插件里用到的图片不做处理
getImagesFiles.prototype.removeUselessImgs = function() {
    var devImagesList = this.devImagesList;
    var packImagesList = this.packImagesList;
    console.log(packImagesList)
    devImagesList.forEach(function (imgFile) {
        console.log(imgFile);
    })
}

getImagesFiles.prototype.init=function() {
    var that=this;
    //判断打包的时候文件路径是否存在
    fs.exists(this.devfolderPath, function (exists) {
        if(exists) {
            that.readImgFile(that.devfolderPath);
        }
    });
    fs.exists(this.packfolderPath, function (exists) {
        if(exists) {
            that.readPackImages(that.packfolderPath);
        }
    });
}

//读取ask里的图片
var askListImgs=new getImagesFiles(askImagePath);
askListImgs.init();

