<#import "global-mobile.ftl" as global>
<@global.main pageCss="${css.we_start}" pageJavascript="${js.we_register}"  title="拓天财富登录授权" keywords="2323" description="454545">
<div class="weChat-container weChat-register" id="weChatRegister">

    <form id="formCaptcha" class="form-captcha">
        <input class="captcha" type="text" name="imageCaptcha" placeholder="请输入图形验证码" maxlength="5"/>
        <input type="hidden" name="mobile" class="mobile">
        <img src="#" class="image-captcha" id="imageCaptcha"/>
     </form>

    <form id="formRegister" class="form-register">
        <input type="hidden" name="mobile" value="" class="mobile">
        <input validate name="captcha" type="text"  placeholder="请输入短信验证码"/>
        <input type="button" class="get-captcha" value="获取验证码" >

        <input validate name="password" type="password"  placeholder="请设置登录密码"/>
        <input type="hidden" name="redirectToAfterRegisterSuccess" value="/we-chat/entry-point/login-success">
        <div class="error-box"></div>
        <button type="submit" class="btn-normal">下一步</button>
    </form>
</div>
</@global.main>

