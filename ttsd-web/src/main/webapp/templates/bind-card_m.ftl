<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.bank_card_manage}" pageJavascript="${m_js.bank_card_manage}" title="绑定银行卡">

<div class="my-account-content bank-card-manage" id="bankCardManage">
    <div class="info-note">请绑定持卡人本人的银行储蓄卡</div>
    <form method="post" id="bankForm" action="/bind-card">
        <ul class="input-list align-flex-start">
            <li>
                <label for="perName">持卡人</label>
                <input type="text" name="userName" id="perName" value="${userName}" disabled="disabled">
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
                <input type="number" name="cardNumber" id="cardNumber" placeholder="请输入银行卡号">
                <i></i>
            </li>
        </ul>
        <input type="hidden" name="source" value="M"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn-wap-normal next-step" disabled>确认提交</button>
    </form>

    <div class="tip-user-info" style="display: none">
        <em>温馨提示</em>
        为了您的账户资金安全
        只能绑定持卡人的银行储蓄卡
    </div>
</div>


<div class="my-account-content bank-card-manage" id="bankCardSelect" style="display: none">
    <div class="info-note">银行卡添加完成后，充值和提现只能使用该银行卡，建议选择充值限额较大的银行</div>
    <ul class="bank-list">
        <#list bankList as bank>
            <li>
                <i class="icon-bank ${bank.bankCode}"></i>
                <span class="bank-show">
                    <em>${bank.name}</em>
                    单笔限额${(bank.singleAmount?number)}元，单日限额${(bank.singleDayAmount?number)}元
                </span>
                <i class="icon-radio"></i>
            </li>
        </#list>
    </ul>

    <div class="info-more-bank">
        更多银行努力接入中…
    </div>
</div>
</@global.main>
