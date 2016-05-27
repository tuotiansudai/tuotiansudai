let ajax = function(json) {
	let opt = {
		"url": "",  //发生的链接地址
		"done": function() {}, //成功后执行的方法
		"fail": function() {}, //失败后执行的方法
		"type": "GET", //发送类型
		"data": null, //发送的数据 json格式，例：{'name':'abc'}
		"async": true, //异步或同步
		"contentType": "application/json;charset=UTF-8",
		"dataType": "json"
	};
	for (let i in json) {
		opt[i] = json[i];
	}

	let isGet = opt.type.toLowerCase() === 'get';
	if (isGet && opt.data) {
		let str = "";
		for (let i in opt.data) {
			str += (i + "=" + opt.data[i] + "&");
		}
		str = str.substring(0, str.length - 1);
		opt.url = opt.url + '?' + str;
	}

	//1.创建Ajax对象
	let oAjax = new XMLHttpRequest();
	//2.连接服务器
	oAjax.open(opt.type.toUpperCase(), opt.url, opt.async);
	if (opt.contentType) {
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
					let response = JSON.parse(oAjax.responseText);
					if (response.code === '0000') {
						opt.done(response);
					} else {
						console.error(response);
						opt.fail(response);
					}
				} else {
					opt.done(oAjax.responseText);
				}
			} else {
				console.error(oAjax);
				opt.fail(oAjax);
			}
		}
	}
};

export default ajax;
