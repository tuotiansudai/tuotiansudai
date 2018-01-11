<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="成功">
<div class="invest-success-container" id="successBox">
    <div class="invest-text-model">
        <i class="success-tip"></i>
        <#if error??>
            <p class="pay-tip">${error}</p>
        <#else>
            <#if "PTP_MER_BIND_CARD" == service>
                <p class="pay-tip">银行卡绑定成功</p>
                <p class="pay-text">
                    <span class="count-time">5</span>秒后将自动返回“个人资料”，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>
                </p>
                <p class="pay-text">您还可以 <a href="/recharge">去充值</a></p>
            </#if>

            <#if "PTP_MER_REPLACE_CARD" == service>
                <p class="pay-tip">提交成功</p>
                <p class="pay-text">您的换卡申请已提交，换卡申请最快两小时处理完成。</p>
                <p class="pay-text">
                    <span class="count-time">5</span>秒后将自动返回“个人资料”，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>
                </p>
            </#if>

            <#if ["INVEST_PROJECT_TRANSFER", 'INVEST_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
                <p class="pay-tip">支付成功，您已成功投资 ${amount} 元</p>
                <p class="pay-text">
                    <span class="count-time">5</span>秒后将自动返回“我的账户”。如果页面没有跳转，您可以 <a href="/account">点击这里</a>您还可以<a
                        href="/loan-list">继续投资</a>
                </p>
            </#if>

            <#if ['INVEST_TRANSFER_PROJECT_TRANSFER', 'INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
                <p class="pay-tip">支付成功，您已成功购买该债权。</p>
                <p class="pay-text">
                    <span class="count-time">5</span>秒后将自动返回“我的账户”。如果页面没有跳转，您可以 <a href="/account">点击这里</a>您还可以<a
                        href="/loan-list">继续购买</a>
                </p>
            </#if>
            <#if "CUST_WITHDRAWALS" == service>
                <p class="pay-tip">申请提现成功,申请提现金额${amount!} 元</p>
                <p class="pay-text">
                    <span class="count-time">5</span>秒后将自动返回“个人资料”，如果没有跳转，您可以 <a href="/account">点击这里</a>
                </p>
            </#if>
        </#if>

        <p class="pay-problem">如有其他疑问请致电客服 400-169-1188（服务时间：9:00－20:00）</p>
    </div>
</div>


<div class="invest-success-phone">
    <#if error??>
        <p class="pay-tip">${error}</p>
    <#else>
        <#if "PTP_MER_BIND_CARD" == service>
            <p class="pay-tip">银行卡绑定成功</p>
            <p class="pay-text">
                <span class="count-time">5</span>秒后将自动返回“个人资料”，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>
            </p>
            <p class="pay-text">您还可以 <a href="/recharge">去充值</a></p>
        </#if>

        <#if "PTP_MER_REPLACE_CARD" == service>
            <p class="pay-tip">提交成功</p>
            <p class="pay-text">您的换卡申请已提交，换卡申请最快两小时处理完成。</p>
            <p class="pay-text">
                <span class="count-time">5</span>秒后将自动返回“个人资料”，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>
            </p>
        </#if>

        <#if ["INVEST_PROJECT_TRANSFER", 'INVEST_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
            <p class="pay-tip">支付成功，您已成功投资 ${amount} 元</p>
            <p class="pay-text">
                <span class="count-time">5</span>秒后将自动返回“我的账户”。如果页面没有跳转，您可以 <a href="/account">点击这里</a>您还可以<a
                    href="/loan-list">继续投资</a>
            </p>
        </#if>

        <#if ['INVEST_TRANSFER_PROJECT_TRANSFER', 'INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD']?seq_contains(service)>
            <p class="pay-tip">支付成功，您已成功购买该债权。</p>
            <p class="pay-text">
                <span class="count-time">5</span>秒后将自动返回“我的账户”。如果页面没有跳转，您可以 <a href="/account">点击这里</a>您还可以<a
                    href="/loan-list">继续投资</a>
            </p>
        </#if>
    </#if>

    <p class="pay-problem">如有其他疑问请致电客服 400-169-1188<br/>（服务时间：9:00－20:00）</p>
</div>
</@global.main>