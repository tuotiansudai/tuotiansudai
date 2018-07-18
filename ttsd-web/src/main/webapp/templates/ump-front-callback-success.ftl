<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.ump_callback_success}" pageJavascript="${js.ump_callback_success}" activeLeftNav="" title="成功">
<div class="invest-success-container" id="successBox">
    <div class="invest-text-model">
        <i class="success-tip"></i>
        <p class="pay-tip">${umpCallbackType.getTitle()}</p>
        <p class="pay-text">
            <span class="count-time">5</span>秒后将自动返回“个人资料”，如果没有跳转，您可以 <a href="/ump/account">点击这里</a>
        </p>
        <p class="pay-problem">如有其他疑问请致电客服 400-169-1188（服务时间：9:00－20:00）</p>
    </div>
</div>
</@global.main>