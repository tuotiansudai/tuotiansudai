<div class="browser-warning-dialog"></div>
<div class="browser-warning">
    <p>警告！</p>
    <span>您的浏览器版本过低，请用IE9以上版本浏览器浏览本网站！</span>
</div>
<div class="header">
    <div class="top">
        <div>
            <p>客服电话：400-169-1188<span>沟通时间：(9:00-18:00)</span></p>
            <ul>
                <li><a href="javascript:">帮助中心</a></li>
            <@global.security.authorize access="isAuthenticated()">
                <li><a href="/user-center"><@global.security.authentication property="principal.username" /></a></li>
                <li><a class="logout" href="/logout">退出</a>

                    <form class="logout-form" action="/logout" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                </li>
            </@global.security.authorize>

            <@global.security.authorize access="! isAuthenticated()">
                <li><a href="/register/user">免费注册</a></li>
                <li><a href="/login">登录</a></li>
            </@global.security.authorize>
            </ul>
        </div>
    </div>
    <div class="nav">
        <div class="nav-main">
            <a href="#" class="logo">
                <img src="${requestContext.getContextPath()}/images/logo.png" alt="">
            </a>
        </div>
    </div>
</div>
