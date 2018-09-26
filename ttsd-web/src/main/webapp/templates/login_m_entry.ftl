<#--jsName: sign_enter_point-->
<div class="sign-container" id="weChatStartContainer">
    <div class="goBack_wrapper">
        登录/注册
        <a class="go-back-container" id="goBack_login" >
            <span class="go-back"></span>
        </a>
    </div>
    <div class="logo-note" id="logoNote">
        <i class="logo"></i>
       <span>注册即拿6888元体验金+1000元出借红包！</span>
    </div>

    <div id="EntryPointForm">
        <form id="EntryPointFormPageOne" class="form-login">
        <input validate type="text" name="mobile" id="mobileInput" value="${mobile!}" autocomplete="off" placeholder="请输入手机号" maxlength="11" class="telephoneInput"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>

        <div class="close_btn" id="close_btn"></div>

        <div class="captcha_container page-one clearfix">
            <input validate id="captchaPageOneInput" class="captcha captchaPageOne" type="text" name="captchaPageOne" autocomplete="off" placeholder="请输入图形验证码" maxlength="5" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
            <div class="close_btn"></div>
            <img src="" class="image-captcha" id="imageCaptchaPageOne"/>
        </div>
        <div class="show-mobile show-mobile-entry"></div>
        <button type="submit" class="btn-wap-normal next-step step_one" disabled>下一步</button>
        </form>
    </div>

</div>
