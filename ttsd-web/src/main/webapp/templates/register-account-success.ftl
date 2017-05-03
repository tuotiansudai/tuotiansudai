<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="成功">
<div class="invest-success-container" id="successBox">
    <div class="invest-text-model">
        <i class="success-tip"></i>
        <p class="pay-tip">实名认证成功</p>
        <p class="pay-text">
            <span class="count-time">3</span>秒后将自动返回"个人资料"，如果没有跳转，您可以 <a href="/personal-info">点击这里</a>
        </p>
        <p class="pay-text">您还可以 <a href="/bind-card">去绑定银行卡</a></p>
        <p class="pay-problem">如有其他疑问请致电客服 400-169-1188（服务时间：9:00－20:00）</p>
    </div>
</div>
</@global.main>