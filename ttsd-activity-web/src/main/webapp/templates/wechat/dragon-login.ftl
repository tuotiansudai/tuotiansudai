<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.dragon_login}" pageJavascript="${js.dragon_login}"  title="助力好友抢红包" >

<div class="wechat-invite-container" id="wechatInvite">
    <div class="login-form-container">
        <input type="hidden" id="sharerUnique" value="${sharerUnique!}">
        <h3 class="title-item">登录完成后投资红包会直接放到您的账户中哦~</h3>
        <form action="/login" method="post" class="login-item" id="loginForm">
            <div class="model-item">
                <input type="text" name="mobile"  value="" tabindex="1"  class="int-item" maxlength="11" placeholder="请输入您的手机号" validate/>
            </div>

            <div class="model-item">
                <input type="password" name="password"  value="" tabindex="2"  maxlength="20" class="int-item" placeholder="请输入您的密码" validate/>
            </div>

            <div class="model-item">
                <img id="captchaImg" src="" class="captcha-img"/>
                <input type="text" name="captcha" id="captcha" value="" tabindex="3"  maxlength="5" class="int-item" placeholder="请输入图形验证码" validate/>
            </div>
            
            <div class="model-item text-model">
                <a href="/activity/dragon/wechat/toRegister?sharerUnique=${sharerUnique!}" class="fr login-link">注册</a>
            </div>
            <div class="model-item text-model">
                <i class="icon-check active"></i>
                <span class="agree-item">同意拓天速贷<strong class="agree-text">《服务协议》</strong></span>
                <input type="hidden" name="agreement" class="agree-check" value="true" id="agreementLogin"/>
                <input type="hidden" name="source" value="WE_CHAT"/>
            </div>

            <div class="model-item text-model tc">
                <input type="submit" value="登录领取" class="login-btn" id="loginSubmit"/>
            </div>
        </form>
    </div>
</div>
<#include '../module/register-agreement.ftl' />
</@global.main>