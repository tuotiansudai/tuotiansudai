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


const data = {
    tabHeader: [{
        label: '全部',
        value: 'ALL'
    }, {
        label: '法律法规',
        value: 'LAW_RULE'
    }, {
        label: '出借人教育',
        value: 'INVESTOR_EDUCATION'
    }, {
        label: '基础知识',
        value: 'BASIC_KNOWLEDGE'
    }]
};
class KnowledgeList extends React.Component {
	state = {
        active: data.tabHeader[0].value,
		isShowLoading: false,
		listData: Immutable([])
	};
	listIndex = 1;
	fetchData(index = 1, section = 'KNOWLEDGE',subSection='ALL', callback = function() {}) {
		let sendData = {
			"index": index,//当前页面
			"pageSize": pageSize,
			"section": section,
			"subSection":subSection
		};
		if (subSection === 'ALL') {
			delete sendData.subSection;
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
		this.fetchData(this.listIndex,'KNOWLEDGE', this.state.active, (response) => {
			this.setState((previousState) => {
				return {
					listData: previousState.listData.concat(response.data.articleList),
					isShowLoading: response.data.articleList.length < pageSize ? false : true
				}
			});
		})
	}
	componentDidMount() {
		mobileCommon.changeTitle('网贷课堂');
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
		this.fetchData(1, 'KNOWLEDGE',this.state.active, (response) => {
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
    tabHeaderClickHandler(event) {
        let value = event.target.dataset.value;
        this.setState({
            active: value
        });
        this.listIndex = 1;
        this.fetchData(1,'KNOWLEDGE', value, (response) => {
            this.setState((previousState) => {
                return {
                    listData: Immutable(response.data.articleList),
                    isShowLoading: response.data.articleList.length < pageSize ? false : true
                };
            });
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
                <ul className="tab-header clearfix" ref="tabHeader">
                    {data.tabHeader.map((value, index) => {
                        return <li className={classNames({ 'pull-left': true, active: this.state.active === value.value })} key={index} data-value={value.value} onTouchTap={this.tabHeaderClickHandler.bind(this)}>{value.label}</li>;
                    })}
                </ul>

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

											<div className="pull-right">
												<div className="create-time"><span>{value.updatedTime}</span></div>
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
export default KnowledgeList;
