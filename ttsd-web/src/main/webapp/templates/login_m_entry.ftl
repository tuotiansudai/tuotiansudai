<#--jsName: sign_enter_point-->
<div class="sign-container" id="weChatStartContainer">
    <div class="logo-note" id="logoNote">
        <i class="logo"></i>
       <span>注册即拿6888元体验金+668元现金红包！</span>
    </div>

    <form id="EntryPointForm" action="/entry-point?redirect=${redirect!('/')}" method="post">
        <input validate type="text" name="mobile" value="${mobile!}" placeholder="请输入手机号" maxlength="11"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <span class="show-mobile"></span>

        <button type="submit" class="btn-wap-normal next-step" disabled>下一步</button>
    </form>

</div>
