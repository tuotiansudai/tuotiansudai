<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.wechat_entry_point}" pageJavascript="${js.wechat_register}" title="完善用户信息">
<div class="weChat-container weChat-register" id="weChatRegister">

    <form id="formCaptcha" class="form-captcha">
        <input class="captcha" type="text" name="imageCaptcha" placeholder="请输入图形验证码" maxlength="5"/>
        <input type="hidden" name="mobile" class="mobile" value="${mobile}">
        <img src="/register/user/image-captcha" class="image-captcha" id="imageCaptcha"/>
    </form>

    <form id="formRegister" class="form-register" action="/register/user" method="post">
        <input type="hidden" name="mobile" value="${mobile}">
        <input type="hidden" name="openid" value="${openid}">
        <input type="hidden" name="source" value="WE_CHAT">
        <input type="hidden" name="agreement" value="true"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="redirectToAfterRegisterSuccess" value="/we-chat/bind-success?redirect=${redirect!('/')}">

        <input validate name="captcha" type="text" placeholder="请输入短信验证码"/>
        <input type="button" class="get-captcha" value="获取验证码">

        <input validate name="password" type="password" placeholder="请设置登录密码"/>
        <div class="error-box"></div>

        <button type="submit" class="btn-normal">下一步</button>
    </form>
</div>
</@global.main>

