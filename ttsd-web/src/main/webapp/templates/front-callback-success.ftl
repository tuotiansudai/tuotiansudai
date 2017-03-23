<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="成功">
<div class="invest-success-container" id="successBox">
    <div class="invest-text-model">
        <i class="success-tip"></i>

        <#if error??>
            <p class="pay-tip">${error}</p>
        <#else>
            <#if "ptp_mer_bind_card" == service>
                <p class="pay-tip">银行卡绑定成功</p>
                <p class="pay-text">
                    <span class="count-time">3</span>秒后将自动返回"个人资料"，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>
                </p>
                <p class="pay-text">您还可以 <a href="/recharge">去充值</a></p>
            </#if>

            <#if "ptp_mer_replace_card" == service>
                <p class="pay-tip">提交成功</p>
                <p class="pay-text">您的换卡申请已提交，换卡申请最快两小时处理完成。</p>
                <p class="pay-text">
                    <span class="count-time">3</span>秒后将自动返回"个人资料"，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>
                </p>
            </#if>
        </#if>

        <p class="pay-problem">如有其他疑问请致电客服 400-169-1188（服务时间：9:00－20:00）</p>
    </div>
</div>
</@global.main>