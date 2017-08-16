<div class="register-common-frame" id="registerCommonFrame">
<div class="landing-phone-section-header">
    <h3><span><i class="left-icon"></i>现在就注册吧<i class="right-icon"></i></span></h3>
</div>
<#if !isAppSource>
<div class="landing-top">
    <div class="landing-inner">
        <div class="register-box">
            <form class="register-user-form" action="/register/user" method="post" autocomplete="off" novalidate="novalidate">
                <ul class="reg-list tl register-step-one register-icon-list">
                    <li>
                        <label for="" class="reg-title">手机号:</label>
                        <i class="icon-mobile"></i>
                        <input type="text" id="mobile" name="mobile" class="mobile long" placeholder="手机号"
                               maxlength="11" value="">
                    </li>
                    <li id="mobileErr" class="height"></li>
                    <li>
                        <label for="" class="reg-title">密码:</label>
                        <i class="icon-password"></i>
                        <input type="password" id="password" name="password" placeholder="密码" maxlength="20"
                               class="password long" value="">
                    </li>
                    <li id="passwordErr" class="height"></li>

                    <li class="code">
                        <label for="" class="reg-title">验证码:</label>
                        <i class="icon-img-captcha"></i>
                        <input type="text" id="appCaptcha" name="appCaptcha" placeholder="验证码" maxlength="5"
                               class="appCaptcha" value="">
                        <em class="image-captcha">
                            <img id="image-captcha-image" src="" alt=""/>
                        </em>
                        <span class="img-change">换一张</span>
                    </li>
                    <li id="appCaptchaErr" class="height appCaptchaErr"></li>
                    <li>
                        <label for="captcha" class="reg-title">手机验证码:</label>
                        <i class="icon-captcha"></i>
                            <span class="captcha-tag" id="pcCaptcha">
                                <input type="text" class="captcha" autocomplete="off" name="captcha" id="captcha"
                                       autocorrect="off" autocapitalize="off" placeholder="手机验证码" maxlength="6">
                                <button type="button" class="fetch-captcha btn" disabled="disabled">获取验证码</button>
                            </span>
                    </li>
                    <li id="captchaErr" class="height"></li>
                    <li class="agree-last">
                        <input type="checkbox" name="agreement" id="agreementInput" class="agreement-check" checked>
                        <label for="agreementInput" class="check-label">同意拓天速贷<a href="javascript:void(0);" class="show-agreement">《服务协议》</a></label>
                    </li>
                    <li id="agreementInputErr" class="height hide"></li>
                    <li  class="tc">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" class="register-user btn-normal-invest" value="立即注册">
                    </li>
                    <li class="tc mobile-agreement">
                        <label>点击立即注册即同意拓天速贷<a href="javascript:void(0);" class="show-agreement">《服务协议》</a></label>
                    </li>
                </ul>
            </form>

        </div>
    </div>
</div>

</#if>
<#include 'register-agreement.ftl' />
</div>
