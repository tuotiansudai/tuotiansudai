var express = require('express');
var app = express();

// app.use(function(req, res, next) {
// 	res.set({
// 		'Access-Control-Allow-Origin': '*',
// 		"Access-Control-Allow-Methods": "GET,POST,PUT,DELETE,OPTIONS",
// 		"Access-Control-Allow-Headers": "Origin, X-Requested-With, Content-Type, Accept"
// 	});
// 	next();
// });

app.get('/task-center/completed-tasks', function(req, res) {
	res.send({
		code: "0000",
		message: "",
		data: {

			newbieTasks: [{
				name: "EACH_SUM_INVEST",
				title: "累计投资满10000.00元",
				description: "还差<span class=color-key>0.00元</span>即可获得奖励",
				point: 2000,
				completed: true
			}, {
				name: "FIRST_SINGLE_INVEST",
				title: "单笔投资满10000.00元",
				description: '可获得奖励',
				point: 2000,
				completed: true
			},{
				name: "FIRST_INVEST_180",
				title: "首次投资180天标的",
				description: '可获得奖励',
				point: 1000,
				completed: true
			}],
			advancedTasks: [{
				name: "EACH_SUM_INVEST",
				title: "累计投资满10000.00元",
				description: "还差<span class=color-key>0.00元</span>即可获得奖励",
				point: 2000,
				completed: true
			}, {
				name: "FIRST_SINGLE_INVEST",
				title: "单笔投资满10000.00元",
				description: '可获得奖励',
				point: 2000,
				completed: true
			}, {
				name: "EACH_RECOMMEND",
				title: "每邀请1名好友注册",
				description: "已邀请0名好友注册",
				point: 200,
				completed: true
			}, {
				name: "FIRST_REFERRER_INVEST",
				title: "首次邀请好友投资",
				description: '可获得奖励',
				point: 5000,
				completed: true
			}, {
				name: "EACH_REFERRER_INVEST",
				title: "每邀请好友投资满1000元",
				description: "已邀请好友投资0.00元",
				point: 1000,
				completed: true
			}]
		}
	});
});
app.get('/task-center/tasks', function(req, res) {
	res.send({
		code: "0000",
		message: "",
		data: {
			newbieTasks: null,
			advancedTasks: [{
				name: "EACH_SUM_INVEST",
				title: "累计投资满10000.00元",
				description: "还差<span class=color-key>0.00元</span>即可获得奖励",
				point: 2000,
				completed: false
			}, {
				name: "FIRST_SINGLE_INVEST",
				title: "单笔投资满10000.00元",
				description: null,
				point: 2000,
				completed: false
			}, {
				name: "EACH_RECOMMEND",
				title: "每邀请1名好友注册",
				description: "已邀请0名好友注册",
				point: 200,
				completed: false
			},
			{
				name: "EACH_SUM_INVEST",
				title: "累计投资满10000.00元",
				description: "还差<span class=color-key>0.00元</span>即可获得奖励",
				point: 2000,
				completed: false
			}, {
				name: "FIRST_SINGLE_INVEST",
				title: "单笔投资满10000.00元",
				description: null,
				point: 2000,
				completed: false
			}, {
				name: "EACH_RECOMMEND",
				title: "每邀请1名好友注册",
				description: "已邀请0名好友注册",
				point: 200,
				completed: false
			},
			{
				name: "EACH_SUM_INVEST",
				title: "累计投资满10000.00元",
				description: "还差<span class=color-key>0.00元</span>即可获得奖励",
				point: 2000,
				completed: false
			}, {
				name: "FIRST_SINGLE_INVEST",
				title: "单笔投资满10000.00元",
				description: null,
				point: 2000,
				completed: false
			}, {
				name: "EACH_RECOMMEND",
				title: "每邀请1名好友注册",
				description: "已邀请0名好友注册",
				point: 200,
				completed: false
			}]
		}
	});
});

app.get('/media-center/article-detail/:articleId', function(req, res) {
	res.send({
		"code": "0000",
		"message": "",
		"data": {
			"articleId": "11111", //文章ID
			"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
			"author": "张三", //作者
			"thumbPicture": "", //缩略图
			"showPicture": "http://placekitten.com/750/340?text=img", //展示图
			"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
			"content": '<img src="http://placekitten.com/750/340?text=img" alt=""/> <p>事老地方就是浪费 事老地方就是浪费流口水都放假了双方就 老师的分类考试的附件 流口水的减肥了开始就发来撒风景老师的放假了双方 老师的发了啥快递放假</p> <img src="http://placekitten.com/750/340?text=img" alt=""/> <p>事老地方就是浪费 事老地方就是浪费流口水都放假了双方就 老师的分类考试的附件 流口水的减肥了开始就发来撒风景老师的放假了双方 老师的发了啥快递放假</p> <p>事老地方就是浪费 事老地方就是浪费流口水都放假了双方就 老师的分类考试的附件 流口水的减肥了开始就发来撒风景老师的放假了双方 老师的发了啥快递放假</p> <img src="http://placekitten.com/750/340?text=img" alt=""/> <p>事老地方就是浪费 事老地方就是浪费流口水都放假了双方就 老师的分类考试的附件 流口水的减肥了开始就发来撒风景老师的放假了双方 老师的发了啥快递放假</p>', //文章内容
			"createTime": "2016-05-06 12:32:58", //创建时间
			"source": "", //文章来源
			"likeCount": "100", //点赞数
			"readCount": "100", //阅读数
		}
	});
});

app.get('/media-center/article-list', function(req, res) {
	res.send({
		"code": "0000",
		"message": "",
		"data": {
			"index": "", //当前页面
			"pageSize": "", //每页条数
			"totalCount": "", //总条数
			"articleList": [{
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}, {
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}, {
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}, {
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}, {
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}, {
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}, {
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}]
		}
	});
});

app.get('/media-center/banner', function(req, res) {
	res.send({
		"code": "0000",
		"message": "",
		"data": {
			"index": "", //当前页面
			"pageSize": "", //每页条数
			"totalCount": "", //总条数
			"articleList": [{
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "http://placekitten.com/750/350?text=img", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}, {
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "http://placekitten.com/750/350?text=img", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}, {
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "http://placekitten.com/750/350?text=img", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
			}, {
				"articleId": "11111", //文章ID
				"title": "拓天速贷第二期全国排行活动正式启动", //文章标题
				"author": "张三", //作者
				"thumbPicture": "http://placekitten.com/120/120?text=img", //缩略图
				"showPicture": "http://placekitten.com/750/350?text=img", //展示图
				"section": "ALL", //栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
				"content": "内容", //文章内容
				"createTime": "2016-05-06 12:32:58", //创建时间
				"source": "", //文章来源
				"likeCount": "100", //点赞数
				"readCount": "100", //阅读数
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

app.listen(8889, function() {
	console.log('mock server listening on port 8889!');
});