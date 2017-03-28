import  "mobileStyle/messageCenter.scss";

import React from "react";
import IScroll from "iscroll";
import imagesLoaded from "imagesloaded";
import {mobileCommon} from 'mobileJsModule/mobileCommon';

class messageCenter extends React.Component {
    state = {
        "code": "",
        "message": "",
        "data": {
            "userMessageId": "", //消息ID
            "title": "", //消息标题
            "content": "", //文章内容
            "createdTime": "", //创建时间
            "appUrl": ""//app跳转路径
        }
    };
    destroyIscroll() {
        if (this.myScroll) {
            this.myScroll.destroy();
            this.myScroll = null;
        }
    }

    componentDidMount() {
        mobileCommon.changeTitle('消息中心');
        mobileCommon.ajax({
            url: `/message-center/userMessage/${this.props.params.id}`,
            type: 'get',
            done: function (data) {
                this.setState(data);
            }.bind(this),
            fail: function (xhr) {
                console.error(xhr);
            }.bind(this)
        });
    }
    goTo(event) {
        let href = event.target.dataset.href;
        window.location.href = href;
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
        let btn=null;

        if(this.state.data.appUrl){
            btn=<section className="info clearfix"><a onTouchTap={this.goTo.bind(this)} data-href={this.state.data.appUrl}>去看看</a></section>;
        }
        return (
            <div ref="scrollWrap" className='message-center-frame'>
                <article>
                    <section className="meta">
                        <h1 dangerouslySetInnerHTML={{__html: this.state.data.title}}></h1>
                        <time className="pull-right">发送时间：{this.state.data.createdTime}</time>
                    </section>
                    <section className="line"></section>
                    <section className="content" dangerouslySetInnerHTML={{__html: this.state.data.content}}>
                    </section>
                </article>
                {btn}
            </div>
        );
    }
}
export default messageCenter;