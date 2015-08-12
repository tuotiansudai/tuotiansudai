<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv=″X-UA-Compatible″ content=″IE=edge,chrome=1″/>
    <title>登陆</title>
    <link rel="stylesheet" type="text/css" href="${requestContext.getContextPath()}/style/dest/${css.global}">
    <link rel="stylesheet" type="text/css" href="${requestContext.getContextPath()}/style/dest/${css.login}">

</head>
<body>
<#include "header.ftl" />
<div class="login">
    <div>
        <h3>欢迎登陆拓天速贷</h3>

        <form action="" name="" class="form-login">
            <label>
                <em class="name">账<i></i>号:</em>
                <input class="login-name unlock" type="text" value="" name="user" placeholder="请输入账号/手机号"/>
            </label>
            <label>
                <em class="name">密<i></i>码:</em>
                <input class="password unlock" type="password" value="" name="password" placeholder="请输入密码"/>
            </label>
            <label>
                <em class="name">验证码:</em>
                <input class="captcha unlock" type="text" value="" name="captcha" placeholder="请输入验证码" maxlength="5"/>
                <em class="img-captcha">
                    <img src="/login/captcha" alt=""/>
                </em>
            </label>
            <div class="error"></div>

            <p class="forgot-password">
                <a href="javascript:" class="reg">免费注册</a>
                <a href="javascript:">忘记密码？</a>
            </p>
            <span>
                <a href="javascript:" class="login-now grey" disabled="disabled">立即登录</a>
            </span>

            <b>数据采用256位加密技术，保障您的信息安全！</b>
        </form>
    </div>
</div>

<#include "footer.ftl">
<script src="${requestContext.getContextPath()}/js/dest/${js.config}"></script>
<script src="${requestContext.getContextPath()}/js/libs/require-2.1.20.min.js" defer async="true"
        data-main="${requestContext.getContextPath()}/js/dest/${js.login}"></script>
</body>
</html>