<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'account_overview' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.account_overview}" pageJavascript="${js.account_overview}" title="账户总览">

<div class="my-account-content account-overview">
    <div class="account-summary">
        <div class="collection">
            <span class="summary-box ">
                <b>8,000,000.00</b>
                <em>待收回款（元）<i class="fa fa-angle-right"></i> </em>
            </span>
        </div>
        <div class="amount-balance">
            <span class="summary-box">
                <b>8,000,000.00</b>
                <em>待收回款（元）<i class="fa fa-angle-right"></i></em>
            </span>
            <span class="summary-box">
                <b>8,000,000.00</b>
                <em>待收回款（元）<i class="fa fa-angle-right"></i></em>
            </span>
        </div>
    </div>

    <div class="menu-quick">
        <a href="#">明细</a>
        <a href="#">充值</a>
        <a href="#">提现</a>
    </div>

    <ul class="menu-list">
        <li><a href="#">我的体验金<em class="experience-amount">6800</em> <i class="fa fa-angle-right"></i></a> </li>
        <li><a href="#">我的投资<i class="fa fa-angle-right"></i></a> </li>
        <li><a href="#">优惠券<i class="fa fa-angle-right"></i></a> </li>
        <li><a href="#">回款日历<i class="fa fa-angle-right"></i></a> </li>
        <li><a href="#">个人资料<i class="fa fa-angle-right"></i></a> </li>
        <li><a href="#">设置<i class="fa fa-angle-right"></i></a> </li>
    </ul>
</div>
</@global.main>
