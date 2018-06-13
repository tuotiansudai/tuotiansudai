<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.register_account}" pageJavascript="${m_js.register_account}" title="实名认证">

<div class="my-account-content certification-box" id="certificationBox">
    <div class="m-header"><em id="iconRegister" class="icon-left"><i></i></em>开通存管账号</div>

    <h3>完成实名认证，即可开通富滇银行个人资金管理账户。<br/>
        资金安全有保障！</h3>

    <form class="register-account-form" action="/m/register/account" id="registerAccountForm" method="post">
        <ul class="input-list">
            <li>
                <label for="perName">真实姓名</label>
                <input type="text" name="userName" id="perName"
                       placeholder="请输入您的真实姓名" maxlength="25">
            </li>
            <li>
                <label for="perNum">身份证号</label>
                <input type="text" name="identityNumber" id="perNum" autocomplete="off"
                       placeholder="请输入您的身份证号" maxlength="20">
            </li>
        </ul>
        <div class="error-box"></div>

        <div class="button-layer">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input name="source" type="hidden" value="M"/>
            <input type="submit" id="accountBtn" class="btn-wap-normal next-step" value="立即认证" disabled/>
        </div>
    </form>

</div>
</@global.main>
