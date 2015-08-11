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
<div class="login"><!--login begin-->
    <div>
        <h3>欢迎登陆拓天速贷</h3>
        <form action="" name="" class="form-login">
            <label><em class="name">账<i></i>号:</em><input class="userName unlock" type="text" value=""
                                                          placeholder="请输入账号/手机号"/> </label>

            <div class="error"></div>
            <label><em class="name">密<i></i>码:</em><input class="userPass unlock" type="text" value="" placeholder="请输入密码"/>
            </label>

            <div class="error"></div>
            <label><em class="name">验证码:</em><input class="yzm jq-yzm unlock" type="text" value="" placeholder="请输入验证码"/><em
                    class="img-yzm"><img
                    src="" alt=""/></em> </label>

            <div class="error"></div>
            <p><input type="checkbox" value=""/>下次自动登录 <a href="javascript:;">忘记密码？</a></p>
            <span><a href="javascript:;" class="login-now grey">立即登录</a><a href="javascript:;" class="reg">免费注册</a></span>
            <b>数据采用256位加密技术，保障您的信息安全！</b>
        </form>
    </div>
</div>

<script>
    //    json 数据 api
    var _API_USER = '../../static/jsons/user.json';
    var _API_YZM = '../../static/jsons/yzm.json';
</script>
<!--login end-->
<#include "footer.ftl">
<script src="${requestContext.getContextPath()}/js/dest/${js.config}"></script>
<script src="${requestContext.getContextPath()}/js/libs/require-2.1.20.min.js" defer async="true"
        data-main="${requestContext.getContextPath()}/js/dest/${js.login}"></script>
</body>
</html>