<form id="registerForm" class="register-form" action="/register/user" method="post">
    <dl class="register-list">
        <dd>
            <input type="text" validate  name="mobile" placeholder="请输入手机号" id="mobile" maxlength="11">
            <span class="error" style="visibility: visible"></span>
        </dd>
        <dd>
            <input class="image-captcha-text" type="text" placeholder="请输入图形验证码" maxlength="5"/>
            <img src="" class="image-captcha" id="imageUpdate"/>
            <span class="error" style="visibility: visible"></span>
        </dd>
        <dd>
            <input type="text" validate  name="captcha" placeholder="请输入短信验证码" id="captcha maxlength="6">
            <button type="button" class="get-captcha"  id="getCaptchaBtn">获取验证码</button>
            <span class="error" style="visibility: visible"></span>
        </dd>
        <dd>
            <input validate name="password" type="password" placeholder="请输入密码" maxlength="20"/>
            <span class="error" style="visibility: visible"></span>
        </dd>
        <dd class="tc">
            <button type="submit" class="btn-normal item-submit">立即注册</button>
        </dd>
        <dd class="tc">
            <input type="hidden" name="source" value="WEB">
            <input type="hidden" name="agreement" value="true"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="redirectToAfterRegisterSuccess" value="/">

            <span class="agreement" id="agreeRule">点击立即注册即同意拓天速贷《服务协议》</span>
        </dd>

    </dl>
</form>

<#include 'register-agreement.ftl' />