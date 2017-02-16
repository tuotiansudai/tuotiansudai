<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.login}" pageJavascript="${js.invest_success}" activeLeftNav="" title="成功">
<div class="invest-success-container">
    <div class="invest-text-model">
        <i class="success-tip"></i>

        <p class="pay-tip">${message}</p>
    <#if "ptp_mer_bind_card" == service>
        <p class="pay-text"><span class="count-time">3</span>秒后将自动返回"个人资料"，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>您还可以<a href="/recharge">去充值</a></p>
    </#if>
    <#if "ptp_mer_replace_card" == service>
        <p class="pay-text">您的换卡申请已提交，换卡申请最快两小时处理完成。</p>
        <p class="pay-text"><span class="count-time">3</span>秒后将自动返回"个人资料"，如果没有跳转，您可以 <a href="/personal-info">点击这里</a></p>
    </#if>
        <p class="pay-problem">如有其他疑问请致电客服 400-169-1188（服务时间：9:00－20:00）</p>
    </div>
</div>
</@global.main>