<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.peach_blossom_festival}" pageJavascript="${js.peach_blossom_festival}"  title="恭喜你！领到380元红包">

<div class="container"
     data-get="<#if drewCoupon??>${drewCoupon?c}</#if>"
     data-success="<#if drawSuccess??>${drawSuccess?c}</#if>"
     data-start-time="${activityStartTime}"
     data-over-time="${activityEndTime}">
    <div class="get-button get_it_btn">
    </div>
    <div class="get-button got_it_btn">
    </div>

</div>


    <#include "../module/login-tip.ftl" />
<script>
    wx.ready(function () {
        wx.onMenuShareAppMessage({
            title: '恭喜你！领到380元红包', // 分享标题
            desc: '恭喜你！领到380元红包', // 分享描述
            link: '${webServer}/activity/spring-breeze/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标
            success: function () {
            },
            cancel: function () {
            }
        });

        wx.onMenuShareTimeline({
            title: '恭喜你！领到380元红包', // 分享标题
            link: '${webServer}/activity/spring-breeze/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
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