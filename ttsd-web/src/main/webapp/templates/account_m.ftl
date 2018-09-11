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
                <a href="#" id="noBankCardRecharge">充值</a>
                <a href="#" id="noBankCardWithdraw">提现</a>
                <form id="bindCardForm" action="/m/bank-card/bind/source/M" method="post" style="display: none">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </#if>
        <#else>
            <a href="/m/register/account">充值</a>
            <a href="/m/register/account">提现</a>
        </#if>
    </div>

    <ul class="menu-list">
        <li class="top-distance"><a>我的体验金<em
                class="experience-amount">${((experienceBalance/100)?string.computer)!}元</em></a></li>
        <li><a href="/m/investor/invest-list">我的投资<i class="iconRight"></i></a></li>
        <li class="top-distance"><a href="/m/personal-info">个人资料<i class="iconRight"></i></a></li>
        <li class="top-distance"><a href="/m/settings">设置<i class="iconRight"></i></a></li>
    </ul>
    <@global.role hasRole="'UMP_INVESTOR'">
        <p class="liandong-tip"> 提示：查看联动优势存管账号资金余额，请登录<strong>PC端拓天速贷</strong></p>
    </@global.role>
</div>

</@global.main>
