<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'sign_login' >
<#assign cssName = 'sign_enter_point' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/${jsName}.js"} >
<#assign css = {"${cssName}":"http://localhost:3008/wapSite/js/${cssName}.css"}>

<@global.main pageCss="${css.sign_enter_point}" pageJavascript="${js.sign_login}" title="登录账户">
<div class="sign-container sign-login" id="weChatLogin">
    <span class="show-mobile">
        ${mobile}
        18510238729</span>
    <form id="formLogin" class="form-login" data-redirect="${redirect!('/')}">
        <input type="hidden" name="source" value="WE_CHAT"/>
        <input type="hidden" name="mobile" value="${mobile}"/>
        <input type="hidden" name="openid" value="${openid}"/>
        <input validate class="captcha" type="text" name="captcha" placeholder="请输入图形验证码" maxlength="5"/>
        <img src="https://tuotiansudai.com/login/captcha??1498029727557" class="image-captcha" id="imageCaptcha"/>

        <input validate class="password" type="password" name="password" placeholder="请输入登录密码" maxlength="20"/>

        <div class="error-box"></div>
        <button type="submit" class="btn-wap-normal next-step" disabled>下一步</button>
        <span class="get-password"> <a href="/mobile-retrieve-password">忘记密码</a></span>

    </form>

</div>
</@global.main>
