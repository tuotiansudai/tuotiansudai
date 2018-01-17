<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.sign_enter_point}" pageJavascript="${m_js.sign_register}" title="注册">
<div class="sign-container sign-login" id="weChatRegister">
<div class="goBack_wrapper">
    注册
    <div class="go-back-container" id="goBack_applyTransfer">
        <span class="go-back"></span>
    </div>
</div>
 <div class="show-mobile show-mobile-register"></div>
    <form id="formCaptcha" class="form-captcha">
        <input class="captcha" type="text" name="imageCaptcha" placeholder="请输入图形验证码" maxlength="5"/>
        <input type="hidden" name="mobile" class="mobile" value="${mobile!}">
        <img src="/register/user/image-captcha?${.now?long?c}" class="image-captcha" id="imageCaptcha"/>
    </form>

    <form id="formRegister" class="form-register">
        <input type="hidden" name="mobile" value="${mobile!}" class="mobile">
        <input type="hidden" name="openid" value="${openid!}">
        <input type="hidden" name="source" value="WE_CHAT">
        <input type="hidden" name="agreement" value="true"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="redirectToAfterRegisterSuccess" value="/we-chat/bind-success?redirect=${redirect!('/')}">
        <div class="step_container">
            <div class="captcha_container">
                <input validate name="captcha" class="short-message-captcha" type="text" placeholder="请输入短信验证码" maxlength="6"/>
                <button class="get-captcha">获取验证码</button>
            </div>
            <button type="button" class="btn-wap-normal next-step register_next_step" disabled>下一步</button>
            <div class="bottom_button_text">
                <div class="bottom_icon"></div>
                CFCA权威认证，保障合同合法性
            </div>
        </div>
        <div class="next_step_container" style="display: none">
            <input validate name="password" type="password" placeholder="请设置登录密码" maxlength="20"/>

            <span class="invite-mobile closed">邀请人手机号码(可不填写)<i></i></span>
            <input validate type="text" name="referrer" maxlength="25" class="referrer closed" placeholder="邀请人手机号码">
        <#--<div class="error-box"></div>-->

            <button id="submitBtn" type="submit" class="btn-wap-normal next-step register_btn" disabled>注册</button>
            <span class="show-agreement">点击“注册”即视为您同意《拓天速贷服务协议》</span>
        </div>
    </form>
</div>

    <#--<#include '../module/register-agreement.ftl' />-->
</@global.main>

