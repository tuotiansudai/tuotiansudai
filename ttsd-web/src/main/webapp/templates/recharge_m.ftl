<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.recharge}" pageJavascript="${m_js.recharge}" title="充值">

<div class="my-account-content bank-card-manage" id="cashMoneyConatiner">
    <div class="m-header"><em class="icon-left" id="goBackIcon"><i class="fa fa-angle-left"></i></em>充值</div>
    <#if isBindCard>
    <ul class="bank-list">
        <li>
            <i class="icon-bank bank ${bankCode}" data-bank-code="${bankCode}"></i>
            <span class="bank-show">
                <em>${bank}</em>
                ${bankCard?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}
            </span>
        </li>
    </ul>
        <#if isFastPayOn>
    <div class="text-item">
            <#if bankModel??>
            <#if bankModel.singleAmount gt 1000000>
                <#assign singleAmount = bankModel.singleAmount/1000000+"万">
            <#else>
                <#assign singleAmount = bankModel.singleAmount/100>
            </#if>
            <#if bankModel.singleDayAmount gt 1000000>
                <#assign singleDayAmount = bankModel.singleDayAmount/1000000+"万">
            <#else>
                <#assign singleDayAmount = bankModel.singleDayAmount/100>
            </#if>
                该卡充值单笔限额${singleAmount}元，今日可充值金额${singleDayAmount}元</span></div>
            </#if>
    </div>
    <form action="/recharge" method="post" class="form-cash">
        <div class="int-item">
            <label for="name">充值金额</label>
            <input type="text" class="money-item" id="cashMoney" value="" placeholder="充值金额最少为1元"/>
        </div>

        <div class="user-money">
            <p>账户余额：<span>${balance}</span>元</p>
        </div>
        <input type="hidden" name="bankCode" value="${bankCode}"/>
        <input type="hidden" name="amount" value=""/>
        <input type="hidden" name="source" value="M"/>
        <input type="hidden" name="fastPay" value="true"/>
        <input type="hidden" name="publicPay" value="false"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn-wap-normal" id="toCash" disabled>确认提交</button>
    </form>
        <#else>
    <div class="manage-note">
        <p>手机端仅支持快捷支付</p>
        <p>请前往app开通快捷支付后再进行充值</p>
        <a class="btn-app-link" href="https://tuotiansudai.com/app/download" target="_blank">下载app</a>
    </div>
        </#if>
    </#if>
</div>
</@global.main>
