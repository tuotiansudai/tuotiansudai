<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'bank_card_manage' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.bank_card_manage}" pageJavascript="${js.bank_card_manage}" title="选择银行">

<div class="my-account-content bank-card-manage" id="bankCardSelect">
    <div class="info-note">银行卡添加完成后，充值和提现只能使用该银行卡，建议选择充值限额较大的银行</div>
    <ul class="bank-list">
        <li>
            <i class="icon-bank"></i>
            <span class="bank-show">
                <em>中国农业银行 </em>
                单笔限额5万元，单日限额20万元
            </span>
            <i class="icon-radio"></i>
        </li>

        <li>
            <i class="icon-bank"></i>
            <span class="bank-show">
                <em>中国农业银行 </em>
                单笔限额5万元，单日限额20万元
            </span>
            <i class="icon-radio"></i>
        </li>

        <li>
            <i class="icon-bank"></i>
            <span class="bank-show">
               <em>中国农业银行 </em>
                单笔限额5万元，单日限额20万元
            </span>
            <i class="icon-radio"></i>
        </li>

    </ul>

    <div class="info-more-bank">
        更多银行努力接入中…
    </div>

</div>
</@global.main>
