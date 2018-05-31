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
            <#--充值显示充值金额-->
                充值成功，充值金额 1.00元
            <#--申请提现成功-->
                申请提现成功，申请提现金额 1.00元
            <#--投资成功-->
                支付成功，您已成功投资 100.00元
            </#if>

        </p>
    <#--实名认证成功-->
        <#if bankCallbackType == 'REGISTER'>
            <div class="handle_btn_container">
                <form action="${requestContext.getContextPath()}/bank-card/bind/source/WEB" method="post"
                      style="display: inline-block;float:right">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="submit" value="去绑卡">
                </form>
            </div>
        </#if>
    <#--充值成功-->
        <#if bankCallbackType == 'RECHARGE'>
            <div class="handle_btn_container">
                <div class="see_my_account">查看我的账户</div>
                <div class="go_to_invest investBtn">去投资</div>
            </div>
        </#if>
    <#--授权成功-->
        <#if bankCallbackType == 'AUTHORIZATION'>
            <div class="handle_btn_container">
                <div class="go_to_invest investBtn">去投资</div>
            </div>
        </#if>
    <#--申请提现成功-->
        <#if bankCallbackType == 'WITHDRAW'>
            <div class="handle_btn_container">
                <div class="see_my_account">前往我的账户</div>

            </div>
        </#if>
    <#--银行卡绑定成功-->
        <#if bankCallbackType == 'CARD_BIND'>
            <div class="handle_btn_container">
                <div class="go_to_invest investBtn">去投资</div>
            </div>
        </#if>
    <#--充值成功-->
        <#if bankCallbackType == 'CARD_BIND'>
            <div class="handle_btn_container">
                <div class="go_to_invest investBtn">去投资</div>
            </div>
        </#if>
    <#--解绑成功-->
        <#if bankCallbackType == 'CANCEL_CARD_BIND'>
            <div class="handle_btn_container">
                <div class="go_to_invest investBtn">去投资</div>
            </div>
        </#if>
    <#--投资成功-->
        <#if bankCallbackType == 'LOAN_INVEST'>
            <div class="handle_btn_container">
                <div class="go_to_invest investBtn">查看其它项目</div>
            </div>
        </#if>
    </div>

    <p class="pay-problem">如有其他疑问请致电客服 400-169-1188<br/>（服务时间：9:00－20:00）</p>
</div>
</@global.main>