<div class="header-container">
    <div class="header-download">
        <img src="${staticServer}/images/icons/close-tip.png" class="icon-close" id="closeDownloadBox">
        <img src="${staticServer}/images/icons/logo-tip.png">
        <span>APP客户端重磅来袭<br/>更便捷更安全</span>
        <a href="#" class="btn-normal fr" id="btnExperience">立即体验</a>
    </div>
    <div class="header page-width">
        <span class="fl service-time">客服电话：400-169-1188<time>（服务时间：9:00－20:00）</time></span>
        <ul class="fr">
            <li class="login-pop-app" id="iphone-app-pop"><a href="javascript:">手机APP</a>
                <img src="${staticServer}/images/sign/app-pc-top.png" alt="随身理财更轻松" id="iphone-app-img"></li>
        <@global.isNotAnonymous>
            <li><a class="personal-info-link" href="${requestContext.getContextPath()}/personal-info"><@global.security.authentication property="principal.username" /></a></li>
            <li><a id="logout-link" href="/logout" class="logout">退出</a>
                <form id="logout-form" class="logout-form" action="/logout" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </li>
        </@global.isNotAnonymous>

        <@global.isAnonymous>
            <li>
                <a href="/login">登录</a>
            </li>
            <li>
                <a href="<#if channel??>/register/user?channel=${channel}<#else>/register/user</#if>">注册</a>
            </li>
        </@global.isAnonymous>
        </ul>

    </div>
</div>

<#if Session.impersonate?exists && Session.impersonate == '1'>
<div style="position: fixed; right: 10px; top: 20px; font-size: 35px; color: red; z-index: 10">模拟登录</div>
<div style="position: fixed; left: 10px; top: 20px; font-size: 35px; color: green; z-index: 10">模拟登录</div>
</#if>
