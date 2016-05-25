<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.register}" pageJavascript="${js.register_account}" activeLeftNav="" title="拓天速贷-实名验证" >

<div class="register-container page-width">
    <div class="step-register-head tc">
        欢迎注册拓天速贷  <span>已有账号？<a href="#"> 立即登录</a></span>
    </div>
    <div class="clear-blank"></div>
    <div class="register-box">
        <ul class="reg-list tl register-step-two">
            <form class="register-account-form" action="/register/account" method="post">
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
                <input type="submit" class="register-account" value="下一步"/>
            </form>
        </ul>
    </div>
</div>
</@global.main>