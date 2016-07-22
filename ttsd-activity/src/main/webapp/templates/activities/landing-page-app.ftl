<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.landing_page}" pageJavascript="${js.landing_page}" activeNav="" activeLeftNav="" title="新手福利_拓天新手投资_拓天速贷" keywords="拓天速贷,新手投资,新手加息券,新手红包" description="拓天速贷是中国P2P互联网金融信息服务平台,为广大投资、贷款的用户提供多元化的投资选择和优质的综合理财服务,新手注册可领取588红包大奖和3%的新手加息券.">

<div class="landing-container-app">
    <div class="register-box">
        <form class="register-user-form" action="/register/user" method="post" autocomplete="off"
              novalidate="novalidate">
            <ul class="reg-list tl register-step-one">
                <li class="bg-col">
                    <input type="text" id="mobile" name="mobile" class="mobile long" placeholder="手机号"
                           maxlength="11" value="">
                </li>
                <li id="mobileErr" class="height"></li>
                <li class="bg-col">
                    <input type="password" id="password" name="password" placeholder="密码" maxlength="20"
                           class="password long" value="">
                </li>
                <li id="passwordErr" class="height"></li>
                <li class="code bg-col">
                    <input type="text" id="appCaptcha" name="" placeholder="验证码" maxlength="5"
                           class="appCaptcha" value="">
                    <em class="image-captcha">
                        <img src="" alt=""/>
                    </em>
                    <span class="img-change">换一张</span>
                </li>
                <li id="appCaptchaErr" class="height appCaptchaErr"></li>
                <li class="bg-col">
                    <i class="sprite-register-ic-captcha"></i>
                            <span class="captcha-tag" id="pcCaptcha">
                                <input type="text" class="captcha" autocomplete="off" name="captcha" id="captcha"
                                       autocorrect="off" autocapitalize="off" placeholder="手机验证码" maxlength="6">
                                <button type="button" class="fetch-captcha btn" disabled="disabled">获取验证码</button>
                            </span>
                </li>
                <li id="captchaErr" class="height"></li>
                <li class="agree-last">
                    <input type="checkbox" name="agreement" id="agreementInput" class="agreement-check" checked>
                    <label for="agreementInput" class="check-label">同意拓天速贷<a href="javascript:void(0);"
                                                                             class="show-agreement">《服务协议》</a></label>
                </li>
                <li id="agreementInputErr" class="height"></li>
                <li class="tc">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="submit" class="register-user" value="立即注册">
                </li>
                <li class="tc mobile-agreement">
                    <label>点击立即注册即同意拓天速贷<a href="javascript:void(0);" class="show-agreement">《服务协议》</a></label>
                </li>
            </ul>
        </form>
    </div>
</div>

</@global.main>