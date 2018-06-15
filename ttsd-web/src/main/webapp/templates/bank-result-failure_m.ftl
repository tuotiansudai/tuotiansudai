<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.bank_callback}" pageJavascript="${m_js.bank_callback}" title="个人资料">

<div class="my-account-content personal-profile">
    <#if bankCallbackType == 'REGISTER'>
        <div class="m-header">开通存管账号</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-failure"></div>
                <p class="desc">实名认证失败</p>
                <p class="reason-error">${message!('业务处理失败')}</p>
            </div>

        </div>

    </#if>
<#--银行卡绑定失败-->
    <#if bankCallbackType == 'CARD_BIND'>
        <div class="m-header">开通存管账号</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-failure"></div>
                <p class="desc">绑卡失败</p>
                <p class="reason-error">${message!('业务处理失败')}</p>
            </div>

        </div>

    </#if>
<#--申请提现失败-->
    <#if bankCallbackType == 'WITHDRAW'>
        <div class="m-header">提现失败</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-failure"></div>
                <p class="desc">提现失败</p>
                <p class="reason-error">${message!('业务处理失败')}</p>
            </div>

        </div>

    </#if>
    <#if bankCallbackType == 'LOAN_INVEST' || bankCallbackType == 'LOAN_FAST_INVEST'>
        <div class="m-header">投资失败</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-failure"></div>
                <p class="desc">投资失败</p>
                <p class="reason-error">${message!('业务处理失败')}</p>
            </div>

        </div>

    </#if>
    <#if bankCallbackType == 'PASSWORD_RESET'>
        <div class="m-header">重置密码失败</div>
        <div class="info-container">
            <div class="status-container">
                <div class="icon-status icon-failure"></div>
                <p class="desc">重置密码失败</p>
                <p class="reason-error">${message!('业务处理失败')}</p>
            </div>

        </div>

    </#if>
<#--失败时重新尝试-->
    <div class="btn-container">
        <form id="retry-form" action="${bankCallbackType.getRetryPath()}" method="${bankCallbackType.getMethod()}">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="submit" class="btn-confirm" style="border: none" value="再次尝试"/>
        </form>
    </div>

    <div class="contact"><p>客服电话：400-169-1188（服务时间：9:00-20:00）</p></div>
</div>

</@global.main>


