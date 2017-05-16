<#import "wechat-global-dev.ftl" as global>

<#assign jsName = 'dragon_share' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>


<@global.main pageCss="${css.dragon_share}" pageJavascript="${js.dragon_share}"  title="助力好友抢红包" >

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
<script>
    wx.ready(function () {
//        分享给朋友
        wx.onMenuShareAppMessage({
            title: '是朋友就帮我一起把夏季燃爆', // 分享标题
            desc: '领取投资红包50元，最低50元起投', // 分享描述
            link: '${webServer}/activity/mid-summer/invited-user?mobile=${mobile}', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#FloatingBox').hide();
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                $('#FloatingBox').hide();
            }
        });

//        分享到朋友圈
        wx.onMenuShareTimeline({
            title: '是朋友就帮我一起把夏季燃爆', // 分享标题
            link: '${webServer}/activity/mid-summer/invited-user?mobile=${mobile}', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#FloatingBox').hide();
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                $('#FloatingBox').hide();
            }
        });
    });
</script>
</@global.main>