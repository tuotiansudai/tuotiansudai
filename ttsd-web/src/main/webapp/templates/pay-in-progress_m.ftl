<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.bank_callback}" pageJavascript="${m_js.bank_callback}" title="处理中">

<div class="my-account-content personal-profile loading-page">
    <div class="m-header"><em id="iconRegister" class="icon-left"><i></i></em>正在处理中...</div>
    <div class="gif-container">
        <img id="gifImg" src="" alt="">
    </div>
    <div class="progress-container">
        正在处理中：<span id="secondsCountDown">5</span>
    </div>
    <form id="isPaySuccess" action="${requestContext.getContextPath()}/callback/${bankCallbackType.name()}/order-no/${orderNo}/is-success" method="post" style="display: none">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>

</@global.main>


