<#import "wechat-global-dev.ftl" as global>

<#assign jsName = 'dragon_register' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/wechat/${jsName}.css"}>


<@global.main pageCss="${css.dragon_register}" pageJavascript="${js.dragon_register}"  title="助力好友抢红包" >

<div class="wechat-invite-container" id="wechatInvite">
    <div class="register-form-container">
        <h3 class="title-item">注册完成后现金红包会直接放到您的账户中哦~</h3>
        <form action="#" method="post" class="register-item" id="registerForm">
            <div class="model-item">
                <input type="text" name="mobile" value="" tabindex="1"  class="int-item" maxlength="11" placeholder="请输入您的手机号" validate/>
            </div>

            <div class="model-item">
                <input type="password" name="password"  value="" tabindex="2"  class="int-item" maxlength="20" placeholder="请输入您的密码" validate/>
            </div>

            <div class="model-item">
                <input type="text" name="captcha" id="captcha" value="" tabindex="3"  class="int-item" maxlength="6" placeholder="请输入短信验证码" validate/>
                <input type="button" class="ignore get-code" value="获取验证码" id="getCaptchaBtn" disabled>
            </div>
            
            <div class="model-item text-model">
                <span class="fr login-link to-login to-link">登录</span>
            </div>
            <div class="model-item text-model">
                <i class="icon-check active"></i>
                <span class="agree-item">同意拓天速贷<strong class="agree-text">《服务协议》</strong></span>
                <input type="hidden" name="agreement" class="agree-check" value="true" validate id="agreementRegister"/>
            </div>

            <div class="model-item text-model tc">
                <input type="submit" value="注册领取" class="register-btn" id="registerSubmit"/>
            </div>
        </form>
    </div>
</div>
<#include '../module/register-agreement.ftl' />
</@global.main>