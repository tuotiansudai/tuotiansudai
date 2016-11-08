import React from 'react';
import changeTitle from 'utils/changeTitle';
import { main } from './messageCenter.scss';
import IScroll from 'iscroll';
import imagesLoaded from 'imagesloaded';
import Praise from 'components/licaiCircle/Praise';
import ajax from 'utils/ajax';

class messageCenter extends React.Component {
    state = {
        "code": "",
        "message": "",
        "data":{
            "articleId": "",//消息ID
            "title": "",//消息标题
            "content": "",//文章内容
            "createTime": ""//创建时间
        }
    };
    destroyIscroll() {
        if (this.myScroll) {
            this.myScroll.destroy();
            this.myScroll = null;
        }
    }
    componentDidMount() {
        changeTitle('消息中心');
        ajax({
            url: `/media-center/article-detail/:articleId`,
            type: 'get',
            done: function(data) {
                this.setState(data);
            }.bind(this),
            fail: function(xhr) {
                console.error(xhr);
            }.bind(this)
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
                        <h1>{this.state.data.title}恭喜您获得128g iphone7手机一部</h1>
                        <time className="pull-right">{this.state.data.createTime}发送时间：2016-10-28  16:40  </time>
                    </section>
                    <section className="content" >
                    <p>夜深人静的时候，好不容易想出来，刚发出去半小时，那边微信就说，这个不行！重来！好吧，又辛辛苦苦改了一稿，结果刚交过去小婊砸直接打电话过来说，要史无前例的感觉，你懂嘛？史无前例！我擦，这厮24小时在线是什么鬼！就不怕影响约炮的心情吗？</p>
                    <p>实在是忍无可忍，有这么逼人的嘛！不想干了，直接免费领算了！反正不是我的钱！官网价6188元的128GB爱疯7，免费领去吧！！就当我冒着被开除的风险，给大家发福利吧！！！恩，就这么做，小婊砸反正也不懂，她不是要劲爆嘛，我给你劲爆！不是要史无前例嘛，给你！我给你方案爆表！大不了这次我绑着小婊砸一起屎！</p>
                    <p className="text-right">拓天速贷客户服务中心</p>
                    <p className="text-right">2016年10月28日 </p>
                    </section>
                    <section className="info clearfix">
                        <a href="/loan-list">立即购买</a>
                    </section>
                </article>
            </div>
        );
    }
}

export default messageCenter;