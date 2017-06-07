<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.coupon_special_2017}" pageJavascript="${js.coupon_special_2017}"  title="领券专场_拓天周年庆_活动中心_拓天速贷" >

<div class="wechat-frame-box" id="wechatCouponSpecial" data-drew="<#if drewCoupon??>${drewCoupon?c}</#if>">

    <div class="text-box">
        <span>周年庆专享</span>
        <#if duringActivities>
            <a href="/activity/celebration-coupon/draw" class="btn-receive"></a>
        <#else>
            <a href="javascript:void(0)" class="btn-receive-disabled"></a>
        </#if>
    </div>

</div>

</@global.main>