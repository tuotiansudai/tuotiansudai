var express = require('express');
var path = require('path');
var fs = require('fs');
var app = express();
app.use(function(req, res, next) {
	res.set({
		'Access-Control-Allow-Origin': '*',
		"Access-Control-Allow-Methods": "GET,POST,PUT,DELETE,OPTIONS",
		"Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept"
	});
	next();
});

//web
app.get('/media-center/banner', function(req, res) {
	res.send({
		"code": "0000",
		"message": "",
		"data": {
			"index": "",
			"pageSize": "",
			"totalCount": "",
			"articleList": [{
				"articleId": "11111",
				"title": "拓天速贷第二期全国排行活动正式启动",
				"author": "张三",
				"thumbPicture": "http://placekitten.com/120/120?text=img",
				"showPicture": "http://placekitten.com/750/350?text=img",
				"section": "ALL",
				"content": "内容",
				"createTime": "2016-05-06 12:32:58",
				"source": "",
				"likeCount": "100",
				"readCount": "100"
			}]
		}
	});
});

app.post('/media-center/:id/like', function(req, res) {
	res.send({
		data: {
			likeCount: 12121
		}
	});
});

app.listen(3009, function() {
	console.log('mock server listening on port 3009!');
});
