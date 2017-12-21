<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.dragon_share}" pageJavascript="${js.dragon_share}"  title="助力好友抢红包" >

<div class="share-coupon-container" id="shareCoupon">
    <h3 class="title-item"><#if activityEnd>活动已结束<#elseif exchangeCode??>恭喜您获得了一个优惠券兑换码<#else>您尚未登录</#if></h3>
    <div class="copy-item">
    	<input type="text" class="copy-int" value="<#if activityEnd>活动已结束<#elseif exchangeCode??>${exchangeCode!}<#else>您尚未登录</#if>" readonly id="clipboardText">
    	<span class="copy-btn <#if activityEnd>actor-over</#if>" data-clipboard-action="copy" data-clipboard-target="#clipboardText">复制兑换码</span>
    </div>
    <div class="intro-title">
    	优惠券兑换说明：
    </div>
    <div class="intro-container">
    	<p>在此页面复制兑换码后，可在拓天速贷APP<span>“我的-优惠券-兑换码兑换”</span>中粘贴此兑换码进行兑换；</p>
    	<p class="red-item tc">
    		<img src="" id="redBag" width="80%">
    	</p>
    	<p>点击下方按钮分享给好友，将会送给好友一个投资红包，每邀请一个新用户注册领取，即可获得<span>5000元体验金</span>奖励。每人每日最多可邀请5人领取，超出部分的邀请将不再发放体验金奖励。</p>
    </div>
    <div class="share-btn">
        <span id="shareBtn">分享红包给好友</span>
    </div>
    <div class="share-box" id="shareBox">
        <span class="share-close">我知道了</span>
    </div>
</div>
<script>
    wx.ready(function () {
        //分享给朋友
        wx.onMenuShareAppMessage({
            title: '送你10元投资红包！', // 分享标题
            desc: '我在拓天速贷参加端午节活动，现在送你10元投资红包，快来拿！', // 分享描述
            link: '${webServer}/we-chat/authorize?redirect=/activity/dragon/wechat/shareLanding?sharerUnique=${loginName!}-${unique!}', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#shareBox').hide();
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                $('#shareBox').hide();
            }
        });

        //分享到朋友圈
        wx.onMenuShareTimeline({
            title: '送你10元投资红包！', // 分享标题
            link: '${webServer}/we-chat/authorize?redirect=/activity/dragon/wechat/shareLanding?sharerUnique=${loginName!}-${unique!}', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
                $('#shareBox').hide();
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
                $('#shareBox').hide();
            }
        });
    });
</script>
</@global.main>