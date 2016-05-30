import React from 'react';
import changeTitle from 'utils/changeTitle';
import { main } from './Article.scss';
import IScroll from 'iscroll';
import imagesLoaded from 'imagesLoaded';
import Praise from 'components/licaiCircle/Praise';
import ajax from 'utils/ajax';

class Article extends React.Component {
	state = {
		"code": "",
		"message": "",
		"data":{
			"articleId": "",//文章ID
			"title": "",//文章标题
			"author": "",//作者
			"thumbPicture": "",//缩略图
			"showPicture": "",//展示图
			"section": "",//栏目 ALL:全部，PLATFORM_ACTIVITY：平台活动，PLATFORM_NEWS：平台新闻，INDUSTRY_NEWS：行业资讯
			"content": "",//文章内容
			"createTime": "",//创建时间
			"source": "",//文章来源
			"likeCount": 0,//点赞数
			"readCount": 0//阅读数
		}
	};
	destroyIscroll() {
		if (this.myScroll) {
			this.myScroll.destroy();
			this.myScroll = null;
		}
	}
	componentDidMount() {
		changeTitle('文章详情');
		ajax({
			url: `/media-center/article-detail/${this.props.params.id}`,
			type: 'get',
			done: function(data) {
				this.setState(data);
			}.bind(this),
			fail: function(xhr) {
				console.error(xhr);
			}.bind(this)
		});
	}
	componentDidUpdate() {
		imagesLoaded(this.refs.scrollWrap).on('always', () => {
			this.destroyIscroll.call(this);
			this.myScroll = new IScroll(this.refs.scrollWrap);
		});
	}
	componentWillUnmount() {
		this.destroyIscroll.call(this);
	}
	render() {
		return (
		    <div ref="scrollWrap" className={main}>
				<article>
					<h1>{this.state.data.title}</h1>
					<section className="meta">
						<span>作者: </span><span>{this.state.data.author}</span>
						<time>{this.state.data.createTime}</time>
					</section>
					<section className="show-picture">
						<img src={this.state.data.showPicture} />
					</section>
					<section className="content" dangerouslySetInnerHTML={{ __html: this.state.data.content }}>
					</section>
					<section className="info clearfix">
						<div className="pull-left readed">阅读：{this.state.data.readCount}</div>
						<Praise className="pull-right" likeCount={this.state.data.likeCount} id={this.state.data.articleId}></Praise>
					</section>
				</article>
			</div>
		);
	}
}

export default Article;