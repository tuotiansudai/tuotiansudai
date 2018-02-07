<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.my_account}" pageJavascript="${m_js.my_account}" title="我的账户">

<div class="my-account-content account-overview" id="myAccount">
    <div class="account-summary">
        <div class="title">
            <span>我的</span>
        </div>
        <div class="collection">
            <span class="summary-box">
                <b>${(((expectedTotalCorpus+expectedTotalInterest+expectedTotalExtraInterest+expectedExperienceInterest+expectedCouponInterest)/100)?string(',##0.00'))!}</b>
                <em>待收回款 (<i>元</i>)</em>
            </span>
        </div>
        <div class="amount-balance">
            <span class="summary-box">
                <b>${((balance/100)?string(',##0.00'))!}</b>
                <em>可用余额 (<i>元</i>)</em>
            </span>
            <span class="summary-box">
                <b>${(((totalIncome)/100)?string(',##0.00'))!}</b>
                <em>累积收益 (<i>元</i>)</em>
            </span>
        </div>
    </div>

    <div class="menu-quick">
        <#if hasAccount>
            <#if hasBankCard>
        <a href="/m/recharge">充值</a>
        <a href="/m/withdraw">提现</a>
            <#else>
        <a href="/m/bind-card">充值</a>
        <a href="/m/bind-card">提现</a>
            </#if>
        <#else>
        <a href="/m/register/account">充值</a>
        <a href="/m/register/account">提现</a>
        </#if>
    </div>

    <ul class="menu-list">
        <li class="top-distance"><a>我的体验金<em
                class="experience-amount">${((experienceBalance/100)?string.computer)!}</em></a></li>
        <li><a href="/m/investor/invest-list">我的投资<i class="fa fa-angle-right"></i></a></li>
        <li><a href="/m/my-treasure">优惠券<i class="fa fa-angle-right"></i></a></li>
        <li class="top-distance"><a href="/m/personal-info">个人资料<i class="fa fa-angle-right"></i></a></li>
        <li class="top-distance"><a href="/m/settings">设置<i class="fa fa-angle-right"></i></a></li>
    </ul>
</div>

</@global.main>
