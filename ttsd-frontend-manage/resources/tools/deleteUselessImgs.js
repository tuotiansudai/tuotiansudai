var fs = require('fs');
var _ =require('underscore');
var path = require('path');
var basePath = path.join(__dirname, '../../resources'),
    staticPath = path.join(basePath, 'static'),
    askImagePath=path.join(staticPath, 'ask/images'),
    webImagePath=path.join(staticPath, 'web/images'),
    activityImagePath=path.join(staticPath, 'activity/images'),
    pointImagePath=path.join(staticPath, 'point/images'),
    mobileImagePath=path.join(staticPath, 'mobile/images'),
    publicImagePath=path.join(staticPath, 'public/images'); //默认打包路径

var packfolderPath = path.join(basePath,'develop/images'); //打包图片文件夹路径

/**
 *删除数组指定下标或指定对象
 */
Array.prototype.remove=function(obj){
    for(var i =0;i <this.length;i++){
        var temp = this[i];
        if(!isNaN(obj)){
            temp=i;
        }
        if(temp == obj){
            for(var j = i;j <this.length;j++){
                this[j]=this[j+1];
            }
            this.length = this.length-1;
        }
    }
}

function getImagesFiles(devfolderPath)
{
    this.devfolderPath=devfolderPath; //开发图片文件夹路径
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
            obj.size = parseFloat(states.size/1024).toFixed(2);//文件大小，以字节为单位
            obj.name = file;//文件名
            obj.path = path+'/'+file; //文件绝对路径
            this.devImagesList.push(obj);
        }
    }.bind(this));

    // console.log(this.devImagesList);
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
            objImg.size = parseFloat(states.size/1024).toFixed(2);//文件大小，以字节为单位
            objImg.path = path+'/'+file; //文件绝对路径
            this.packImagesList.push(objImg);
        }
    }.bind(this));

    // console.dir(this.packImagesList);
}

//遍历删除多余的图片,inlineImages,layer,fancybox插件里用到的图片不做处理
getImagesFiles.prototype.removeUselessImgs = function() {
    var devImagesList = this.devImagesList;
    var packNameList = _.pluck(this.packImagesList, 'name');
    var newPackName=[];

    packNameList.forEach(function(img) {
        var splitImg = img.split('.');
        var splitLen = splitImg.length;
        if(splitLen>2 && splitImg[splitLen-2].length ==8) {
            splitImg.remove(splitLen-2);
            newPackName.push(splitImg.join('.'));
        }
        else {
            newPackName.push(img);
        }
    });
    // console.log(packNameList.length);
    // console.log(newPackName.length);
    //开发中需要用的图片
    devImagesList.forEach(function (imgFile) {
        var imageName = imgFile.name;
        var imageSize = imgFile.size;
        // 3072是在module的loader中配置的
        if(imageSize>3072/1024) {
            // console.log(imageName);
            var isBool = _.contains(newPackName, imageName);
            if(!isBool) {
                fs.unlink(imgFile.path, function(err) {
                    if (err) throw err;
                    console.log(imageName + '文件删除成功');
                });
            }
        }
    })
}

//清空图片文件以后，如果图片文件夹为空就删除
getImagesFiles.prototype.removeEmptyFolder=function() {
    //只需要操作开发的目录

}

getImagesFiles.prototype.init=function() {
    var that=this;
    //判断打包的时候文件路径是否存在

    this.readImgFile(that.devfolderPath);
    this.readPackImages(packfolderPath);
    this.removeUselessImgs();
}

//读取ask里的图片
var askListImgs=new getImagesFiles(askImagePath);
askListImgs.init();

// //读取web里的图片
var webListImg=new getImagesFiles(webImagePath);
webListImg.init();

//读取public里的图片
var publicListImg=new getImagesFiles(publicImagePath);
publicListImg.init();
//
// //读取activity里的图片
// var activityListImg=new getImagesFiles(activityImagePath);
// activityListImg.init();
//
//读取point里的图片
var pointListImg=new getImagesFiles(pointImagePath);
pointListImg.init();

//读取mobile里的图片
var mobileListImg=new getImagesFiles(mobileImagePath);
mobileListImg.init();

