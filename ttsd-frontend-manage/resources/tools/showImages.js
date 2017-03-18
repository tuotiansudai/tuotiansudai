var fs = require('fs');
var path = require('path');
var basePath = path.join(__dirname, '../../resources'),
	staticPath = path.join(basePath, 'static'),
	askImagePath=path.join(staticPath, 'ask/images'),
	webImagePath=path.join(staticPath, 'web/images'),
	publicImagePath=path.join(staticPath, 'public/images'); //默认打包路径
var staticServer ='http://localhost:3008/';
//遍历文件夹，获取所有文件夹里面的文件信息
function geFileList(folderPath,fileName)
{
	this.folderPath=folderPath; //文件夹路径
	this.filesList = [];
	this.filename = fileName;
	this.toolSrc = basePath+'/tools/';
	//遍历读取图片文件
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
				obj.size = parseFloat(states.size/1024).toFixed(2)+'K';//文件大小，以字节为单位
				obj.name = file;//文件名
				obj.path = path+'/'+file; //文件绝对路径
				this.filesList.push(obj);
			}
		}.bind(this));
	}

	//读取html内容
	this.ImportHtml =function() {
		var that = this;
		fs.readFile(this.toolSrc+'/'+this.filename,{
			//需要制定编码方式，否则返回原生buffer
			encoding:'utf8'
		},function (err, htmlData) {

			var imageObg =[];

			that.filesList.forEach(function(imgUrl) {
				var itemPath = staticServer+ imgUrl.path.match(/ttsd-frontend-manage\/resources\/(\S*)/)[1];
				console.log(itemPath);
				imageObg.push('<img src="'+itemPath+'" size="'+imgUrl.size+'"> <br/>');
			});

			 var getHtml = imageObg.join('');
			//生成新的ask图片html文件
			fs.writeFile(that.toolSrc+that.filename,getHtml,{
				encoding:'utf8'
			},function(err) {
				if(err) {
					throw err;
				}
				console.log('ask图片文件生成成功');
			});

		})
	}
	//写入文件utf-8格式
	// this.writeFile=function(data) {
	// 	// fs.writeFile(this.fileName,data,'utf-8',function() {
	// 	// 	console.log("文件生成成功");
	// 	// });
	// }
	this.formatHandler=function() {
		var filesList=this.filesList;
		var strJSON={
			"src":{}
		};


		// var strJsonObj=JSON.stringify(strJSON);
		// this.writeFile(strJsonObj);
	}
	this.init=function() {
		var that=this;
		//判断打包的时候文件路径是否存在
		fs.exists(this.folderPath, function (exists) {
			if(exists) {
				that.readFile(that.folderPath);
				that.ImportHtml();
				// that.formatHandler();
			}
		});
	}
}


//读取ask里的图片
var askListImg=new geFileList(askImagePath,'image-ask.html');
askListImg.init();

//读取web里的图片
var webListImg=new geFileList(webImagePath,'image-web.html');
webListImg.init();

//读取public里的图片
var publicListImg=new geFileList(publicImagePath,'image-public.html');
publicListImg.init();





