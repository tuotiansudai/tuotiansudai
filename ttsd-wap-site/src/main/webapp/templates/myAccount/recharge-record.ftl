<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'recharge' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.recharge}" pageJavascript="${js.recharge}" title="充值记录">


<div class="my-account-content amount-detail" id="cashMoneyRecord">
    <div class="amount-detail-list">
        <div class="box-item">
            <dl>
                <dt>¥2,000.00</dt>
                <dd class="tip-text">等待支付</dd>
            </dl>
            <dl>
                <dd>订单号 392323554334533</dd>
                <dd>充值时间 2016-07-01 11:34:20 </dd>
            </dl>
        </div>

        <div class="box-item">
            <dl>
                <dt>¥2,000.00</dt>
                <dd class="info-text">充值成功</dd>
            </dl>
            <dl>
                <dd>订单号 392323554334533</dd>
                <dd>充值时间 2016-07-01 11:34:20 </dd>
            </dl>
        </div>

        <div class="box-item">
            <dl>
                <dt>¥2,000.00</dt>
                <dd class="info-text">充值成功</dd>
            </dl>
            <dl>
                <dd>订单号 392323554334533</dd>
                <dd>充值时间 2016-07-01 11:34:20 </dd>
            </dl>
        </div>

    </div>


</div>
</@global.main>
