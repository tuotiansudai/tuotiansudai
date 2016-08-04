<div class="header-container">
    <div class="header-download">
        <div id="closeDownloadBox" class="icon-close img-close-tip" ></div>
        <div class="img-logo-tip" ></div>
        <span>APP客户端重磅来袭<br/>更便捷更安全</span>
        <a href="#" class="btn-normal fr" id="btnExperience">立即体验</a>
    </div>
    <div class="header page-width">
        <span class="fl service-time">客服电话：400-169-1188<time>（服务时间：9:00－20:00）</time></span>
        <ul class="fr">
            <li class="membership-store">
                <a href="/membership/store">积分商城</a>
            </li>
            <li class="header-membership">
                <a href="/membership">会员中心</a>
            </li>
            <li class="header-activity-center">
                <a href="/activity-center">活动中心</a>
            </li>
            <li class="login-pop-app" id="iphone-app-pop">
                <a href="javascript:" onclick="cnzzPush.trackClick('13顶部导航','手机APP')">手机APP</a>
                <div id="iphone-app-img" class="img-app-pc-top"></div>
            </li>
        <@global.isNotAnonymous>
            <li>
                <a class="personal-info-link" href="${requestContext.getContextPath()}/personal-info"><@global.security.authentication property="principal.mobile" /></a>
            </li>
            <li><a id="logout-link" href="javascript:void(0);" class="logout">退出</a>
                <form id="logout-form" class="logout-form" action="/login/sign-out" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </li>
            <li class="header-message">
                <a href="/message/user-messages">消息
                    <span class="message-badge <@unreadMessageCount><@global.security.authentication property="principal.mobile"/></@unreadMessageCount>">
                        <@unreadMessageCount><@global.security.authentication property="principal.mobile"/></@unreadMessageCount>
                    </span>
                </a>
            </li>
        </@global.isNotAnonymous>

        <@global.isAnonymous>
            <li>
                <a href="/login" onclick="cnzzPush.trackClick('14顶部导航','登录')">登录</a>
            </li>
            <li>
                <a href="<#if channel??>/register/user?channel=${channel}<#else>/register/user</#if>" onclick="cnzzPush.trackClick('15顶部导航','注册')">注册</a>
            </li>
        </@global.isAnonymous>
        </ul>

    </div>
</div>

<#if Session.impersonate?? && Session.impersonate == '1'>
<div style="position: fixed; right: 10px; top: 20px; font-size: 35px; color: red; z-index: 10">模拟登录</div>
<div style="position: fixed; left: 10px; top: 20px; font-size: 35px; color: green; z-index: 10">模拟登录</div>
</#if>
