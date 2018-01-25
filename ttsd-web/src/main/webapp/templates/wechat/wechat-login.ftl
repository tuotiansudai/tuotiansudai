<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.wechat_entry_point}" pageJavascript="${js.wechat_login}" title="登录账户">
<div class="weChat-container weChat-login" id="weChatLogin">
    <h2 class="note">您输入的<i class="mobile">${mobile[0..2]}****${mobile[7..]}</i>手机号已经注册速贷账户</h2>

    <form id="formLogin" class="form-login" data-redirect="${redirect!('/')}">
        <input type="hidden" name="source" value="WE_CHAT"/>
        <input type="hidden" name="mobile" value="${mobile}"/>
        <input type="hidden" name="openid" value="${openid}"/>
        <input validate class="password" type="password" name="password" placeholder="请输入登录密码" maxlength="20"/>
        <input validate class="captcha" type="text" name="captcha" placeholder="请输入图形验证码" maxlength="5"/>
        <img src="/login/captcha" class="image-captcha" id="imageCaptcha"/>

        <div class="error-box"></div>
        <button type="submit" class="btn-normal">下一步</button>
        <#--<span class="get-password"> <a href="/mobile-retrieve-password">忘记密码</a></span>-->

    </form>

</div>
</@global.main>
