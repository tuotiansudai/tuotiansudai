let ajax = function(json) {
	let opt = {
		"url": "",  //发生的链接地址
		"done": function() {}, //成功后执行的方法
		"fail": function() {}, //失败后执行的方法
		"type": "GET", //发送类型
		"data": null, //发送的数据 json格式，例：{'name':'abc'}
		"async": true, //异步或同步
		"contentType": true,
		"dataType": "json"//如果要跨域请求
	};
	for (let i in json) {
		opt[i] = json[i];
	}

	let isGet = opt.type.toLowerCase() === 'get';
	if (isGet && opt.data) {
		opt.url = opt.url + '?' + JSON.stringify(opt.data);
	}

	//1.创建Ajax对象
	let oAjax = new XMLHttpRequest();
	//2.连接服务器
	oAjax.open(opt.type.toUpperCase(), opt.url, opt.async);
	if (opt.contentType === true) {
		oAjax.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	} else {
		oAjax.setRequestHeader("Content-Type", opt.contentType);
	}
	//3.发送请求
	if (isGet) {
		oAjax.send();
	} else {
		oAjax.send(JSON.stringify(opt.data));
	}
	//4.接收服务器的返回
	oAjax.onreadystatechange = function() {
		if (oAjax.readyState == 4) {
			if (oAjax.status < 300) {
				if (opt.dataType === 'json') {
					opt.done(JSON.parse(oAjax.responseText));
				} else {
					opt.done(oAjax.responseText);
				}
			} else {
				console.error(oAjax);
				if (opt.fail) {
					opt.fail(oAjax);
				}
			}
		}
	}
};

export default ajax;
