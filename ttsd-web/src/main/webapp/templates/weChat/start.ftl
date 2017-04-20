<#import "global-mobile.ftl" as global>
<@global.main pageCss="${css.we_start}" pageJavascript="${js.we_start}"  title="拓天财富登录授权" keywords="2323" description="454545">
<div class="weChat-container" id="weChatStartContainer">
    <h2 class="note">请填写手机号码，以补全您的拓天速贷账户信息。</h2>

    <form id="formStart">
        <input validate  type="text"  name="mobile" placeholder="请输入账号/手机号" maxlength="25"/>
        <div class="error-box"></div>
        <button type="submit" class="btn-normal">下一步</button>
    </form>

    <div class="notice-tip">
        <b>温馨提示:</b>
        <p>若您输入的手机号码已被注册，需输入相应的登录密码补全账户信息；若未被注册需要设置密码补全信息。</p>
    </div>
</div>
</@global.main>
