<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.everyone_receive_sharing}" pageJavascript="${js.everyone_receive_sharing}"  title="拓天速贷人人可领10元现金">
<div class="top_container">
    <div class="nickName">
        <#if helpModel??>
            <#if helpModel.userName??>
                ${helpModel.userName}
            <#elseif helpModel.mobile??>
                ${helpModel.mobile}
            </#if>
        <#else>
            <#if nickName??>
                ${nickName}
            </#if>
        </#if>
    </div>
    <div class="rules"></div>
</div>
<div class="content_text">
    <div class="title"></div>
    <div class="desc">
        <div>您已经获得<span class="strong"><#if helpModel??>${helpModel.helpUserCount}<#else>0</#if>位</span>好友助力</div>
        <div>获得了<span class="strong"><#if helpModel??>${(helpModel.reward/100)?string('0.00')}<#else>0.00</#if>元</span>助力现金奖励</div>
        <div><#if drawEndTime??>请于<span class="strong">${drawEndTime?string('yyyy-MM-dd HH:mm:ss')}</span>前领取</#if></div>
    </div>
    <div class="invite_friends_btn"></div>
    <#if helpModel??>
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
        <div class="withdraw_cash" id="helpId" data-help-id="${helpModel.id}"></div>
    </#if>
    <div class="invited_friends_container">
        <div class="title">已助力好友（共<#if helpModel??>${helpModel.helpUserCount}<#else>0</#if>人）</div>
        <div class="content">
            <#if helpFriends??>
                <div class="list">
                        <#list helpFriends as friend>
                            <div class="list_item">
                                <div class="portrait"></div>
                                <div class="nickName">${friend.nickName}</div>
                                <div class="finish_time">${friend.createdTime?string('yyyy-MM-dd HH:mm:ss')}</div>
                            </div>
                        </#list>
                </div>
                <#if helpFriends?size == 0>
                    <div class="no_help">您还没有获得好友助力，快去邀请吧</div>
                </#if>
            <#else>
                <div class="no_help">您还没有获得好友助力，快去邀请吧</div>
            </#if>
        </div>
    </div>
</div>
<div class="flex_rules">
    <div class="close_rules"></div>
    <div class="desc first_desc">1:活动期间，用户分享活动链接至微信群/朋友圈/任意好友，邀请好友为您助力，分享后24小时内的好友助力有效。</div>
    <div class="desc">2:每多邀请1人点击，可获得0.2元现金奖励，最高可获得10元现金奖励。</div>
    <div class="desc">3:奖励将于用户成功分享24小时后统一发放到账户，可全部提现。</div>
    <div class="desc">4. 本活动所发放的现金奖励可提现，用户可在pc端“我的账户”或APP端“我的”中进行查看，未绑定过银行卡的用户，需完成绑卡后方可查看和提现；</div>
    <div class="desc">5.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</div>
</div>
<div class="wechat_share_tip">
    <div class="tip_icon"></div>
</div>
    <#include "../module/login-tip.ftl" />
<script>
    wx.ready(function () {
        wx.onMenuShareAppMessage({
            title: '拓天HR给你一个开工红包', // 分享标题
            desc: '无红包，不开工！', // 分享描述
            link: '${webServer}/activity/start-work/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

        wx.onMenuShareTimeline({
            title: '拓天HR给你一个开工红包', // 分享标题
            link: '${webServer}/activity/start-work/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
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