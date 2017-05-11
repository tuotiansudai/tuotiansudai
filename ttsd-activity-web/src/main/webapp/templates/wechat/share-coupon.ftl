<#import "wechat-global-dev.ftl" as global>

<#assign jsName = 'share_coupon' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>


<@global.main pageCss="${css.share_coupon}" pageJavascript="${js.share_coupon}"  title="助力好友抢红包" >

<div class="share-coupon-container" id="shareCoupon">
    <h3 class="title-item">恭喜您获得了一个优惠券兑换码</h3>
</div>
</@global.main>