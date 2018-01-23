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
    <div id="resetPassword">
        <div class="input-box">
            <input type="tel" name='num1' maxlength="1" class="num num1" id="num1">
            <input type="tel" name='num2' maxlength="1" class="num num2 input_border">
            <input type="tel" name='num3' maxlength="1" class="num num3 input_border">
            <input type="tel" name='num4' maxlength="1" class="num num4 input_border">
        </div>
        <a class="btn-wap-normal next-step" id="sendShortMsg">发送短信修改密码</a>
    </div>
</div>
</@global.main>