<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.bank_callback}" pageJavascript="${m_js.bank_callback}" title="个人资料">

<div class="my-account-content personal-profile">
    <div class="m-header"><em id="iconRegister" class="icon-left"><i></i></em>实名认证</div>
    <div class="info-container">
        <div class="status-container">
            <div class="icon-status icon-failure"></div>
            <p class="desc">银行卡绑定失败</p>
            <p class="reason-error">身份证信息和真实姓名不匹配</p>
        </div>

    </div>

<#--失败时重新尝试-->
    <div class="btn-container">
        <a href="/" class="btn-confirm">再次尝试</a>
    </div>
</div>

</@global.main>


