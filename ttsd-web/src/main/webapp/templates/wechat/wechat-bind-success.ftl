<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.wechat_entry_point}" pageJavascript="${js.wechat_bind_success}" title="登录成功">
<div class="weChat-container success-box">
    <span class="result-ok">
        <i class="icon-sucess"></i>
        <em>登录成功</em>
    </span>

    <span class="down-time"><i id="downTime" data-redirect="${redirect!('/')}"></i><em id="after">后自动跳转</em></span>
    <button type="button" class="btn-normal" id="okButton">确认</button>
</div>
</@global.main>