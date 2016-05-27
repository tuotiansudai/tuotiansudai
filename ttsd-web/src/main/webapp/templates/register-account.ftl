<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.register}" pageJavascript="${js.register_account}" activeLeftNav="" title="拓天速贷-实名验证" >

<div class="register-container page-width register-account">

    <div class="step-account-head">
        <i class="sprite-register-ic-account"></i>
       <span>
           为了保障您的账户及资金安全， <br/>
       根据相关法律法规，我们会对您进行实名认证。
       </span>

    </div>
    <div class="register-box ">
        <form class="register-account-form" action="/register/account" method="post">
            <ul class="reg-list tl register-step-two">
                <li>
                    <label class="title">真实姓名</label>
                    <input type="text" name="userName" placeholder="请输入您的真实姓名"
                           class="user-name" value="${(originalFormData.userName)!}"/>
                </li>
                <li><label class="title">身份证号</label>
                    <input type="text" name="identityNumber" placeholder="请输入您的身份证号码"
                           class="identity-number"
                           value="${(originalFormData.identityNumber)!}"/>
                </li>
            </ul>
            <div class="button-layer">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="status"></div>
                <input type="submit" class="register-account btn-success" value="认证"/>
            </div>
        </form>
    </div>
</div>
</@global.main>