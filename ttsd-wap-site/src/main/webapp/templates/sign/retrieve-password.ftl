<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'sign_retrieve' >
<#assign cssName = 'sign_enter_point' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/${jsName}.js"} >
<#assign css = {"${cssName}":"http://localhost:3008/wapSite/js/${cssName}.css"}>

<@global.main pageCss="${css.sign_enter_point}" pageJavascript="${js.sign_retrieve}" title="找回密码">

<div class="sign-container sign-login">
    <form action="/mobile-retrieve-password" method="post" id="inputPasswordForm">

        <input type="hidden" name="mobile" value="${mobile}">
        <input type="hidden" name="captcha" value="${captcha}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input name="password" type="password" id="newPassword" maxlength="20" placeholder="重设登录密码（6-16位字母数字组合）"/>
        <div class="clearfix"></div>
        <button type="submit" class="btn-wap-normal btn-ok-update">确认</button>

    </form>
</div>

</@global.main>

