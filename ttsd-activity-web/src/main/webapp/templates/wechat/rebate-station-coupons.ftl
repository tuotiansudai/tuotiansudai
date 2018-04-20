<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.rebate_station_coupons}" pageJavascript="${js.rebate_station_coupons}"  title="扫码赠0.5%加息券">
<div class="container"
     data-get="<#if drewCoupon??>${drewCoupon?c}</#if>"
     data-success="<#if drawSuccess??>${drawSuccess?c}</#if>"
     data-start-time="${activityStartTime}"
     data-over-time="${activityEndTime}">
    <div class="tip_text">
        <div>您已经领取</div>
        <div>加息券已发放至您的账户</div>
    </div>
    <div class="handle_btn"></div>
    <div class="content"></div>
    <div class="single_coupon single_coupon1"></div>
    <div class="single_coupon single_coupon2"></div>
</div>
    <#include "../module/login-tip.ftl" />
<script>
    wx.ready(function () {
        wx.onMenuShareAppMessage({
            title: '返利加油站-邀请好友助力，最高返现1%×2', // 分享标题
            desc: '我在拓天速贷app发现了一个助力得现金奖励的活动，快来参加吧！', // 分享描述
            link: '${webServer}/activity/invite-help/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

        wx.onMenuShareTimeline({
            title: '返利加油站-邀请好友助力，最高返现1%×2', // 分享标题
            link: '${webServer}/activity/invite-help/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
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