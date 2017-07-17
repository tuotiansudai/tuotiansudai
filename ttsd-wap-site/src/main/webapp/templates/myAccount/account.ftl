<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'my_account' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.my_account}" pageJavascript="${js.my_account}" title="我的账户">

<div class="my-account-content account-overview" id="myAccount">
    <div class="account-summary">
        <div class="collection">
            <span class="summary-box" data-key="collection">
                <b>8,000,000.00</b>
                <em>待收回款（元）<i class="fa fa-angle-right"></i> </em>
            </span>
        </div>
        <div class="amount-balance">
            <span class="summary-box" data-key="remain">
                <b>8,000,000.00</b>
                <em>待收回款（元）<i class="fa fa-angle-right"></i></em>
            </span>
            <span class="summary-box" data-key="total">
                <b>8,000,000.00</b>
                <em>待收回款（元）<i class="fa fa-angle-right"></i></em>
            </span>
        </div>
    </div>

    <div class="menu-quick">
        <a href="amount-detail.ftl">明细</a>
        <a href="recharge.ftl">充值</a>
        <a href="cash-money.ftl">提现</a>
    </div>

    <ul class="menu-list">
        <li><a href="experience-amount.ftl">我的体验金<em class="experience-amount" <i class="fa fa-angle-right"></i></a> </li>
        <li><a href="/investment/my-invest.ftl">我的投资<i class="fa fa-angle-right"></i></a> </li>
        <li><a href="coupon.ftl">优惠券<i class="fa fa-angle-right"></i></a> </li>
        <li><a href="investment/payment-calendar.ftl">回款日历<i class="fa fa-angle-right"></i></a> </li>
        <li><a href="personal-profile.ftl">个人资料<i class="fa fa-angle-right"></i></a> </li>
        <li><a href="settings.ftl">设置<i class="fa fa-angle-right"></i></a> </li>
    </ul>
</div>
</@global.main>
