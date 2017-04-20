<#import "global-mobile.ftl" as global>
<@global.main pageCss="${css.we_start}" pageJavascript="${js.we_start}"  title="拓天财富登录授权" keywords="2323" description="454545">
<div class="weChat-container weChat-register">

    <form id="formRegister" class="form-register">
        <input validate class="captcha" type="text"  placeholder="请输入图形验证码"/>
        <img src="https://tuotiansudai.com/login/captcha??1492589585943" class="image-captcha" id="imageCaptcha"/>

        <input validate class="captcha" type="text"  placeholder="请输入短信验证码"/>
        <input type="button" class="get-captcha" value="获取验证码">

        <input validate class="password" type="password"  placeholder="请设置登录密码"/>

        <div class="error-box">sdsd</div>
        <button type="submit" class="btn-normal">下一步</button>
    </form>
</div>
</@global.main>

