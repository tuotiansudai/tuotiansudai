import React from 'react';
import IScroll from 'iscroll/build/iscroll-probe';
import imagesLoaded from 'imagesloaded';
import classNames from 'classnames';
import 'mobileStyle/taskCenter.scss';
import {mobileCommon} from 'mobileJsModule/mobileCommon';
import titleOne from 'mobileImages/title-one.png';
import titleTwo from 'mobileImages/title-two.png';
import task_banner from 'mobileImages/task-banner.png';


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
        // debugger
        if(newbieTasks) {
            newbieTasks.forEach(function(option,key) {
            keyNum=key+1;
            rows.push(<li className={option.completed ? 'completed' : 'working'}  key={key}>
                        <i className="serial">0{keyNum}</i>
                         <span className="detail">
                            <b>{option.title}</b>
                             <em className="reward">奖励 {option.point} 积分</em>
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
                let keyNum=key+1;
            rows.push(<li className="advance-task" key={key}>
                        <span className="detail">
                            <b>{option.title}</b>
                             <em className="reward">奖励 {option.point} 积分</em>
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

    destroyIscroll() {
        if (this.myScroll) {
            this.myScroll.destroy();
            this.myScroll = null;
        }
    }
   
    fetchData(url , callback = function() {}) {
        mobileCommon.ajax({
            url: url,
            type: 'get',
            done: callback
        });
    }

    //当下滑固定菜单在顶部
    fixTopMenu(scrollY) {
        let conOffsetTop = this.refs.imageTopHead.offsetHeight;
        let tabHeaderDom = document.getElementById('tabHeaderDom');
        let OngoingBoxTop = document.getElementById('OngoingBox').offsetTop;
        if (!scrollY && tabHeaderDom.getAttribute('style')) {

            let menuScrollTop = OngoingBoxTop - tabHeaderDom.offsetHeight * 0.27;
            let conScrollTop = OngoingBoxTop - tabHeaderDom.offsetHeight;

            this.myScroll.scrollTo(0, -conScrollTop, 0);
            tabHeaderDom.setAttribute('style', 'position:absolute;top:' + menuScrollTop + 'px;width:100%;left:0;height:1rem; line-height:1rem');
        }
        else if (scrollY && scrollY >= conOffsetTop) {
            let yTop = scrollY + tabHeaderDom.offsetHeight * 0.30;
            tabHeaderDom.setAttribute('style', 'position:absolute;top:' + yTop + 'px;width:100%;left:0;height:1rem; line-height:1rem');
        }
        else if (scrollY && scrollY < conOffsetTop) {
            tabHeaderDom.removeAttribute('style');
        }

    }
    tabHeaderClickHandler(event) {
        let value = event.target.dataset.value;
        if(/active/.test(event.target.className) ) {
            return;
        }
        this.setState({
          active: value,
          isShowLoading:true
        });
        this.fixTopMenu();

        if(value=='ONGOING') {
            this.fetchData('/task-center/tasks',(response) => {

            this.setState((previousState) => {
                return {
                    isShowLoading:false,
                    listData: {
                        newbieTasks: response.data.newbieTasks,
                        advancedTasks: response.data.advancedTasks
                    }
                };
            });
         });
        }
        else if(value=='FINISHED') {
           this.fetchData('/task-center/completed-tasks',(response) => {
            this.setState((previousState) => {
                return {
                    isShowLoading:false,
                    listData: {
                        newbieTasks: response.data.newbieTasks,
                        advancedTasks: response.data.advancedTasks
                    }
                };
            });
        }); 
        }
        }  

	componentDidMount() {
        mobileCommon.changeTitle('任务中心');
		let isComplete = 0;
        let listData = [];
        
        this.fetchData('/task-center/tasks',(response) => {
            this.setState((previousState) => {
                return {
                    isShowLoading:false,
                    listData: {
                        newbieTasks: response.data.newbieTasks,
                        advancedTasks: response.data.advancedTasks
                    }
                };
            });
         });

	}
    componentDidUpdate() {
        //数据加载完成后
        if (!this.state.isShowLoading) {
            imagesLoaded(this.refs.mainConWrap).on('done', () => {
                let tabHeaderDom = document.getElementById('tabHeaderDom');
                let menuHeight = tabHeaderDom.clientHeight * 0.5;
                if (!this.myScroll) {
                    this.refs.mainConWrap.style.height = document.documentElement.clientHeight + 'px';
                    this.myScroll = new IScroll(this.refs.mainConWrap, {
                        probeType: 3,
                        mouseWheel: true,
                        hScrollbar: false,
                        vScrollbar: true,
                        momentum: false,
                        useTransition: false,
                        bounce: false,
                        useTransform: true

                    });
                    this.myScroll.on('scroll', function () {
                        let curY = Math.abs(this.myScroll.y) - menuHeight/2;
                        this.fixTopMenu(curY);
                    }.bind(this));

                }
                else {
                    this.myScroll.refresh();
                    if (!this.myScroll.hasVerticalScroll) {
                        //垂直方向没有滚动条
                        tabHeaderDom.removeAttribute('style');
                    }
                }
            });
        }
    }

	componentWillUnmount() {
		this.destroyIscroll.call(this);
	}
	render() { 
        let loading = null;
  		return (
			<div className="task-center-frame" >
                <div className="bodyCon" ref='mainConWrap'>
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

module.exports = taskCenter;
// export default taskCenter;



