<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="">
<div class="head-banner"></div>
<div>
    <div class="callBack_container">
        <div class="success_tip_icon"></div>
        <p class="my_pay_tip">
            <#if isInProgress>
                业务正在处理中，请稍后查询。
            <#else>
                ${bankCallbackType.getTitle()}
            </#if>

        </p>
        <div class="handle_btn_container">
            <div class="see_my_account">查看我的账户</div>

            <div class="go_to_invest investBtn">去投资</div>
        </div>
    </div>

    <p class="pay-problem">如有其他疑问请致电客服 400-169-1188<br/>（服务时间：9:00－20:00）</p>
</div>
</@global.main>