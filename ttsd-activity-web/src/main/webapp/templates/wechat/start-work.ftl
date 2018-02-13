<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.start_work}" pageJavascript="${js.start_work}"  title="520元开工红包">

<div class="container" data-get="false" data-success="true" data-start-time="2018-02-01 15:25:00" data-over-time="2018-02-14 15:25:00">
    <div class="get_it_btn"></div>
    <div class="got_it_btn"></div>
    <div class="plus_ticket"></div>
    <div class="red_ticket200"></div>
    <div class="red_ticket100"></div>
    <div class="red_ticket50"></div>
    <div class="red_ticket30"></div>
</div>
<div class="pop_modal_container modal_container">
    <div class="pop_modal_bg">
        <div class="pop_modal">
            <div class="tip_text">开工红包已发放</div>
            <div class="see_my_redPocket"></div>
            <div class="closeBtn"></div>
        </div>
    </div>
</div>
<div class="pop_modal_container_again modal_container">
    <div class="pop_modal_bg">
        <div class="pop_modal">
            <div class="tip_text">开工红包未发放</div>
            <div class="tip_desc">小伙伴领取开工红包激情太高<br/>我们正在为您准备中...</div>
            <div class="see_my_redPocket"></div>
            <div class="closeBtn"></div>
        </div>
    </div>
</div>
    <#include "../module/login-tip.ftl" />
<script>
    wx.ready(function () {
        wx.onMenuShareAppMessage({
            title: '拓天HR给你一个开工红包', // 分享标题
            desc: '无红包，不开工！', // 分享描述
            link: '${webServer}/activity/start-work/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

        wx.onMenuShareTimeline({
            title: '拓天HR给你一个开工红包', // 分享标题
            link: '${webServer}/activity/start-work/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
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