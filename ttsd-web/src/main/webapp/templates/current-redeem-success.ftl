<#import "macro/global.ftl" as global>

<@global.main pageCss="${css.current_redeem_success}" pageJavascript="${js.current_redeem_success}" activeLeftNav="" title="转出成功">
<div class="invest-success-container" id="successBox">
    <div class="invest-text-model">
        <i class="success-tip"></i>

        <#if "success" == result>
            <p class="pay-tip">转出申请成功，申请赎回金额：${amount!}元</p>
            <p class="pay-text">转出后资金将返回账户余额，下一个工作日到账，节假日顺延，最终到账时间以实际到账时间为准。</p>
            <p class="pay-text">
                <span class="count-time">5</span>秒后将自动返回“我的账户”。<a href="/">点击此处返回首页</a>
            </p>
        </#if>

        <p class="pay-problem">如有其他疑问请致电客服 400-169-1188（服务时间：9:00－20:00）</p>
    </div>
</div>

</@global.main>