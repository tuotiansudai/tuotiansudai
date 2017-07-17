<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'cash_money' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.cash_money}" pageJavascript="${js.cash_money}" title="提现记录">

<div class="my-account-content amount-detail" id="cashMoneyRecord">
    <div class="amount-detail-list">
        <div class="box-item">
            <dl>
                <dt>¥2,000.00</dt>
                <dd class="tip-text">申请成功，处理中</dd>
            </dl>
            <dl>
                <dd>订单号 392323554334533</dd>
                <dd>提现时间 2016-07-01 11:34:20 </dd>
            </dl>
        </div>

        <div class="box-item">
            <dl>
                <dt>¥2,000.00</dt>
                <dd class="info-text">提现成功</dd>
            </dl>
            <dl>
                <dd>订单号 392323554334533</dd>
                <dd>提现时间 2016-07-01 11:34:20 </dd>
            </dl>
        </div>

        <div class="box-item">
            <dl>
                <dt>¥2,000.00</dt>
                <dd class="info-text">提现成功</dd>
            </dl>
            <dl>
                <dd>订单号 392323554334533</dd>
                <dd>提现时间 2016-07-01 11:34:20 </dd>
            </dl>
        </div>

    </div>
</div>
</@global.main>
