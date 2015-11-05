<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="登录" pageCss="${css.login}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="login">
    <div>
        <h3>欢迎登录拓天速贷</h3>

        <form class="form-login" action="/login-handler" method="post" data-redirect-url="${redirect}">
            <label>
                <em class="name">账<i></i>号:</em>
                <input class="login-name unlock" type="text" value="" name="username" placeholder="请输入账号/手机号"/>
            </label>
            <label>
                <em class="name">密<i></i>码:</em>
                <input class="password unlock" type="password" value="" name="password" placeholder="请输入密码"/>
            </label>
            <label>
                <em class="name">验证码:</em>
                <input class="captcha unlock" type="text" value="" name="captcha" placeholder="请输入验证码" maxlength="5"/>
                <em class="image-captcha">
                    <img src="/login/captcha" alt=""/>
                </em>
            </label>

            <div class="error"></div>

            <p class="forgot-password">
                <a href="/register/user" class="register">免费注册</a>
                <a href="/mobile-retrieve-password">忘记密码？</a>
            </p>
            <span>
                <a href="javascript:" class="login-submit grey">立即登录</a>
            </span>

            <b>数据采用256位加密技术，保障您的信息安全！</b>
        </form>
    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.login}">
</@global.javascript>
</body>
</html>