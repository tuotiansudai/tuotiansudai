<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.start_work}" pageJavascript="${js.start_work}"  title="新年更有钱">

<div class="container" data-get="false" data-start-time="2018-02-01 15:25:00" data-over-time="2018-02-14 15:25:00">
    <div class="get_it_btn"></div>
    <div class="got_it_btn"></div>
    <div class="plus_ticket"></div>
    <div class="red_ticket200"></div>
    <div class="red_ticket100"></div>
    <div class="red_ticket50"></div>
    <div class="red_ticket30"></div>
</div>
<div class="pop_modal_container">
    <div class="pop_modal_bg">
        <div class="pop_modal">
            <div class="tip_text">开工红包已发放</div>
            <div class="see_my_redPocket"></div>
            <div class="closeBtn"></div>
        </div>
    </div>
</div>
<script>
    wx.ready(function () {
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