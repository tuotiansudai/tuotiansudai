<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.coupon_special_2017}" pageJavascript="${js.coupon_special_2017}"  title="领券专场_拓天周年庆_活动中心_拓天速贷" >

<div class="wechat-frame-box" id="wechatCouponSpecial" data-drew="<#if drewCoupon??>${drewCoupon?c}</#if>">

    <div class="text-box">
        <span>周年庆专享</span>
        <#if duringActivities>
            <button type="button"  class="btn-receive"></button>
        <#else>
            <a href="javascript:void(0)" class="btn-receive-disabled"></a>
        </#if>
    </div>

</div>

<script>
    wx.ready(function () {
//        分享给朋友
        wx.onMenuShareAppMessage({
            title: '是朋友就帮我一起把夏季燃爆', // 分享标题
            desc: '领取投资红包50元，最低50元起投', // 分享描述
            link: '${webServer}/activity/celebration-coupon', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

//        分享到朋友圈
        wx.onMenuShareTimeline({
            title: '是朋友就帮我一起把夏季燃爆', // 分享标题
            link: '${webServer}/activity/celebration-coupon', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
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