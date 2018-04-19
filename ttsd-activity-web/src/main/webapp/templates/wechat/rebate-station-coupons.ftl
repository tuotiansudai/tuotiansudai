<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.rebate_station_coupons}" pageJavascript="${js.rebate_station_coupons}"  title="扫码赠0.5%加息券">
<div class="container"
     data-get="<#if drewCoupon??>${drewCoupon?c}</#if>"
     data-success="<#if drawSuccess??>${drawSuccess?c}</#if>"
     data-start-time="${activityStartTime}"
     data-over-time="${activityEndTime}">
    <div class="tip_text" style="display: none">
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
            title: '拓天HR给你一个开工红包', // 分享标题
            desc: '无红包，不开工！', // 分享描述
            link: '${webServer}/activity/invite-help/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

        wx.onMenuShareTimeline({
            title: '拓天HR给你一个开工红包', // 分享标题
            link: '${webServer}/activity/invite-help/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
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