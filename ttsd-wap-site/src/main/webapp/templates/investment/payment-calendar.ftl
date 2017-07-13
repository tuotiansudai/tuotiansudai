<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'payment_calendar' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.payment_calendar}" pageJavascript="${js.payment_calendar}" title="申请转让">

<div class="my-account-content payment-calendar"  id="paymentCalendar">

    <div id="calendarTree"></div>

    <div class="payment-box">
        <span>
            <em>840,031.28 </em>
            9月预计待收款(元)
        </span>
        <span>
            <em>840,031.28 </em>
            9月预计待收款(元)
        </span>
    </div>

    <div class="payment-total">
        <span>2016年9月12</span>
        <em class="key">共12,300.00元</em>
    </div>

    <div class="payment-info">
        <span>
            车辆抵押借款(1/1) <br/>
            回款金额：<em class="key"> 10,000.28元</em>
        </span>
        <span>待回款</span>
    </div>


</div>

</@global.main>
