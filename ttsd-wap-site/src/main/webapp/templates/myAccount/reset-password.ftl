<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'edit_payment_password' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.edit_payment_password}" pageJavascript="${js.edit_payment_password}" title="重置支付密码">


<div class="my-account-content reset-payment-password" >
    <h2>请输入身份证后四位</h2>
    <form id="resetPassword">


    <div class="input-box">
        <input type="text" name='num1' maxlength="1">
        <input type="text" name='num2' maxlength="1">
        <input type="text" name='num3' maxlength="1">
        <input type="text" name='num4' maxlength="1">
    </div>

        <a href="#" class="btn-wap-normal next-step" id="sendShortMsg">发送短信修改密码</a>

    </form>

</div>
</@global.main>
