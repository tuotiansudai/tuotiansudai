<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.settings}" pageJavascript="${m_js.settings}" title="设置">

<div class="goBack_wrapper" id="settingsContainer" data-bankcard="${hasBankCard?c}" data-account="${hasAccount?c}">
    设置
    <div class="go-back-container" id="goBack_applyTransfer">
        <span class="go-back"></span>
    </div>
</div>
<div class="my-account-content" id="settingBox">
    <ul class="input-list">
        <li id="reset-password">
            <label for="perName">支付密码</label>
            <a class="reset-bank-password">
                重置 <i class="icon-more"></i>
            </a>
            <form id="resetPasswordForm" action="/personal-info/reset-bank-password/source/M" method="post" style="display: none">
                <input type="submit" class="update-pwd-btn">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            <form id="bindCardForm" action="/m/bank-card/bind/source/M" method="post" style="display: none">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </li>
        <li id="anxinSign">
            <label for="perName">安心签</label>
            <a><i class="icon-more"></i></a>
        </li>
        <li id="riskEstimate">
            <label for="perName">风险测评</label>
    <#if !estimate??>
        <a>评估一下更了解自己哦！<i class="icon-more"></i></a>
    <#else>
        <a>${estimate.getType()} <i class="icon-more"></i></a>
    </#if>
        </li>
    </ul>

    <button type="button" class="btn-wap-normal next-step" id="logout">退出登录</button>
</div>

</@global.main>
