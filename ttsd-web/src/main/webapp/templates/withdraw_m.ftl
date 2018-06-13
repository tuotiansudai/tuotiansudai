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
            <p>单笔手续费收取<em id="service_charge">${withdrawFee!}</em>元</p>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn-wap-normal" id="toCash" disabled>确认提交</button>
    </form>
    <dl class="rule-list">
        <dt>温馨提示：</dt>
        <dd>1、<span>工作日16:00</span>前提现，当日到账；</dd>
        <dd>2、<span>工作日16:00</span>后提现第二个工作日到账，节假日则推迟至下一个工作日。</dd>
        <dd>3、若您在<span>周五16:00</span>后或<span>非工作日</span>进行提现，待工作日后方能到账。</dd>
    </dl>
</div>
</@global.main>
