
<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="业务失败">
<div class="head-banner"></div>
<div>
    <div class="callBack_container">
        <div class="success_tip_icon"></div>
        <p class="my_pay_tip">${message!('业务处理失败')}</p>
        <div class="handle_btn_container">
            <div class="retry" data-redirect-to="${bankCallbackType.getRetryPath()}">再次尝试</div>
        </div>
    </div>

    <p class="pay-problem">如有其他疑问请致电客服 400-169-1188<br/>（服务时间：9:00－20:00）</p>
</div>
</@global.main>