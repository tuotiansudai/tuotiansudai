<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'bank_card_manage' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.bank_card_manage}" pageJavascript="${js.bank_card_manage}" title="开通快捷支付">


<div class="my-account-content bank-card-manage" id="bankCardManage">
    <div class="info-note">请绑定持卡人本人的银行储蓄卡</div>
    <form  method="post" id="bankForm">
    <ul class="input-list align-flex-start">
        <li>
            <label for="perName">持卡人</label>
            <input type="text"  name="userName" id="perName" value="陈一" disabled>
            <i class="icon-notice"></i>
        </li>
        <li class="bank-column">
            <label for="perNum">银行</label>
            <span class="bank-info bank-show key">
                请选择银行
            </span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <label for="cardNumber">银行卡号</label>
            <input type="text" name="cardNumber" id="cardNumber" placeholder="请输入银行卡号">
            <i ></i>
        </li>
    </ul>
        <input type="hidden" name="bankName" value="">
        <button type="submit" class="btn-wap-normal next-step"  disabled>确认提交</button>
        </form>

    <div class="tip-user-info" style="display: none">
        <em>温馨提示</em>
        为了您的账户资金安全
        只能绑定持卡人的银行储蓄卡
    </div>
</div>
</@global.main>
