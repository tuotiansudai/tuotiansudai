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
        <div class="captcha_container">
            <input class="captcha" type="text" name="imageCaptcha" placeholder="请输入图形验证码" maxlength="5"/>
            <div class="close_btn"></div>
            <input type="hidden" name="mobile" class="mobile" value="${mobile!}">
            <img src="/register/user/image-captcha?${.now?long?c}" class="image-captcha" id="imageCaptcha"/>
        </div>
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
                <div class="close_btn close_btn1"></div>
                <div class="get-captcha">
                    <div class="get-captcha-icon"></div>
                    <div class="get-captcha-text">获取短信验证码</div>
                </div>
            </div>
            <button type="button" class="btn-wap-normal next-step register_next_step" disabled>下一步</button>
            <div class="bottom_button_text">
                <div class="bottom_icon"></div>
                CFCA权威认证，保障合同合法性
            </div>
        </div>
        <div class="next_step_container" style="display: none">
            <input validate name="password" type="password" placeholder="6到20位数字和密码组合" maxlength="20"/>
            <div class="close_btn"></div>
            <div class="see_password"></div>
            <span class="invite-mobile closed">邀请人手机号码(可不填写)<i></i></span>
            <input validate type="text" name="referrer" maxlength="11" class="referrer closed" placeholder="邀请人手机号码">
        <#--<div class="error-box"></div>-->

            <button id="submitBtn" type="submit" class="btn-wap-normal next-step register_btn" disabled>注册</button>
            <span class="show-agreement">点击“注册”即视为您同意<span class="serviceAgreement">《拓天速贷服务协议》</span></span>
        </div>
    </form>
    <div id="freeSuccess"  style="display: none">
        <div class="success-info-tip">
            <div class="pop_title">获取语音验证码</div>
            <div class="pop_content">我们将以电话的形式将验证码发送<br/>给您，请注意接听</div>
        </div>
    </div>
</div>

    <#include './register-agreement_m.ftl' />
</@global.main>

