<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.register}" pageJavascript="${js.register_account}" activeLeftNav="" title="拓天速贷-实名验证" >

<div class="register-container page-width">
    <ul class="step-register-tab">
        <li class="first"><s></s>1 注册<g></g></li>
        <li class="second on"><s></s>2 实名验证<g></g></li>
        <li class="last"><s></s>3 开始投资<g></g></li>
    </ul>
    <div class="clear-blank"></div>
    <div class="register-box">
        <ul class="reg-list tl register-step-two">
            <form class="register-account-form" action="/register/account?redirect=${redirect}" method="post">
                <li>
                    <input type="text" name="userName" placeholder="请输入姓名" class="user-name" value = "${(originalFormData.userName)!}" />
                </li>
                <li>
                    <input type="text" name="identityNumber" placeholder="请输入身份证" class="identity-number" value="${(originalFormData.identityNumber)!}"/>
                </li>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <#if success?? && !success>
                <label class="error">实名认证失败，请检查您提交的信息是否正确</label>
                </#if>
                <input type="submit" class="register-account" value="下一步" onclick="statisticsCnzzByRegister()"/>
            </form>
        </ul>
    </div>
</div>
</@global.main>