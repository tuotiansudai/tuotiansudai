import React from 'react';
import { hashHistory } from 'react-router';
import changeTitle from 'utils/changeTitle';
import { main, spinner } from './MediaList.scss';
import IScroll from 'iscroll';
import imagesLoaded from 'imagesLoaded';
import Praise from 'components/licaiCircle/Praise';
import classNames from 'classNames';
import ajax from 'utils/ajax';
import Immutable from 'seamless-immutable';
import shallowCompare from 'react-addons-shallow-compare';
import Carousel from 'components/carousel/Carousel';

const pageSize = 20;
const data = {
	tabHeader: [{
		label: '全部',
		value: 'ALL'
	}, {
		label: '行业资讯',
		value: 'INDUSTRY_NEWS'
	}, {
		label: '平台动态',
		value: 'PLATFORM_NEWS'
	}, {
		label: '活动中心',
		value: 'PLATFORM_ACTIVITY'
	}]
};

class MediaList extends React.Component {
	state = {
		active: data.tabHeader[0].value,
		isShowLoading: false,
		listData: Immutable([]),
		bannerData: Immutable([])
	};
	listIndex = 1;
	destroyIscroll() {
		if (this.myScroll) {
			this.myScroll.destroy();
			this.myScroll = null;
		}
	}
	fetchData(index = 1, section = 'ALL', callback = function() {}) {
		ajax({
			url: '/media-center/article-list',
			data: {
			    "index": index,//当前页面
			    "pageSize": pageSize,
			    "section": section
			},
			done: callback
		});
	}
	tabHeaderClickHandler(event) {
		let value = event.target.dataset.value;
		this.setState({
			active: value
		});
		this.listIndex = 1;
		this.fetchData(1, value, (response) => {
			this.setState((previousState) => {
				return {
					listData: Immutable(response.data.articleList),
					isShowLoading: response.data.articleList.length < pageSize ? false : true
				};
			});
		});
	}
	listItemTapHandler(event) {
		hashHistory.push(event.target.dataset.id);
	}
	pagination() {
		this.listIndex++;
		this.fetchData(this.listIndex, this.state.active, (response) => {
			this.setState((previousState) => {
				return {
					listData: previousState.listData.concat(response.data.articleList),
					isShowLoading: response.data.articleList.length < pageSize ? false : true
				}
			});
		})
	}
	componentDidMount() {
		changeTitle('媒体中心');
		let isComplete = 0;
		let listData = [];
		let bannerData = [];

		let fn = (isComplete) => {
			if (isComplete === 2) {
				this.setState((previousState) => {
					return {
						listData: Immutable(listData),
						bannerData: Immutable(bannerData),
						isShowLoading: listData.length < pageSize ? false : true
					};
				});
			}
		};
		this.fetchData(1, 'ALL', (response) => {
			listData = response.data.articleList;
			fn(++isComplete);
		});
		ajax({
			url: '/media-center/banner',
			done: function(response) {
				bannerData = response.data.articleList.map((value) => {
					return {
						img: value.showPicture,
						href: `media-center/article/${value.articleId}`
					}
				});
				fn(++isComplete);
			}.bind(this)
		});
	}
	componentDidUpdate() {
		imagesLoaded(this.refs.scrollWrap).on('always', () => {
			setTimeout(() => {
				if (!this.myScroll) {
					let marginTop = parseInt(window.getComputedStyle(this.refs.tabBody)['margin-top']) + 50;
					this.refs.scrollWrap.style.height = (window.screen.height * document.documentElement.dataset.dpr - this.refs.banner.offsetHeight - this.refs.tabHeader.offsetHeight - marginTop) + 'px';
					this.myScroll = new IScroll(this.refs.scrollWrap);
					this.myScroll.on('scrollEnd', () => {
						if (this.myScroll.y <= this.myScroll.maxScrollY) {
							if (this.state.isShowLoading) {
								this.pagination.call(this);
							}
						}
					});
				} else {
					this.myScroll.refresh();
					if (this.listIndex === 1) {
						this.myScroll.scrollTo(0, 0);
					}
				}
			}, 0);
		});
	}
	componentWillUnmount() {
		this.destroyIscroll.call(this);
	}
	shouldComponentUpdate(nextProps, nextState) {
   		return shallowCompare(this, nextProps, nextState);
  	}
	render() {
		let loading = null;
		if (this.state.isShowLoading) {
			loading = <li style={{textAlign: 'center', fontSize: 50}}><i className={spinner + ' fa fa-spinner'} aria-hidden="true"></i></li>;
		}
		return (
			<section className={main}>
				<div className="banner" ref="banner">
					<Carousel data={this.state.bannerData} />
				</div>
				<ul className="tab-header clearfix" ref="tabHeader">
					{data.tabHeader.map((value, index) => {
						return <li className={classNames({ 'pull-left': true, active: this.state.active === value.value })} key={index} data-value={value.value} onTouchTap={this.tabHeaderClickHandler.bind(this)}>{value.label}</li>;
					})}
				</ul>
				<div className="tab-body" ref="tabBody">
					<div className="scroll-wrap" ref="scrollWrap">
						<ul className="list">
							{this.state.listData.map((value, index) => {
								return (
									<li key={index} className="clearfix">
										<div className="pull-left">
											<a href="javascript:" onTouchTap={this.listItemTapHandler.bind(this)} data-id={value.articleId}>
												<img src={value.thumbPicture} alt={value.title} />
											</a>
										</div>
										<h3><a href="javascript:" onTouchTap={this.listItemTapHandler.bind(this)} data-id={value.articleId}>{value.title}</a></h3>
										<div className="clearfix bottom-block">
											<time className="pull-left">{value.creatTime}</time>
											<div className="pull-right">
												<div className="readed">阅读：<span>{value.readCount}</span></div>
												<Praise className="praise" likeCount={value.likeCount}></Praise>
											</div>
										</div>
									</li>
								);
							})}
							{loading}
						</ul>
					</div>
				</div>
			</section>
		);
	}
}

export default MediaList;