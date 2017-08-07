<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'exchange_coupon' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.exchange_coupon}" pageJavascript="${js.exchange_coupon}" title="兑换优惠券">


<div class="my-account-content amount-overview" id="couponList">
    <form  method="post" id="bankForm">
    <ul class="input-list align-flex-start">
        <li>
            <input type="text" name="couponNumber" id="couponNumber" value="" placeholder="请输入兑换码">
        </li>
    </ul>
        <input type="hidden" name="bankName" value="">
        <button type="submit" class="btn-wap-normal next-step"  disabled>兑换</button>
        </form>
</div>
</@global.main>
