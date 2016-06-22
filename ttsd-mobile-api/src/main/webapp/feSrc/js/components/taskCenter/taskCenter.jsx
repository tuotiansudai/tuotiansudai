import React from 'react';
import { main } from './taskCenter.scss'; 
import changeTitle from 'utils/changeTitle';
import ajax from 'utils/ajax';
import IScroll from 'iscroll';
import classNames from 'classnames';
import Immutable from 'seamless-immutable';
import taskBanner from './task_banner.png';
import taskLineLeft from './task_line_left.png';
import taskLineRight from './task_line_right.png';
import taskBean from './task_bean.png';

const pageSize = 10;
const MenuData = {
    tabHeader: [{
        label: '进行中',
        value: 'ONGOING'
    }, {
        label: '已完成',
        value: 'FINISHED'
    }]
};


class ButtonStatus extends React.Component {
    
    jumpToWhere(event) {
        let value=event.target.value;
        this.props.jumpToPage(value);
    } 
    render() { 
        let stocked=this.props.stocked;
        let button=null;
        let value=this.props.value;
        if(stocked) {
            button=<span className="TaskItemBtn" value={value} onTouchTap={this.jumpToWhere.bind(this)}>去完成</span>;
        }
        else  {
           button=<span className="TaskItemCompleteBtn" disabled>已完成</span>;
        }
        return button;
    }  
    };

class NewbieTaskGroup extends React.Component {
    render() {
        let NewData=this.props.data;
        let rows=[];
        let jumto=this.props.jumpToEvent;
        NewData.forEach(function(option,key) { 
            rows.push(<div className="TaskItemNewbie" key={key}>
                        <div className="SerialNum">{option.number}</div>
                            <div className="TaskContent">
                            <div className="TaskItemTitle">{option.category}</div>
                            <div className="TaskItemDes">{option.description}</div>
                            <div className="TaskItemLine"></div>
                            <div className="TaskRewardGroup">
                                <div className="TaskReward">{option.reward}</div>
                                <img className="TaskBeanImg" src={taskBean} />
                            </div>
                        </div>
                        <ButtonStatus stocked={option.stocked} value={option.number} jumpToPage={jumto} />
                   </div>);
        });

        return (
            <div className="NewbieTaskGroup">
            <div className="HeaderGroup">
                <img src={taskLineLeft} />
                <span className="HeaderTitle">新手任务</span>
                <img src={taskLineRight}/>
            </div>
            <div className="scroll-wrap" ref="scrollWrap">
                {rows}
                </div>
            </div>
            );
             
    }
}
class AdvanceTaskGroup extends React.Component {
    render() {
        let AdvanceData=this.props.data;
        let rows=[];
        let jumto=this.props.jumpToEvent;
    
        AdvanceData.forEach(function(option,key) { 

            rows.push(<div className="TaskItemNewbie" key={key}>
                    <div className="TaskAdvanceContent">
                        <div className="TaskAdvanceRewardGroup">
                            <div className="TaskAdvanceItemTitle">{option.category}</div>
                            <div className="TaskAdvanceReward">{option.reward}</div>
                            <img className="TaskAdvanceBeanImg" src={taskBean}/>
                        </div>
                        <div className="TaskAdvanceItemDes">{option.description}</div>
                    </div>
                    <ButtonStatus stocked={option.stocked} value={option.number} jumpToPage={jumto} />
                </div>);
        });

        return (
            <div className="AdvanceTaskGroup">
            <div className="HeaderGroup">
                <img src={taskLineLeft} />
                <span className="HeaderTitle">进阶任务</span>
                <img src={taskLineRight} />
            </div>

            <div className="scroll-wrap" ref="scrollOngoing">
               {rows}
            </div>
            
            </div>
            );
    };
}


class taskCenter extends React.Component {

