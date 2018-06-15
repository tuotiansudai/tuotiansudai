<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.bank_callback}" pageJavascript="${m_js.bank_callback}" title="个人资料">

<div class="my-account-content personal-profile">

    <#if bankCallbackType == 'REGISTER'>
        <div class="m-header">开通存管账号</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-success"></div>
                <p class="desc"> ${bankCallbackType.getTitle()}</p>
            </div>

        </div>
        <div class="btn-container">
            <a href="/m/personal-info" class="btn-confirm">确定</a>
        </div>
    </#if>
<#--银行卡绑定成功-->
    <#if bankCallbackType == 'CARD_BIND'>
        <div class="m-header">绑卡成功</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-success"></div>
                <p class="desc"> ${bankCallbackType.getTitle()}</p>
            </div>

        </div>
        <div class="btn-container">
            <a href="/m/personal-info" class="btn-confirm">确定</a>
        </div>
    </#if>
    <#if bankCallbackType == 'RECHARGE'>
        <div class="m-header">充值成功</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-success"></div>
                <p class="desc"> ${bankCallbackType.getTitle()}</p>
            </div>

        </div>
        <div class="btn-container">
            <a href="/m/account" class="btn-confirm">确定</a>
        </div>

    </#if>
<#--申请提现成功-->
    <#if bankCallbackType == 'WITHDRAW'>
        <div class="m-header">提现成功</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-success"></div>
                <p class="desc"> ${bankCallbackType.getTitle()}</p>
            </div>

        </div>
        <div class="btn-container">
            <a href="/m/account" class="btn-confirm">确定</a>
        </div>
    </#if>

<#--投资成功-->
    <#if bankCallbackType == 'LOAN_INVEST' || bankCallbackType == 'LOAN_FAST_INVEST' || bankCallbackType == 'LOAN_CREDIT_INVEST'>
        <div class="m-header">投资成功</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-success"></div>
                <p class="desc"> ${bankCallbackType.getTitle()}</p>
            </div>

        </div>
        <div class="btn-container">
            <a href="/m/loan-list" class="btn-confirm">确定</a>
        </div>
    </#if>
<#--修改密码成功-->
    <#if bankCallbackType == 'PASSWORD_RESET'>
        <div class="m-header">重置密码成功</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-success"></div>
                <p class="desc"> ${bankCallbackType.getTitle()}</p>
            </div>

        </div>
        <div class="btn-container">
            <a href="/m/account" class="btn-confirm">确定</a>
        </div>
    </#if>

    <div class="contact"><p>客服电话：400-169-1188（服务时间：9:00-20:00）</p></div>
</div>

</@global.main>


