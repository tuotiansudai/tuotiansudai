import React from "react";
import changeTitle from "utils/changeTitle";
import {main} from "./messageCenter.scss";
import IScroll from "iscroll";
import ajax from "utils/ajax";
import {hashHistory} from "react-router";

class messageCenter extends React.Component {
    state = {
        "code": "",
        "message": "",
        "data": {
            "userMessageId": "",//消息ID
            "title": "",//消息标题
            "content": "",//消息内容
            "createdTime": "",//创建时间
            "appUrl": ""//app跳转路径
        }
    };
    destroyIscroll() {
        if (this.myScroll) {
            this.myScroll.destroy();
            this.myScroll = null;
        }
    }

    goTo(event) {
        let href = event.target.dataset.href;
        hashHistory.push(href);
    }
    componentDidMount() {
        changeTitle('消息中心');
        ajax({
            url: `/v1.0/get/userMessage/${this.props.params.id}`,
            type: 'get',
            done: function (data) {
                this.setState(data);
            }.bind(this),
            fail: function (xhr) {
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
                    <section className="meta">
                        <h1>{this.state.data.title}</h1>
                        <time className="pull-right">发送时间：{this.state.data.createTime}</time>
                    </section>
                    <section className="content" dangerouslySetInnerHTML={{__html: this.state.data.content}}>
                    </section>
                    <section className="info clearfix">
                        <a onTouchTap={this.goTo.bind(this)} data-href={this.state.data.appUrl}>去看看</a>
                    </section>
                </article>
            </div>
        );
    }
}

export default messageCenter;