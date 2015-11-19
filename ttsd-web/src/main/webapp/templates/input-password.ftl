<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.forget_password}" pageJavascript="${js.forget_password}" activeNav="" activeLeftNav="" title="忘记密码">
<div class="main">
    <div class="forget-phone-box">
        <div class="hd">
            通过认证手机找回密码
        </div>
        <form action="/mobile-retrieve-password" method="post">
            <div class="item-block">
                <label for="">新的密码：</label>
                <input type="hidden" name="mobile" value="${mobile}">
                <input type="hidden" name="captcha" value="${captcha}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input class="password-txt jq-ps-1" name="password" type="password" maxlength="20" placeholder="请输入密码"/>
            </div>
            <div class="item-block">
                <label for="">确认密码：</label>
                <input class="password-txt jq-ps-2" type="password" maxlength="20" placeholder="再次输入密码"/>
            </div>
            <div class="item-block">
                <div class="error"></div>
            </div>
            <div class="item-block">
                <button type="submit" class="btn-send-form-second btn-success" >确认</button>
            </div>
        </form>

    </div>
    <p class="txtc tips">找回密码过程中如有问题，请致电拓天速贷客服：400-169-1188 （工作日 9:00-22:00）</p>
</div>
</@global.main>