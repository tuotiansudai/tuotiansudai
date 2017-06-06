<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.coupon_special}" pageJavascript="${js.coupon_special}"  title="新手福利_拓天新手投资_拓天速贷" >

<div class="wechat-frame-box" id="wechatCouponSpecial">

    <button type="button" class="btn-receive" <#if !duringActivities>disabled</#if>></button>
</div>

</@global.main>