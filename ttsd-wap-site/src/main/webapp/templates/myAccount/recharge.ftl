<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'recharge' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.recharge}" pageJavascript="${js.recharge}" title="充值">

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
    <div class="text-item">
        该卡充值单笔限额5万元，今日可充值金额5万元
    </div>
    <form action="#" method="post" class="form-cash">
        <div class="int-item">
            <label for="name">充值金额</label>
            <input type="text" name="cashMoney" class="money-item" id="cashMoney" value="" placeholder="充值最少为1元" />
        </div>
    
        <div class="user-money">
            <p>账户余额:<span>3500</span>元</p>
        </div>
        <button type="submit" class="btn-wap-normal" id="toCash" disabled>确认提交</button>
    </form>
    <a href="cash-money-record.ftl" class="to-record">充值记录</a>
</div>
</@global.main>
