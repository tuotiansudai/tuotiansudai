<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.coupon_special_2017}" pageJavascript="${js.coupon_special_2017}"  title="领券专场_拓天周年庆_活动中心_拓天速贷" >
<style type="text/css">
    body {
        height:100%;
    }
</style>
<div class="wechat-frame-box success-box" id="wechatCouponSpecial">

    <div class="border-title">
            <span>领取成功</span>
        <span class="bg-border"></span>
    </div>

    <div class="red-bag-list clearfix">
        <dl>
            <dt><span>0.8</span><i>%</i><em>加息券</em></dt>
            <dd>50元起投</dd>
        </dl>
        <dl>
            <dt><span>400</span><i>元</i><em>红包</em></dt>
            <dd>32万元起投</dd>
        </dl>
        <dl>
            <dt><span>200</span><i>元</i><em>红包</em></dt>
            <dd>16万元起投</dd>
        </dl>
        <dl>
            <dt><span>180</span><i>元</i><em>红包</em></dt>
            <dd>14.4万元起投</dd>
        </dl>
        <dl>
            <dt><span>100</span><i>元</i><em>红包</em></dt>
            <dd>8万元起投</dd>
        </dl>
        <dl>
            <dt><span>60</span><i>元</i><em>红包</em></dt>
            <dd>4.8万元起投</dd>
        </dl>
        <dl>
            <dt><span>30</span><i>元</i><em>红包</em></dt>
            <dd>2.4万元起投</dd>
        </dl>
        <dl>
            <dt><span>20</span><i>元</i><em>红包</em></dt>
            <dd>1.6万元起投</dd>
        </dl>
        <dl>
            <dt><span>10</span><i>元</i><em>红包</em></dt>
            <dd>0.8万元起投</dd>
        </dl>
    </div>

    <p class="app-note">优惠券已发放到您的账户，可以去APP、微信、PC网站
        我的优惠券页面进行查看</p>

    <p class="remark">活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有</p>

</div>
<script>
    wx.ready(function () {
//        分享给朋友
        wx.onMenuShareAppMessage({
            title: '@所有人 关注公众号就能领红包，速来！', // 分享标题
            desc: '我已在拓天速贷抢到1000元红包，你也来抢吧', // 分享描述
            link: '${webServer}/activity/celebration-coupon?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/logo-tip.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

//        分享到朋友圈
        wx.onMenuShareTimeline({
            title: '@所有人 关注公众号就能领红包，速来！', // 分享标题
            link: '${webServer}/activity/celebration-coupon?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
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