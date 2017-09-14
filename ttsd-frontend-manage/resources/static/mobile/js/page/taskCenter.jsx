import  'mobileStyle/taskCenter.scss';
import React from 'react';
import imagesLoaded from 'imagesloaded';
import classNames from 'classnames';
import {mobileCommon} from 'mobileJsModule/mobileCommon';
import task_banner from 'mobileImages/task-banner.png';

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
        let dataset=event.target.dataset;
        window.location.href = dataset.url;
    } 
    render() { 
        let isComplete=this.props.stocked;
        let button=null;
        let value=this.props.value;
        let url=this.props.location;
        let description=this.props.description;

        if(!isComplete) {
            button=<a className="btn-action" onTouchTap={this.jumpToWhere.bind(this)} data-value={value} data-url={url} >去完成</a>;
        }
        else  {
           button=<a href="javascript:void(0)" className="btn-action disabled">已完成</a>;
        }
        return button;
    }
    };

class NewbieTaskGroup extends React.Component {
    static defaultProps = {
        data: []
    }
    render() {
        let newbieTasks=this.props.data;
        let rows=[];
        let jumto=this.props.jumpToEvent;
        let keyNum;
        if(newbieTasks) {
            newbieTasks.forEach(function(option,key) {
            keyNum=key+1;
            rows.push(<li className={option.completed ? 'completed' : 'working'}  key={key}>
                        <i className="serial">0{keyNum}</i>
                         <span className="detail">
                            <b>{option.title}</b>
                             <em className="reward">奖励 <s>{option.point}</s> 积分</em>
                             <i className="info" dangerouslySetInnerHTML={{__html: option.description}}></i>
                        </span>
                    <ButtonStatus stocked={option.completed} description={option.description} value={option.number} location={option.url} />
                </li>);
            });
            return (
                <div className="task-box">
                    <div className="title-newer"></div>
                    <ul className="task-list">
                        {rows}
                    </ul>
                </div>
            );
        }
        else {
            return (<div></div>);
        }
    }
}
class AdvanceTaskGroup extends React.Component {
    render() {
        let AdvanceData=this.props.data;
        let rows=[];
        if(AdvanceData) {
            AdvanceData.forEach(function(option,key) {
            rows.push(<li className="advance-task" key={key}>
                        <span className="detail">
                            <b>{option.title}</b>
                             <em className="reward">奖励 <s>{option.point}</s> 积分</em>
                             <i className="info" dangerouslySetInnerHTML={{__html: option.description}}></i>
                        </span>
                        <ButtonStatus stocked={option.completed} description={option.description} value={option.number} location={option.url} />
                 </li>);
        });

        return (
            <div className="task-box">
                <div className="title-finished"></div>
                <ul className="task-list">
                    {rows}
                </ul>
            </div>
            );
        }
        else {
            return (<div></div>);
        }  
    };
}

class taskCenter extends React.Component {
        state = {
            active: MenuData.tabHeader[0].value,
            isShowLoading: true,
            listData: {
                newbieTasks: [],
                advancedTasks: []
            }
        };

    fixTopMenu(tabHeader,imageTopHead) {
    
        let winScrollTop = document.documentElement.scrollTop || document.body.scrollTop;
        tabHeader.className = (winScrollTop>=imageTopHead)? 'MenuBox fix-menu' : 'MenuBox';
        if(/fix-menu/.test(tabHeader.className)) {
            tabHeader.style.top = winScrollTop + 'px';
        } else {

            tabHeader.removeAttribute('style')
        }


    }
   
    fetchData(url , callback = function() {}) {
        mobileCommon.ajax({
            url: url,
            type: 'get',
            done: callback
        });
    }

    showListData(value) {
        let taskUrl;
        this.setState({
            active: value,
            isShowLoading:true
        });
        if(value == 'ONGOING') {
            taskUrl = 'task-center/tasks';
        } else if(value == 'FINISHED') {
            taskUrl = 'task-center/completed-tasks';
        }
        mobileCommon.ajax({
            url: taskUrl,
            type: 'get',
            done: function(response) {
                this.setState((previousState) => {
                    return {
                        isShowLoading:false,
                        listData: {
                            newbieTasks: response.data.newbieTasks,
                            advancedTasks: response.data.advancedTasks
                        }
                    };
                });
            }.bind(this)
        });
    }
    //切换任务列别状态
    tabHeaderClickHandler(event) {
        if(/active/.test(event.target.className) ) {
            return;
        }
       this.showListData(event.target.dataset.value);
    }

	componentDidMount() {
        mobileCommon.changeTitle('任务中心');
        this.showListData('ONGOING');
	}
    componentDidUpdate() {
        //数据加载完成后
        let _this = this;

        if (!this.state.isShowLoading) {
            imagesLoaded(this.refs.mainConWrap).on('done', () => {
                let imageTopHead = this.refs.imageTopHead.offsetHeight;

                //菜单离屏幕的高度
                let tabHeader = this.refs.tabHeader;
                window.onscroll=function() {
                    mobileCommon.throttle( _this.fixTopMenu(tabHeader,imageTopHead));

                }

            });

        }
    }

	render() { 
        let loading = null;
  		return (
			<div className='task-center-frame' ref="mainConWrap">
                <div className="bodyCon">
                <div className="clearfix">
                    <div className="image-top-head" id="imageTopHead" ref="imageTopHead">
                        <img src={task_banner}/>
                    </div>
                    <div className={classNames({'MenuBox': true})} ref="tabHeader" id="tabHeaderDom">
                        <ul >
                            {MenuData.tabHeader.map((value, index) => {
                                return <li className={classNames({
                                    'MenuBoxItemNormal': true,
                                    active: this.state.active === value.value
                                })} key={index} data-value={value.value}
                                           onTouchTap={this.tabHeaderClickHandler.bind(this)}>{value.label}</li>;
                            })}
                        </ul>
                    </div>
    			<div className="content-box" ref="scrollWrap">
    			     <div id="OngoingBox" className="OngoingBox clearfix" >
                    {loading}
    			    <NewbieTaskGroup data={this.state.listData.newbieTasks} jumpToEvent={this.jumpTo} />
    			    <AdvanceTaskGroup data={this.state.listData.advancedTasks} jumpToEvent={this.jumpTo} />
                
    			     </div>
                  </div>
    			</div>
                </div>
			</div>	    
		);
        if (this.state.isShowLoading) {
            loading = <div className="loading"><i className="fa fa-spinner fa-spin"></i></div>;
        }
	}
}

// module.exports = taskCenter;
export default taskCenter;



