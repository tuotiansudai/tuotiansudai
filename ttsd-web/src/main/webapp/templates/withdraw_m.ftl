<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.withdraw}" pageJavascript="${m_js.withdraw}" title="提现">

<div class="my-account-content bank-card-manage" id="withdrawContainer">
    <ul class="bank-list">
        <li>
            <i class="icon-bank"></i>
            <span class="bank-show">
                <em>${bankName!} </em>
            ${bankCard.cardNumber?replace("^(\\d{4}).*(\\d{4})$","$1****$2","r")}
            </span>
        </li>
    </ul>
    <form action="#" method="post" class="form-cash">
        <div class="int-item">
            <label for="name">提现金额</label>
            <input type="text" name="cashMoney" class="money-item" id="cashMoney" value=""/>
        </div>

        <div class="user-money">
            <p>账户可提取现金:<span>${balance!}</span>元</p>
            <p>单笔手续费收取${withdrawFee!}元</p>
        </div>
        <button type="submit" class="btn-wap-normal" id="toCash" disabled>确认提交</button>
    </form>
    <dl class="rule-list">
        <dt>温馨提示：</dt>
        <dd>1、<span>工作日16:00</span>前提现，当日到账；</dd>
        <dd>2、<span>工作日16:00</span>后提现第二个工作日到账，节假日则推迟至下一个工作日。</dd>
        <dd>3、若您在<span>周五16:00</span>后或<span>非工作日</span>进行提现，待工作日后方能到账。</dd>
    </dl>
    <a href="cash-money-record.ftl" class="to-record">提现记录</a>
</div>
</@global.main>
