<#--jsName = 'sign_login' -->
<#--cssName = 'sign_enter_point' -->

<div class="sign-container sign-login" id="weChatLogin">
    <div class="goBack_wrapper">
        登录
        <a class="go-back-container" id="goBack_applyTransfer" href="javascript:history.go(-1)">
            <span class="go-back"></span>
        </a>
    </div>
    <div class="show-mobile show-mobile-login"></div>
    <form id="formLogin" class="form-login">
        <div class="captcha_container">
            <input validate class="captcha" type="text" name="captcha" autocomplete="off" placeholder="请输入图形验证码" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
            <div class="close_btn"></div>
            <img src="" class="image-captcha" id="imageCaptcha"/>
        </div>
        <div class="password_container">
            <input validate class="password next_input" autocomplete="off" type="password" name="password" placeholder="请输入登录密码" maxlength="20"/>
            <div class="see_password"></div>
            <div class="close_btn" style="top: 1.7rem;"></div>
        </div>
        <input type="hidden" id="username" name="username"/>
        <input type="hidden" name="source" value="WEB"/>
        <input type="hidden" id="redirectBox" name="redirect" value="${redirect!}"/>
        <div class="error-box"></div>
        <button type="submit" class="btn-wap-normal next-step step_two" disabled>登录</button>
        <span class="get-password"> <a href="/m/mobile-retrieve-password">忘记密码?</a></span>
    </form>

</div>
