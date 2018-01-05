<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'sign_retrieve' >
<#assign cssName = 'sign_enter_point' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/${jsName}.js"} >
<#assign css = {"${cssName}":"http://localhost:3008/wapSite/js/${cssName}.css"}>

<@global.main pageCss="${css.sign_enter_point}" pageJavascript="${js.sign_retrieve}" title="找回密码">

<div class="sign-container sign-login">
 <span class="show-mobile">${mobile}</span>
    <form id="formCaptcha" class="form-captcha">
        <input class="captcha" type="text" name="imageCaptcha" placeholder="请输入图形验证码" maxlength="5"/>
        <input type="hidden" name="mobile" class="mobile" value="${mobile}">
        <img src="/register/user/image-captcha?${.now?long?c}" class="image-captcha" id="imageCaptcha"/>
    </form>

    <form id="retrieveForm" class="form-register" action="/register/user" method="post">
        <input type="hidden" name="mobile" value="${mobile}">
        <input type="hidden" name="openid" value="${openid}">
        <input type="hidden" name="source" value="WE_CHAT">
        <input type="hidden" name="agreement" value="true"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="redirectToAfterRegisterSuccess" value="/bind-success?redirect=${redirect!('/')}">

        <input validate name="captcha" class="short-message-captcha" type="text" placeholder="请输入短信验证码" maxlength="6"/>
        <span class="get-captcha">获取验证码</span>

        <div class="clearfix"></div>
        <button type="submit" class="btn-wap-normal next-step" disabled>确定</button>
    </form>
</div>

</@global.main>

