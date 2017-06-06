<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.coupon_special_2017}" pageJavascript="${js.coupon_special_2017}"  title="新手福利_拓天新手投资_拓天速贷" >

<div class="wechat-frame-box" id="wechatCouponSpecial">

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