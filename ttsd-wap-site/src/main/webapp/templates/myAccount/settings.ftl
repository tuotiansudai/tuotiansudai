<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'settings' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.settings}" pageJavascript="${js.settings}" title="设置">


<div class="my-account-content" id="settingBox">
    <ul class="input-list">
        <li>
            <label for="perMobile">登录密码</label>
            <a href="edit-login-password.ftl" class="update-login-pwd"> 修改 <i class="fa fa-angle-right"></i></a>

        </li>
        <li>
            <label for="perName">支付密码</label>
            <a href="edit-payment-password.ftl" class="update-payment-pwd">
                修改 <i class="fa fa-angle-right"></i>
            </a>
        </li>
        <li>
            <label for="perCard">免密支付</label>
            <span class="update-payment-nopwd opened" data-firstopen="false">
                <i></i>
            </span>
        </li>
        <li>
            <label for="perName">安心签</label>
            <a href="anxin-electro-sign.ftl">
                <i class="fa fa-angle-right"></i>
            </a>
        </li>
    </ul>

    <button type="button" class="btn-wap-normal next-step" >退出登录</button>
</div>


<#--开启免密投资 如果是第一次，需要有联动优势授权-->
<div id="turnOnNoPassword" class="pad " style="display: none;">
    <form name="turnOnNoPasswordInvestForm" >
        <b class="pop-title">开通免密支付</b>
        <span>理财快人一步，专享快速购买标的的便捷体验</span>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="noPasswordInvest" value="true"/>
    </form>
</div>

<div id="noPasswordInvestDOM" class="pad open-success-result" style="display: none;">
    <p>请在新打开的联动优势页面充值完成后选择：</p>
    <p><a href="/personal-info" class="btn-success" data-category="确认成功" >继续</a>(授权后视情况可能会有一秒或更长的延迟)</p>
    <span>遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-20:00）</span>
</div>


<#--关闭免密投资 需要有短信验证码-->

<div id="turnOffNoPassword" class="tip-to-close pad" style="display: none">
    <em class="icon-notice"></em>
    <span>
            免密支付可以帮助你<br/>
            在投资时快速购买标的；<br/>
            您是否确认关闭免密支付？
        </span>

</div>
<div id="turnOnSendCaptcha" class="tip-to-close send-captcha pad" style="display: none;">
    <b class="pop-title">关闭免密支付</b>
    <p>推荐您开通免密投资功能，简化投资过程，投资快人一步，确认关闭吗？</p>
    <form id="imageCaptchaForm" name="imageCaptchaForm" class="form-captcha" method="post">
        <input type="text" class="image-captcha-text" name="imageCaptcha" maxlength="5" placeholder="请输入图形验证码"/>
        <img src="/no-password-invest/image-captcha" class="image-captcha" id="imageCaptcha"/>
        <input type="hidden" name="mobile" value="${mobile}"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <span class="code-number-hidden">验证码发送到${mobile?replace("^(\\d{3}).*(\\d{4})$","$1****$2","r")}</span>
    <form name="turnOffNoPasswordInvestForm" class="form-captcha" id="turnOffNoPasswordInvestForm">

        <input type="hidden" name="mobile" value="${mobile}"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="text" name="captcha"> <button type="button" class="get-captcha">获取验证码</button>
    </form>
</div>

</@global.main>
