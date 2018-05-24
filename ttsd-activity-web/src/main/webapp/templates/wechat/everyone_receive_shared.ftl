<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.everyone_receive_shared}" pageJavascript="${js.everyone_receive_shared}"  title="拓天速贷人人可领10元现金">
<div class="top_container">
    <div class="nickName">${name!}</div>
    <div class="rules"></div>
</div>
<div class="content_text">
    <div class="title"></div>
    <#if !isHelp>
        <div class="no_shared">
            <div class="desc">
                <div>您的朋友${name!}邀请你帮他助力</div>
            </div>
            <div class="help_rightNow" id="nowHelpId" data-help-id="${helpModel.id?c}"></div>
            <div class="help_too" data-start-time="${activityStartTime}" data-over-time="${activityEndTime}" data-own-help="${existOwnHelp?c}"></div>
        </div>
    <#else >
        <div class="has_shared">
            <div class="desc">
                <div class="joined">您已经参与了助力</div>
                <div>您的好友已经获得<span class="strong">${helpModel.helpUserCount}位</span>好友助力</div>
                <div>获得了<span class="strong">${(helpModel.reward/100)?string('0.00')}元</span>助力现金奖励</div>
            </div>
            <div class="help_too" data-start-time="${activityStartTime}" data-over-time="${activityEndTime}" data-own-help="${existOwnHelp?c}"></div>
        </div>
    </#if>
    <div class="countDown_time_wrapper">
        <span class="text" id="countDown" data-countdown="${helpModel.endTime?string('yyyy-MM-dd HH:mm:ss')}">倒计时：</span>
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
            <#if helpFriends??>
                <div class="list">
                    <#list helpFriends as friend>
                        <div class="list_item">
                            <#if friend.headImgUrl?? && friend.headImgUrl?trim?has_content>
                                <img class="portrait" src="${friend.headImgUrl!}" />
                            <#else >
                                <div class="portrait"></div>
                            </#if>
                            <div class="nickName">${friend.nickName!}</div>
                            <div class="finish_time">${friend.createdTime?string('yyyy-MM-dd HH:mm:ss')}</div>
                        </div>
                    </#list>
                </div>
                <#if helpFriends?size == 0>
                    <div class="no_help">还没有获得好友助力</div>
                </#if>
            <#else>
                <div class="no_help">还没有获得好友助力</div>
            </#if>
        </div>
    </div>
</div>
<div class="flex_rules">
    <div class="close_rules"></div>
    <div class="desc first_desc">1.活动期间，用户分享活动链接至微信群/朋友圈/任意好友，邀请好友为您助力，分享后24小时内的好友助力有效；</div>
    <div class="desc">2.每多邀请1人点击，可获得0.2元现金奖励，最高可获得10元现金奖励；</div>
    <div class="desc">3.奖励将于用户成功分享24小时后统一发放到账户，可全部提现；</div>
    <div class="desc">4.提现流程：1:点击“去提现”2:登录“拓天速贷”3:完成实名认证；</div>
    <div class="desc">5.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</div>
</div>
<div class="wechat_share_tip">
    <div class="tip_icon"></div>
</div>
    <#include "../module/login-tip.ftl" />
<script>
    wx.ready(function () {
        wx.onMenuShareAppMessage({
            title: '我正在拓天速贷领10元现金，就差你助力了！', // 分享标题
            desc: '我在拓天速贷app发现了一个助力得现金奖励的活动，快来参加吧！', // 分享描述
            link: '${webServer}/we-chat/active/authorize?redirect=/activity/invite-help/wechat/share/${helpModel.id?c}/everyone/help?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

        wx.onMenuShareTimeline({
            title: '我正在拓天速贷领10元现金，就差你助力了！', // 分享标题
            link: '${webServer}/we-chat/active/authorize?redirect=/activity/invite-help/wechat/share/${helpModel.id?c}/everyone/help?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
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