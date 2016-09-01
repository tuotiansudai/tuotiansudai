import React from 'react';
import { main ,spinner } from './taskCenter.scss'; 
import changeTitle from 'utils/changeTitle';
import ajax from 'utils/ajax';
import IScroll from 'iscroll/build/iscroll-probe';
import imagesLoaded from 'imagesloaded';
import classNames from 'classnames';
import Immutable from 'seamless-immutable';
import taskBanner from './task_banner1.png';
import taskLineLeft from './task_line_left.png';
import taskLineRight from './task_line_right.png';

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
            // onClick={this.jumpToWhere.bind(this)}
            button=<a className={description? 'TaskItemBtn' : 'TaskItemBtn column-one'} url={url} onTouchTap={this.jumpToWhere.bind(this)} data-value={value} data-url={url} >去完成</a>;
        }
        else  {
           button=<button className={description? 'TaskItemCompleteBtn' : 'TaskItemCompleteBtn column-one'}  disabled>已完成</button>;
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
            rows.push(<div className={option.completed ? 'TaskItemNewbie completed-tasks' : 'TaskItemNewbie'} key={key} >
                        <div className="SerialNum" >0{keyNum}</div>
                            <div className="TaskContent">
                            <div className="TaskItemTitle">{option.title}</div>
                            <div className="TaskItemDes" dangerouslySetInnerHTML={{__html: option.description}} ></div>
                            <div className="TaskItemLine"></div>
                            <div className="TaskRewardGroup">
                                <div className="TaskReward">奖励{option.point}</div>
                            </div>
                        </div>
                        <ButtonStatus stocked={option.completed} description={option.description} value={option.number} location={option.url} />
                   </div>);
        });
            return (
            <div className="NewbieTaskGroup">
            <div className="HeaderGroup">
                <img src={taskLineLeft} />
                <span className="HeaderTitle">新手任务</span>
                <img src={taskLineRight}/>
            </div>
            <div className="scroll-wrap clearfix" ref="scrollWrap">
                {rows}
                </div>
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

            rows.push(<div className="TaskItemNewbie" key={key}>
                    <div className="TaskAdvanceContent">
                        <div className="TaskAdvanceRewardGroup">
                            <div className="TaskAdvanceItemTitle">{option.title}</div>
                            <div className="TaskAdvanceReward">奖励{option.point}</div>
                        </div>
                        <div className="TaskAdvanceItemDes" dangerouslySetInnerHTML={{__html: option.description}} data-hyb="xxx" aria-ybs="true"></div>
                    </div>
                    <ButtonStatus stocked={option.completed} description={option.description} value={option.number} location={option.url} />
                </div>);
        });

        return (
            <div className="AdvanceTaskGroup">
            <div className="HeaderGroup">
                <img src={taskLineLeft} />
                <span className="HeaderTitle">进阶任务</span>
                <img src={taskLineRight} />
            </div>

            <div className="scroll-wrap clearfix" ref="scrollOngoing">
               {rows}
            </div>
            
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

    listIndex = 1;

    destroyIscroll() {
        if (this.myScroll) {
            this.myScroll.destroy();
            this.myScroll = null;
        }
    }
   
    fetchData(url , callback = function() {}) {
        ajax({
            url: url,
            type: 'get',
            done: callback
        });
    }
    tabHeaderClickHandler(event) {
        let value = event.target.dataset.value;
       let top=this.myScroll.y-10;
       let imgHeight=document.getElementById('imageTopHead').scrollHeight*1.1;
        if(/active/.test(event.target.className) ) {
            return;
        }
        this.setState({
          active: value,
          isShowLoading:true

        });
        this.myScroll.scrollTo(0, -imgHeight, 1000);
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
		changeTitle('任务中心');
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

        imagesLoaded(this.refs.mainConWrap).on('always', () => {
            setTimeout(() => {
            if (!this.myScroll) {
                this.refs.mainConWrap.style.height=document.documentElement.clientHeight +'px';
                // this.refs.scrollWrap.style.height = (document.documentElement.clientHeight - this.refs.tabHeader.offsetHeight) + 'px';
                this.myScroll = new IScroll(this.refs.mainConWrap,{
                    probeType: 3,
                    mouseWheel: true,
                    hScrollbar:false,
                    vScrollbar:true,
                    momentum:false,
                    useTransition:false,
                    bounce:false,
                    useTransform:true

                });
                this.myScroll.on('scroll',function() {
                     let imgHeight=document.getElementById('imageTopHead').scrollHeight;
                    let tabHeaderDom=document.getElementById('tabHeaderDom');
                    let maxY=Math.abs(this.myScroll.scrollerHeight);
                    let menuHeight=tabHeaderDom.clientHeight*0.5;
                    let curY=Math.abs(this.myScroll.y)+menuHeight;

                    if(curY>imgHeight) {
                        tabHeaderDom.setAttribute('style','top:'+curY+'px;width:100%;left:0;height:1rem; line-height:1rem');
                    }
                    else {
                        tabHeaderDom.setAttribute('style','top:5.33rem;');
                    }
                }.bind(this));

            }
            else {
                this.myScroll.refresh();
            }
          },100);


        });
    }

	componentWillUnmount() {
		this.destroyIscroll.call(this);
	}
	render() { 
        let loading = null;
  		return (
			<div className={main} >
                <div className="bodyCon" ref='mainConWrap'>
                <div className="clearfix">
                <div className="imageTopHead" id="imageTopHead" ref="imageTopHead"></div>
			    <div className={classNames({'MenuBox':true})} style={{top:this.state.menuTop}}  ref="tabHeader" id="tabHeaderDom">
			        <ul >
                        {MenuData.tabHeader.map((value, index) => {
                            return <li className={classNames({ 'MenuBoxItemNormal': true, active: this.state.active === value.value })} key={index} data-value={value.value} onTouchTap={this.tabHeaderClickHandler.bind(this)}>{value.label}</li>;
                        })}
			        </ul>
			    </div>
		
    			<div className="ContentBox" ref="scrollWrap">
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
export default taskCenter;



