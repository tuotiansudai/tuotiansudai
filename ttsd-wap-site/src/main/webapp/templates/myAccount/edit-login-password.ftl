<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'edit_login_password' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.edit_login_password}" pageJavascript="${js.edit_login_password}" title="修改登录密码">

<div class="my-account-content">

    <form name="changePasswordForm" id="changePasswordForm">
        <ul class="input-list align-flex-start">
            <li>
                <label for="perOldPass">原密码</label> <input type="password" validate id="originalPassword" name="originalPassword" class="input-control" placeholder="请输入密码">
            </li>
            <li>
                <label for="perOldPass">新密码</label> <input type="password" validate id="newPassword" name="newPassword" class="input-control" placeholder="6位至20位，不能全为数字">
            </li>
            <li>
                <label for="perNewPass">再确认</label> <input type="password" validate id="newPasswordConfirm" name="newPasswordConfirm" class="input-control" placeholder="6位至20位，不能全为数字">
            </li>
        </ul>
        <div class="error-box"></div>

        <div class="button-layer">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="status"></div>
            <button type="submit" class="btn-wap-normal next-step" disabled>确认修改</button>
        </div>

    </form>
</div>
</@global.main>
