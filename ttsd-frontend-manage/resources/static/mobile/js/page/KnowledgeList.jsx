import 'mobileStyle/MediaList.scss';
import React from 'react';
import { hashHistory } from 'react-router';

import imagesLoaded from 'imagesloaded';
import classNames from 'classnames';
import Immutable from 'seamless-immutable';
import shallowCompare from 'react-addons-shallow-compare';
import {mobileCommon} from 'mobileJsModule/mobileCommon';
import Carousel from 'mobileJsModule/Carousel';


const pageSize = 10;
import bannerUrl from '../../images/knowledge_banner.png';

class MediaList extends React.Component {
	state = {
		isShowLoading: false,
		listData: Immutable([])
	};
	listIndex = 1;
	fetchData(index = 1, section = 'KNOWLEDGE', callback = function() {}) {
		let sendData = {
			"index": index,//当前页面
			"pageSize": pageSize,
			"section": section
		};
		if (section === 'ALL') {
			delete sendData.section;
		}
		mobileCommon.ajax({
			url: '/media-center/article-list',
			data: sendData,
			done: callback
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
		hashHistory.push({
            pathname: `media-center/article/${target.dataset.id}`,
            query: {
				isShowLikeCount:0
        	}

   		 });

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
		mobileCommon.changeTitle('媒体中心');
		let isComplete = 0;
		let listData = [];
		let bannerData = [];

		let fn = (isComplete) => {
			if (isComplete === 1) {
				this.setState((previousState) => {
					return {
						listData: Immutable(listData),
						isShowLoading: listData.length < pageSize ? false : true
					};
				});
			}
		};
		this.fetchData(1, 'KNOWLEDGE', (response) => {
			listData = response.data.articleList;
			fn(++isComplete);
		});

	}
	componentDidUpdate() {
		imagesLoaded(this.refs.scrollWrap).on('always', () => {

			let _this =this;
			let scrollWrap = this.refs.scrollWrap;
			scrollWrap.style.height = (document.documentElement.clientHeight - this.refs.banner.offsetHeight ) + 'px';
			scrollWrap.onscroll = function() {

				var scrollTop = scrollWrap.scrollTop;
				var offsetHeight = scrollWrap.offsetHeight;
				var scrollHeight = scrollWrap.scrollHeight;

				if(scrollTop+offsetHeight == scrollHeight) {
					if (_this.state.isShowLoading) {
						_this.pagination.call(_this);
					}
				}
			}

		});
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
				<div className="banner knowledge-banner" ref="banner">
					<img src={bannerUrl} alt=""/>
				</div>

				<div className="tab-body" ref="scroll-wrap">
					<div className="scroll-wrap" ref="scrollWrap">
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
												<div className="create-time"><span>{value.createTime}</span></div>
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
