<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.edit_payment_password}" pageJavascript="${m_js.edit_payment_password}" title="重置支付密码">

<div id="goBack_container">
    <div class="go-back-container">
        <span class="go-back"></span>
    </div>
    重置支付密码
</div>
<div class="my-account-content reset-payment-password" >
    <h2>请输入身份证后四位</h2>
    <form id="resetPassword">
        <div class="input-box">
            <input type="text" name='num1' maxlength="1">
            <input type="text" name='num2' maxlength="1">
            <input type="text" name='num3' maxlength="1">
            <input type="text" name='num4' maxlength="1">
        </div>
        <a class="btn-wap-normal next-step" id="sendShortMsg">发送短信修改密码</a>
    </form>
</div>
</@global.main>