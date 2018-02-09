<#--jsName: sign_enter_point-->
<div class="sign-container" id="weChatStartContainer">
    <div class="goBack_wrapper">
        登录/注册
        <div class="go-back-container" id="goBack_applyTransfer">
            <span class="go-back"></span>
        </div>
    </div>
    <div class="logo-note" id="logoNote">
        <i class="logo"></i>
       <span>注册即拿6888元体验金+668元现金红包！</span>
    </div>

    <div id="EntryPointForm">
        <input validate type="text" name="mobile" value="${mobile!}" autocomplete="off" placeholder="请输入手机号" maxlength="11" class="telephoneInput"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
        <div class="close_btn" id="close_btn"></div>
        <div class="show-mobile show-mobile-entry"></div>
        <button type="submit" class="btn-wap-normal next-step step_one" disabled>下一步</button>
    </div>

</div>
