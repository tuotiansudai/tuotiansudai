<#--jsName = 'sign_register'-->
<#--cssName = 'sign_enter_point'-->
<div class="sign-container sign-login" id="weChatRegister">
 <span class="show-mobile">
        ${mobile!}
            18510238729</span>
    <form id="formCaptcha" class="form-captcha">
        <input class="captcha" type="text" name="imageCaptcha" placeholder="请输入图形验证码" maxlength="5"/>
        <input type="hidden" name="mobile" class="mobile" value="${mobile!}">
        <img src="/register/user/image-captcha" class="image-captcha" id="imageCaptcha"/>
    </form>

    <form id="formRegister" class="form-register" action="/register/user" method="post">
        <input type="hidden" name="mobile" value="${mobile!}">
        <input type="hidden" name="openid" value="${openid!}">
        <input type="hidden" name="source" value="WE_CHAT">
        <input type="hidden" name="agreement" value="true"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="redirectToAfterRegisterSuccess" value="/we-chat/bind-success?redirect=${redirect!('/m/')}">

        <input validate name="captcha" class="short-message-captcha" type="text" placeholder="请输入短信验证码" maxlength="6"/>
        <span class="get-captcha">获取验证码</span>

        <input validate name="password" type="password" placeholder="请设置登录密码" maxlength="20"/>

        <span class="invite-mobile closed">邀请人手机号码(可不填写)<i></i></span>
        <input validate type="text" name="referrer" maxlength="25" class="referrer closed" placeholder="邀请人手机号码">

        <button type="submit" class="btn-wap-normal next-step" disabled>注册</button>
        <span class="show-agreement">点击“注册”即视为您同意《拓天速贷服务协议》</span>
    </form>
</div>
