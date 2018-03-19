<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.peach_blossom_festival}" pageJavascript="${js.peach_blossom_festival}"  title="520元开工红包">

<div class="container"
     data-get="true"
     data-success="true"
     data-start-time="2018-02-01"
     data-over-time="2018-04-31">
    <div class="get-button">
    </div>

</div>


    <#include "../module/login-tip.ftl" />
<script>
    <#--wx.ready(function () {-->
        <#--wx.onMenuShareAppMessage({-->
            <#--title: '拓天HR给你一个开工红包', // 分享标题-->
            <#--desc: '无红包，不开工！', // 分享描述-->
            <#--link: '${webServer}/activity/start-work/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致-->
            <#--imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标-->
            <#--success: function () {-->
            <#--},-->
            <#--cancel: function () {-->
            <#--}-->
        <#--});-->

        <#--wx.onMenuShareTimeline({-->
            <#--title: '拓天HR给你一个开工红包', // 分享标题-->
            <#--link: '${webServer}/activity/start-work/wechat?come=wechat', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致-->
            <#--imgUrl: '${commonStaticServer}/images/icons/share_redPocket.png', // 分享图标-->
            <#--success: function () {-->
                <#--// 用户确认分享后执行的回调函数-->
            <#--},-->
            <#--cancel: function () {-->
                <#--// 用户取消分享后执行的回调函数-->
            <#--}-->
        <#--});-->
    <#--});-->
</script>
</@global.main>