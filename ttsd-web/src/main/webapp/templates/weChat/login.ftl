<#import "global-mobile.ftl" as global>
<@global.main pageCss="${css.we_start}" pageJavascript="${js.we_login}"  title="拓天财富登录授权" keywords="2323" description="454545">
<div class="weChat-container weChat-login" id="weChatLogin">
    <h2 class="note">您输入的<i class="mobile"></i>手机号已经注册速贷账户</h2>
    <form id="formLogin" class="form-login">
        <input type="hidden" name="username" value="">
        <input type="hidden" name="source" value="WE_CHAT" />
        <input validate class="password" type="password" name="password" placeholder="请输入登录密码"/>
        <input validate class="captcha" type="text" name="captcha" placeholder="请输入图形验证码"/>
        <img src="/login/captcha" class="image-captcha" id="imageCaptcha"/>

        <div class="error-box"></div>
        <button type="submit" class="btn-normal">下一步</button>
    </form>
</div>
</@global.main>
