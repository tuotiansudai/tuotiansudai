<#import "wechat-global-dev.ftl" as global>

<#assign jsName = 'share_coupon' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>


<@global.main pageCss="${css.share_coupon}" pageJavascript="${js.share_coupon}"  title="助力好友抢红包" >

<div class="share-coupon-container" id="shareCoupon">
    <h3 class="title-item">恭喜您获得了一个优惠券兑换码</h3>
    <div class="copy-item">
    	<input type="text" class="copy-int" value="WEOFJWEFIJWEIFQID" readonly id="clipboardText">
    	<span class="copy-btn" data-clipboard-action="copy" data-clipboard-target="#clipboardText">复制兑换码</span>
    </div>
    <div class="intro-title">
    	优惠券兑换说明：
    </div>
    <div class="intro-container">
    	<p>在此页面复制兑换码后，可在拓天速贷APP<span>“我的-优惠券-兑换码兑换”</span>中粘贴此兑换码进行兑换；</p>
    	<p class="red-item tc">
    		<img src="" id="redBag" width="60%">
    	</p>
    	<p>点击下方按钮分享给好友，将会送给好友一个现金红包，每邀请一个新用户注册领取，或老用户使用成功，即可获得<span>5000元体验金</span>奖励。每人每日最多可邀请5人领取，超出部分的邀请将不再发放体验金奖励。</p>
    </div>
</div>
</@global.main>