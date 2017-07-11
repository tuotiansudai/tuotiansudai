<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'open_quick_pay' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.open_quick_pay}" pageJavascript="${js.open_quick_pay}" title="绑卡未开通快捷支付">

<div class="my-account-content bank-card-manage" id="bankCardSelect">
    <ul class="bank-list">
        <li>
            <i class="icon-bank"></i>
            <span class="bank-show">
                <em>中国农业银行 </em>
                6212****4017
            </span>
        </li>
    </ul>
    <p class="tip-text">单笔限额5万元，单日限额5万元</p>
    <p class="tip-info">手机端仅支持快捷支付，请开通快捷支付进行充值</p>
    <a href="quick-pay-manage.ftl" class="btn-wap-normal to-open">开通快捷支付</a>
    
</div>
</@global.main>
