<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'edit_payment_password' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.edit_payment_password}" pageJavascript="${js.edit_payment_password}" title="修改支付密码">

<div class="my-account-content edit-payment-password">


    <form class="register-account-form" id="editPasswordForm"  method="post">
        <ul class="input-list align-flex-start">
            <li>
                <label for="perOldPass">原密码</label> <input type="password"  name="oldPassword" id="perOldPass" placeholder="请输入原密码">
            </li>
            <li>
                <label for="perNewPass">新确认</label> <input type="password" name="newPassword" id="perNewPass" placeholder="请输入新密码">
            </li>
        </ul>
        <div class="forget-password">
            <a href="#"> 忘记原密码？</a>
        </div>
        <div class="error-box"></div>

        <div class="button-layer">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="status"></div>
            <input type="submit" class="btn-wap-normal next-step" value="发送短信修改密码" disabled/>
        </div>

    <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
    <#--<button type="submit" class="btn-wap-normal next-step" disabled="">认证</button>-->
    </form>
</div>
</@global.main>
