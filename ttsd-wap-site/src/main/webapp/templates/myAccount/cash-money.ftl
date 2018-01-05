<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'cash_money' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.cash_money}" pageJavascript="${js.cash_money}" title="提现">

<div class="my-account-content bank-card-manage" id="cashMoneyConatiner">
    <ul class="bank-list">
        <li>
            <i class="icon-bank"></i>
            <span class="bank-show">
                <em>中国农业银行 </em>
                6212****4017
            </span>
        </li>
    </ul>
    <form action="#" method="post" class="form-cash">
        <div class="int-item">
            <label for="name">提现金额</label>
            <input type="text" name="cashMoney" class="money-item" id="cashMoney" value=""/>
        </div>
    
        <div class="user-money">
            <p>账户可提取现金:<span>350040.28</span>元</p>
            <p>单笔手续费收取2元</p>
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