        state = {
            active: MenuData.tabHeader[0].value,
            isShowLoading: false,
            listData: {
                newData: [{
                        number: '01',
                        category: '实名认证',
                        description: '完成实名认证，开通个人账户',
                        reward: '奖励200',
                        stocked: true
                    }, {
                        number: '02',
                        category: '绑定银行卡',
                        description: '绑定常用银行卡，赚钱快人一步',
                        reward: '奖励200',
                        stocked: true
                    }, {
                        number: '03',
                        category: '首次充值',
                        description: '在拓天平台首次成功充值',
                        reward: '奖励200',
                        stocked: true
                    }, {
                        number: '04',
                        category: '首次投资',
                        description: '在拓天平台首次成功投资',
                        reward: '奖励200',
                        stocked: true
                    }, {
                        number: '05',
                        category: '首次投资',
                        description: '在拓天平台首次成功投资',
                        reward: '奖励200',
                        stocked: false
                    }, {
                        number: '06',
                        category: '首次投资',
                        description: '在拓天平台首次成功投资',
                        reward: '奖励200',
                        stocked: false
                    }

                ],
                ongoingData: [{
                    number: '01',
                        category: '累计投资满5000元',
                        description: '完成实名认证，开通个人账户',
                        reward: '奖励1000',
                        stocked: true
                    }, {
                        number: '02',
                        category: '累计投资满5000元',
                        description: '完成实名认证，开通个人账户',
                        reward: '奖励1000',
                        stocked: true
                    }, {
                        number: '03',
                        category: '累计投资满5000元',
                        description: '完成实名认证，开通个人账户',
                        reward: '奖励1000',
                        stocked: true
                    }, {
                        number: '04',
                        category: '累计投资满5000元',
                        description: '完成实名认证，开通个人账户',
                        reward: '奖励1000',
                        stocked: false
                    }, {
                        number: '05',
                        category: '累计投资满5000元',
                        description: '完成实名认证，开通个人账户',
                        reward: '奖励1000',
                        stocked: false
                    }

                ]
            }
        };

    listIndex = 1;

    tabHeaderClickHandler(event) {
        let value = event.target.dataset.value;
        this.setState({
          active: value
        });

        this.listIndex = 1;

        // this.fetchData(1, value, (response) => {
        //     this.setState((previousState) => {
        //         return {
        //             listData: Immutable(response.data.articleList),
        //             isShowLoading: response.data.articleList.length < pageSize ? false : true
        //         };
        //     });
        // });   

    }

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
        ajax({
            url: '/media-center/article-list',
            data: sendData,
            done: callback
        });
    }

	 jumpTo(n) {
            switch (n) {
                case 0://实名认证
                case 1://绑定银行卡
                    window.location.href = "tuotiantask://accountinfo";
                    break;
                case 2://首次充值
                    window.location.href = "tuotiantask://recharge";
                    break;
                case 3://首次投资
                case 4://累计投满5000元
                case 5://单笔投资满1万元
                case 9://首次投资180天标的
                    window.location.href = "tuotiantask://invest";
                    break;
                case 6://每邀请一名好友注册
                case 7://首次邀请好友投资
                case 8://每邀请好友投资1000元
                    window.location.href = "tuotiantask://invitefriend";
                    break;
                case 10://首次开通免密支付
                    window.location.href = "tuotiantask://nopwdpay";
                    break;
                case 11://首次开通自动投标
                    window.location.href = "tuotiantask://autoinvest";
                    break;

                default :
                    break;
            }
        }
	componentDidMount() {
		changeTitle('任务中心');
		let isComplete = 0;
        let listData = [];

	}
    componentDidUpdate() {
        setTimeout(() => {
                if (!this.myScroll) {
                    let marginTop = parseInt(window.getComputedStyle(this.refs.tabBody)['margin-top']);
                    this.refs.scrollWrap.style.height = (document.documentElement.clientHeight - this.refs.banner.offsetHeight - this.refs.tabHeader.offsetHeight - marginTop) + 'px';
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
            }, 200);
    }

	componentWillUnmount() {
		this.destroyIscroll.call(this);
	}
	render() { 
        // let btn = null;
        // if (this.state.aaa) {
        //     btn = <span className={classNames("TaskItemBtn":true, show: option.stocked)} disabled data-value={option.number} onTouchTap={this.jumpTo.bind(this)} >去完成</span>;
        // } else {
        //     btn = <span className={classNames("TaskItemBtn":true, show: option.stocked)} disabled data-value={option.number} onTouchTap={this.jumpTo.bind(this)} >yi完成</span>;
        // }
  		return (
			<div className={main} >
			    <div className="MenuBox">
			        <ul>
                        {MenuData.tabHeader.map((value, index) => {
                            return <li className={classNames({ 'MenuBoxItemNormal': true, active: this.state.active === value.value })} key={index} data-value={value.value} onTouchTap={this.tabHeaderClickHandler.bind(this)}>{value.label}</li>;
                        })}
			        </ul>
			    </div>
		
			<div className="ContentBox">
			<div id="OngoingBox" className="OngoingBox">

			<NewbieTaskGroup data={this.state.listData.newData} jumpToEvent={this.jumpTo} />

			<AdvanceTaskGroup data={this.state.listData.ongoingData} jumpToEvent={this.jumpTo} />
			</div>
			</div>
			</div>	    
		);
	}
}
export default taskCenter;



