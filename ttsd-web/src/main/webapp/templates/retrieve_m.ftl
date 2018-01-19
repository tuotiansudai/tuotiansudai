<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.sign_enter_point}" pageJavascript="${m_js.sign_retrieve}" title="找回密码">

<div class="sign-container sign-login">
    <div class="reset_one">
        <div class="goBack_wrapper">
            找回密码
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

        <form id="retrieveForm" class="form-register">
            <input type="hidden" name="mobile" value="${mobile!}">
            <input type="hidden" name="openid" value="${openid!}">
            <input type="hidden" name="source" value="WE_CHAT">
            <input type="hidden" name="agreement" value="true"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="redirectToAfterRegisterSuccess" value="/bind-success?redirect=${redirect!('/')}">

            <div class="captcha_container">
                <input validate name="captcha" class="short-message-captcha" type="text" placeholder="请输入短信验证码" maxlength="6"/>
                <div class="close_btn close_btn1"></div>
                <div class="get-captcha">
                    <div class="get-captcha-icon"></div>
                    <div class="get-captcha-text">获取短信验证码</div>
                </div>
            </div>

            <div class="clearfix"></div>
            <button type="submit" class="btn-wap-normal next-step reset_next_step" disabled>确定</button>
        </form>
    </div>

    <div class="sign-container sign-login reset_two">
        <div class="goBack_wrapper">
            找回密码
            <div class="go-back-container" id="goBack_applyTransfer">
                <span class="go-back"></span>
            </div>
        </div>
        <form id="inputPasswordForm">
            <div class="password_container">
                <div class="see_password"></div>
                <div class="close_btn"></div>
                <input name="password" type="password" id="newPassword" class="password" maxlength="20" placeholder="重设登录密码（6-16位字母数字组合）"/>
            </div>
            <div class="clearfix"></div>
            <button type="submit" class="btn-wap-normal btn-ok-update confirm_reset" disabled>确认</button>
        </form>
    </div>

    <div id="freeSuccess"  style="display: none">
        <div class="success-info-tip">
            <div class="pop_title">获取语音验证码</div>
            <div class="pop_content">我们将以电话的形式将验证码发送<br/>给您，请注意接听</div>
        </div>
    </div>
</div>

</@global.main>



