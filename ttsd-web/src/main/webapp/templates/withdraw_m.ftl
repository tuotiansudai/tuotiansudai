<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.withdraw}" pageJavascript="${m_js.withdraw}" title="提现">

<div class="my-account-content bank-card-manage" id="withdrawContainer">
    <div class="m-header"><em id="iconBack" class="icon-left"><i></i></em>提现 </div>
    <ul class="bank-list">
        <li>
            <i class="icon-bank BANK-${bankCard.bankCode}"></i>
            <span class="bank-show">
                <em>${bankCard.bank!} </em>
            ${bankCard.cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}
            </span>
        </li>
    </ul>
    <form action="/m/withdraw" id="cashForm" method="post" class="form-cash">
        <div class="int-item">
            <label for="name">提现金额</label>
            <input type="text" name="amount" class="money-item" id="amount" value="" autocomplete="off"/>
            <input name="source" type="hidden" value="M"/>
        </div>

        <div class="user-money">
            <p>账户可提取现金:<span id="cash_money">${balance!}</span>元</p>
            <p>单笔手续费收取<em id="service_charge" data-fudianbank="${isFudianBank?c}"></em>元</p>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn-wap-normal" id="toCash" disabled>确认提交</button>
    </form>
    <dl class="rule-list">
        <dt>温馨提示：</dt>
        <dd>1、5W及以下，任何时间都可以申请提现，实时到账；</dd>
        <dd>2、5W以上，<span>工作日8:30-17:00</span>，实时到账；其他时间不允许提现；</dd>
        <dd>3、当日充值的金额只能下一个工作日12:00之后才能提现；</dd>
        <dd>4、提现手续费收取标准：5万元（含5万）以下1.5元／笔，5万以上5元／笔。</dd>
    </dl>
</div>
</@global.main>
