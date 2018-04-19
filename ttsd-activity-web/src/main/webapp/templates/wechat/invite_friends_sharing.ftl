<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.invite_friends_sharing}" pageJavascript="${js.invite_friends_sharing}"  title="拓天速贷-好友助力得现金">

<div class="top_container">
    <div class="nickName">${helpModel.userName}</div>
    <div class="rules"></div>
</div>
<div class="content_text">
    <div class="title"></div>
    <div class="desc">
        <div class="total">投资金额：<span class="strong">${(helpModel.investAmount/100)?string('0.00')}元</span></div>
        <div class="profit">该笔投资已经获得<span class="strong">${(helpModel.reward/100)?string('0.00')}元现金</span></div>
        <#if nextNode??>
            <div class="differ">还差${nextNode}个好友助力即可得到<span class="strong">${(nextAmount/100)?string('0.00')}元现金</span>
            </div></#if>
    </div>
    <div class="percent_wrapper">
        <div id="cycle" data-cashchain="${myCashChain?join(',')}" data-mycash="${(helpModel.reward/100)?string('0.00')}"></div>
        <#list myCashChain as chain>
            <div class="percent percent${chain_index + 1}">${chain}<br/>元</div>
        </#list>
        <div class="invite_friends_btn"></div>
        <div class="text text1">2人</div>
        <div class="text text2">8人</div>
        <div class="text text3">18人</div>
        <div class="text text4">58人</div>
        <div class="text text5">88人</div>
        <div class="text text6">108人</div>
    </div>
    <div class="countDown_time_wrapper">
        <span class="text" id="countDown" data-count-down="${helpModel.endTime?string('yyyy-MM-dd HH:mm:ss')}">倒计时：</span>
        <div class="pic_wrapper">
            <div class="time_num_wrapper">
                <div class="time_num hour1"></div>
                <div class="time_num hour2"></div>
                <span class="icon">:</span>
                <div class="time_num minutes1"></div>
                <div class="time_num minutes2"></div>
                <span class="icon">:</span>
                <div class="time_num seconds1"></div>
                <div class="time_num seconds2"></div>
            </div>
        </div>
        <div class="time_over">已结束</div>
    </div>
    <div class="invited_friends_container">
        <div class="title">已助力好友（共${helpModel.helpUserCount}人）</div>
        <div class="content">
            <#if helpFriends!?if_exists?size !=0 >
                <div class="list">
                    <#list helpFriends as friend>
                        <div class="list_item">
                            <div class="portrait"></div>
                            <div class="nickName">${friend.nickName}</div>
                            <div class="finish_time">${friend.createdTime?string('yyyy-MM-dd HH:mm:ss')}</div>
                        </div>
                    </#list>
                </div>
            <#else>
                <div class="no_help">您还没有获得好友助力，快去邀请吧</div>
            </#if>
        </div>
    </div>
</div>
<div class="flex_rules">
    <div class="close_rules"></div>
    <div class="desc first_desc">
        1、用户投资后24小时内邀请助力好友累计人数越多，获得现金奖励越多，全部现金可提现。
        <table class="flex_table">
            <thead>
            <tr>
                <th>最少助力人数</th>
                <th>返现利率（年化）</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>2</td>
                <td>0.2%</td>
            </tr>
            <tr>
                <td>8</td>
                <td>0.5%</td>
            </tr>
            <tr>
                <td>18</td>
                <td>0.6%</td>
            </tr>
            <tr>
                <td>58</td>
                <td>0.7%</td>
            </tr>
            <tr>
                <td>88</td>
                <td>0.8%</td>
            </tr>
            <tr>
                <td>108</td>
                <td>1%</td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="desc">2、活动期间，为投资用户直接助力的好友，可平分与投资用户等额的现金奖励。现金可直接提现；</div>
    <div class="desc">3、项目放款后48小时内发放至双方用户账户。</div>
    <div class="desc">4、被邀请好友成功参与助力后，可再次分享活动链接邀请好友为自己助力，每邀请一人即可多得0.2元，最高可再多提10元现金。</div>
    <div class="desc">5. 本活动所发放的现金奖励可提现，用户可在pc端“我的账户”或APP端“我的”中进行查看，未绑定过银行卡的用户，需完成绑卡后方可查看和提现；</div>
    <div class="desc">6.截止发放时间为止，如果助力好友未登录拓天速贷并进行实名认证，将无法收到奖励；</div>
    <div class="desc">7.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</div>
</div>
<div class="wechat_share_tip">
    <div class="tip_icon"></div>
</div>

    <#include "../module/login-tip.ftl" />
<script>
    wx.ready(function () {
        wx.onMenuShareAppMessage({
            title: '返利加油站-邀请好友助力，最高返现1%×2', // 分享标题
            desc: '我在拓天速贷app发现了一个助力得现金奖励的活动，快来参加吧！', // 分享描述
            link: '${webServer}/we-chat/authorize?redirect=/activity/invite-help/wechat/share/${helpModel.id}/invest/help?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

        wx.onMenuShareTimeline({
            title: '返利加油站-邀请好友助力，最高返现1%×2', // 分享标题
            link: '${webServer}/we-chat/authorize?redirect=/activity/invite-help/wechat/share/${helpModel.id}/invest/help?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
    });
</script>
</@global.main>