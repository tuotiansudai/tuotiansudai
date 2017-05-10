<form id="registerForm" class="register-form" action="/register/user" method="post">
    <dl class="register-list">
        <dd>
            <input type="text" validate  name="mobile" placeholder="您的手机号码" id="mobile">
            <span class="error"></span>
        </dd>
        <dd>
            <input class="captcha" type="text" placeholder="图形验证码" maxlength="5"/>
            <img src="https://tuotiansudai.com/login/captcha??1494329145386" class="image-captcha" id="imageCaptcha"/>
            <span class="error"></span>
        </dd>
        <dd>
            <input type="text" validate  name="captcha" placeholder="短信验证码" id="captcha">
            <button type="button" class="get-captcha"  id="getCaptchaBtn">获取验证码</button>
            <span class="error"></span>
        </dd>
        <dd>
            <input validate name="password" type="password" placeholder="密码" maxlength="20"/>
            <span class="error"></span>
        </dd>
        <dd class="tc">
            <button type="submit" class="btn-normal item-submit">立即领取</button>
        </dd>
        <dd class="tc">
            <input type="hidden" name="source" value="WEB">
            <input type="hidden" name="agreement" value="true"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="redirectToAfterRegisterSuccess" value="/we-chat/bind-success?redirect=${redirect!('/')}">
            <span class="agreement">点击立即领取即同意拓天速贷《服务协议》</span>
        </dd>

    </dl>
</form>

<#include 'register-agreement.ftl' />