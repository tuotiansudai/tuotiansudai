import 'mobileStyle/MediaList.scss';
import React from 'react';
import { hashHistory } from 'react-router';
import IScroll from 'iscroll/build/iscroll-probe';
import imagesLoaded from 'imagesloaded';
import classNames from 'classnames';
import Immutable from 'seamless-immutable';
import shallowCompare from 'react-addons-shallow-compare';
import {mobileCommon} from 'mobileJsModule/mobileCommon';
import Praise from 'mobileJsModule/Praise';
import Carousel from 'mobileJsModule/Carousel';


const pageSize = 10;
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
		let sendData = {
			"index": index,//当前页面
			"pageSize": pageSize,
			"section": section
		};
		if (section === 'ALL') {
			delete sendData.section;
		}
		this.setState({
			isShowLoading:true
		});

		mobileCommon.ajax({
			url: '/media-center/article-list',
			data: sendData,
			done: function(data) {
				this.setState({
					isShowLoading:false
				});
				callback && callback(data);
			}.bind(this)
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
					isShowLoading: false
				};
			});
		});
	}
	findDelegateEle(ele) {
		if (ele.dataset.delegate || ele.nodeName.toLowerCase() === 'body') {
			return ele;
		} else {
			return this.findDelegateEle.call(this, ele.parentNode);
		}
	}
	listItemTapHandler(event) {
		let target = this.findDelegateEle.call(this, event.target);
		hashHistory.push(`media-center/article/${target.dataset.id}`);
	}
	pagination() {
		this.listIndex++;
		this.fetchData(this.listIndex, this.state.active, (response) => {
			this.setState((previousState) => {
				return {
					listData: previousState.listData.concat(response.data.articleList),
					isShowLoading: false
				}
			});
		})
	}
	componentDidMount() {
		mobileCommon.changeTitle('媒体中心');
		let isComplete = 0;
		let listData = [];
		let bannerData = [];

		let fn = (isComplete) => {
			if (isComplete === 2) {
				this.setState((previousState) => {
					return {
						listData: Immutable(listData),
						bannerData: Immutable(bannerData),
						isShowLoading: false
					};
				});
			}
		};
		this.fetchData(1, 'ALL', (response) => {
			listData = response.data.articleList;
			fn(++isComplete);
		});
		mobileCommon.ajax({
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
		//数据加载完成后
		//数据加载完成后
		if (!this.state.isShowLoading) {
			imagesLoaded(this.refs.mainConWrap).on('done', () => {

				let scrollHeight = (document.documentElement.clientHeight -this.refs.banner.offsetHeight - this.refs.tabHeader.offsetHeight) + 'px';
				let myIScrollSec = new IScroll(this.refs.mainConWrap, {
					probeType: 3,
					mouseWheel: true,
					hScrollbar: false,
					vScrollbar: true,
					momentum: false,
					useTransition: false,
					bounce: false,
					useTransform: true

				});
				if (!this.myScroll) {
					this.refs.mainConWrap.style.height = scrollHeight;
					this.myScroll = myIScrollSec;

					this.myScroll.on('scrollEnd', () => {
						if (this.myScroll.y <= this.myScroll.maxScrollY) {

							this.pagination.call(this);

							this.myScroll.refresh();

						}
					});


				}  else {
					this.myScroll.refresh();
					if (this.listIndex === 1) {
						this.myScroll.scrollTo(0, 0);
					}
				}
			});
		}
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
			loading = <li style={{textAlign: 'center', fontSize: 50}}><i className='spinner fa fa-spinner' aria-hidden="true"></i></li>;
		}
		return (
			<section className='media-center-frame'>
				<div className="banner" ref="banner">
					<Carousel data={this.state.bannerData} />
				</div>
				<ul className="tab-header clearfix" ref="tabHeader">
					{data.tabHeader.map((value, index) => {
						return <li className={classNames({ 'pull-left': true, active: this.state.active === value.value })} key={index} data-value={value.value} onTouchTap={this.tabHeaderClickHandler.bind(this)}>{value.label}</li>;
					})}
				</ul>
				<div className="tab-body">
					<div className="scroll-wrap" ref="mainConWrap">
						<div className="clearfix">
							<ul className="list">
								{this.state.listData.map((value, index) => {
									return (
										<li key={index} className="clearfix" onTouchTap={this.listItemTapHandler.bind(this)} data-id={value.articleId} data-delegate="true">
											<div className="pull-left">
												<img src={value.thumbPicture} alt={value.title} data-id={value.articleId} />
											</div>
											<h3>{value.title}</h3>
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
				</div>
			</section>
		);
	}
}
export default MediaList;
