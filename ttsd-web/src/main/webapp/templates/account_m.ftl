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
                <em>待收回款（元）</em>
            </span>
        </div>
        <div class="amount-balance">
            <span class="summary-box">
                <b>${((balance/100)?string(',##0.00'))!}</b>
                <em>可用余额（元）</em>
            </span>
            <span class="summary-box">
                <b>${(((totalIncome)/100)?string(',##0.00'))!}</b>
                <em>累积收益（元）</em>
            </span>
        </div>
    </div>

    <div class="menu-quick">
        <a href="recharge.ftl">充值</a>
        <a href="cash-money.ftl">提现</a>
    </div>

    <ul class="menu-list">
        <li class="top-distance"><a href="experience-amount.ftl">我的体验金<em class="experience-amount">1</em></a> </li>
        <li><a href="/investment/my-invest.ftl">我的投资<i class="fa fa-angle-right"></i></a> </li>
        <li><a href="coupon.ftl">优惠券<i class="fa fa-angle-right"></i></a> </li>
        <li class="top-distance"><a href="personal-profile.ftl">个人资料<i class="fa fa-angle-right"></i></a> </li>
        <li class="top-distance"><a href="settings.ftl">设置<i class="fa fa-angle-right"></i></a> </li>
    </ul>
</div>
</@global.main>
