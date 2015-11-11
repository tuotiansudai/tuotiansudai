<div class="header-container">
    <div class="header page-width">
            <span class="fl">客服电话：400-169-1188<time>沟通时间：(9:00-18:00)</time></span>
            <ul class="fr">
                <li><a href="javascript:">帮助中心</a></li>
                <@global.security.authorize access="isAuthenticated()">
                <li><a href="${requestContext.getContextPath()}/user-center"><@global.security.authentication property="principal.username" /></a></li>
                <li><a id="logout-link" href="/logout" class="logout">退出</a>
                    <form id="logout-form" class="logout-form" action="/logout" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                </li>
                </@global.security.authorize>

                <@global.security.authorize access="! isAuthenticated()">
                <li><a href="${requestContext.getContextPath()}/register/user">免费注册</a></li>
                <li><a href="${requestContext.getContextPath()}/login">登录</a></li>
                </@global.security.authorize>
            </ul>

    </div>
</div>
