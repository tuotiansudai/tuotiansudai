<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.register_account}" pageJavascript="${m_js.register_account}" title="实名认证">

<div class="my-account-content certification-box" id="certificationBox">

    <h3>完成实名认证，即可开通联动优势个人资金管理账户资金安全有保障</h3>

    <form class="register-account-form" id="registerAccountForm" method="post">
        <ul class="input-list">
            <li>
                <label for="perName">真实姓名</label>
                <input type="text" name="userName" id="perName"
                       placeholder="请输入您的真实姓名">
            </li>
            <li>
                <label for="perNum">身份证号</label>
                <input type="text" name="identityNumber" id="perNum"
                       placeholder="请输入您的身份证号">
            </li>
        </ul>
        <div class="error-box"></div>

        <div class="button-layer">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="submit" class="btn-wap-normal next-step" value="立即认证" disabled/>
        </div>
    </form>
</div>
</@global.main>
