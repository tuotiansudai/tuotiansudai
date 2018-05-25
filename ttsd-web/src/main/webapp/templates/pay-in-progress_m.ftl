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
</div>

</@global.main>


