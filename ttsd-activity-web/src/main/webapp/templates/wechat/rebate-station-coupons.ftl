<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.rebate_station_coupons}" pageJavascript="${js.rebate_station_coupons}"  title="520元开工红包">
<div class="top_container">
    <div class="nickName">昵称</div>
    <div class="rules"></div>
</div>
<div class="content_text">
    <div class="title"></div>
    <div class="desc">
        <div class="total">投资金额：<span class="strong">10000.00元</span></div>
        <div class="profit">该笔投资已经获得<span class="strong">500.00元现金</span></div>
        <div class="differ">还差15个好友助力即可得到<span class="strong">xxx.xx元现金</span></div>
    </div>
    <div class="percent_wrapper">
        <div id="cycle"></div>
        <div class="percent percent1">100<br/>元</div>
        <div class="percent percent2">200<br/>元</div>
        <div class="percent percent3">300<br/>元</div>
        <div class="percent percent4">400<br/>元</div>
        <div class="percent percent5">500<br/>元</div>
        <div class="percent percent6">1000<br/>元</div>
        <div class="invite_friends_btn"></div>
        <div class="text text1">2人</div>
        <div class="text text2">8人</div>
        <div class="text text3">18人</div>
        <div class="text text4">58人</div>
        <div class="text text5">88人</div>
        <div class="text text6">108人</div>
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