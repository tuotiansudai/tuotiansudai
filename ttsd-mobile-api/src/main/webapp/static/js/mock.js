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

app.get('/media-center/article-detail/:articleId', function(req, res) {
	res.send({
		"code": "",
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
		"code": "",
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
			},{
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
		"code": "",
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