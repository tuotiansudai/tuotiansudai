<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="实名验证" pageCss="${css.register}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="register">
    <ul>
        <li class="register-step-one-title register-arrow">1 注册</li>
        <li class="register-step-two-title register-arrow active">2 实名验证</li>
        <li class="register-step-three-title register-arrow">3 充值投资</li>
    </ul>
    <div class="register-step-two register-step active">
        <ol>
            <form class="register-account-form" action="/register/account" method="post">
                <li>
                    <input type="text" name="userName" placeholder="请输入姓名" class="user-name" value = "${(originalFormData.userName)!}" />
                </li>
                <li>
                    <input type="text" name="identityNumber" placeholder="请输入身份证" class="identity-number" value="${(originalFormData.identityNumber)!}"/>
                </li>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <#if success?? && success == false>
                    <div class="register-error">实名认证失败，请检查您提交的信息是否正确！</div>
                </#if>

                <input type="submit" class="register-account" value="下一步"/>
            </form>
        </ol>
    </div>
</div>
<#include "footer.ftl">
<@global.javascript pageJavascript="${js.register_account}">
</@global.javascript>
</body>
</html>