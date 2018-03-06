<#import "../macro/global.ftl" as global>
<div class="header-container">
    <div class="header-download">
        <div id="closeDownloadBox" class="icon-close img-close-tip" ></div>
        <div class="img-logo-tip sprite-global-logo-tip fl" ></div>
        <span>APP客户端重磅来袭<br/>更便捷更安全</span>
        <a href="#" class="btn-normal fr" id="btnExperience">立即体验</a>
    </div>
    <div class="header-top page-width">
        <span class="fl service-time">客服电话：400-169-1188<time>（服务时间：9:00－20:00）</time></span>
        <ul class="fr">
            <li class="membership-store">
                <a href="${webServer}/point-shop" target="_blank">积分商城</a>
            </li>
            <li class="header-membership">
                <a href="${webServer}/membership">会员中心</a>
            </li>
            <li class="header-activity-center">
                <a href="${webServer}/activity/activity-center">活动中心</a>
            </li>
            <li class="header-help-center">
                <a href="${webServer}/help/help-center">帮助中心</a>
            </li>
        <@global.isNotAnonymous>
            <li><a class="personal-info-link" href="${webServer}/personal-info"><@global.security.authentication property="principal.mobile"/></a></li>
            <li><a id="logout-link" href="javascript:void(0);" class="logout">退出</a>
                <form id="logout-form" class="logout-form" action="${webServer}/logout" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </li>
        </@global.isNotAnonymous>

        <@global.isAnonymous>
            <#if Session.weChatUserLoginName??>
                <li>
                    <a class="personal-info-link" href="${webServer}/personal-info">wexin-${Session.weChatUserLoginName}</a>
                </li>
            <#else>
                <li>
                    <a href="${webServer}/login?redirect=${askServer}" >登录</a>
                </li>
                <li>
                    <a href="${webServer}/register/user<#if channel??>?channel=${channel}</#if>">注册</a>
                </li>
            </#if>
        </@global.isAnonymous>
        </ul>

    </div>
</div>

<#if Session.impersonate?exists && Session.impersonate == '1'>
<div style="position: fixed; right: 10px; top: 20px; font-size: 35px; color: red; z-index: 10">模拟登录</div>
<div style="position: fixed; left: 10px; top: 20px; font-size: 35px; color: green; z-index: 10">模拟登录</div>
</#if>
