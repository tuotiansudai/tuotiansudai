<#import "wechat-global.ftl" as global>
<@global.main pageCss="${css.wechat_entry_point}" pageJavascript="${js.wechat_entry_point}">
<div class="weChat-container" id="weChatStartContainer">
    <h2 class="note">请填写手机号码，以补全您的拓天速贷账户信息。</h2>

    <form id="weChatEntryPointForm" action="/we-chat/entry-point?redirect=${redirect!('/')}" method="post">
        <input validate type="text" name="mobile" value="${mobile!}" placeholder="请输入手机号" maxlength="11"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="error-box"><#if error??>手机号格式不正确</#if></div>
        <button type="submit" class="btn-normal">下一步</button>
    </form>

    <div class="notice-tip">
        <b>温馨提示:</b>
        <p>若您输入的手机号码已被注册，需输入相应的登录密码补全账户信息；若未被注册需要设置密码补全信息。</p>
    </div>
</div>
</@global.main>
