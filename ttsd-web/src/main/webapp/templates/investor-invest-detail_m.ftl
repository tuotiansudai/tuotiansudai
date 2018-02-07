<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.invest_detail}" pageJavascript="${m_js.invest_detail}" title="${invest.loanName}">

<div class="my-account-content amount-detail" id="wrapperOut">
    <div class="m-header"><em class="icon-left" id="goBackIcon"><i></i></em>${invest.loanName}</div>
    <div class="amount-detail-inner">
        <ul class="input-list">
            <li>
                <label>投资金额</label>
                <em>${invest.investAmount/100}元<#if invest.experience>(体验金)</#if></em>
            </li>

            <li>
                <label>预期总收益</label>
                <em>${invest.expectedInterest/100}元</em>
            </li>
            <li>
                <label>已收回款</label>
                <em>${invest.actualInterest/100}元</em>
            </li>
            <li>
                <label>待收回款</label>
                <em>${invest.unPaidRepay/100}元</em>
            </li>
            <li>
                <label>起息日</label>
                <em>${invest.interestBeginDate?string("yyyy/MM/dd")}</em>
            </li>
            <li>
                <label>到期日</label>
                <em>${invest.lastRepayDate?string("yyyy/MM/dd")}</em>
            </li>
        </ul>
        <#if invest.experience && invest.userInvestAmountTotal<100000>
        <dl class="payment-plan experience">
            <dt>回款计划</dt>
            <dd>
                <b>温馨提示：<br/>投资体验项目的收益，用户需投资直投项目累计满1000元即可提现（投资债权转让项目除外）。</b>
            </dd>
        </dl>
        <#elseif invest.loanStatus == 'RAISING' || invest.loanStatus == 'RECHECK' >
        <dl class="payment-plan experience">
            <dt>回款计划</dt>
            <dd>
                <b>温馨提示：<br/>募集完成后才会生成回款计划。</b>
            </dd>
        </dl>
        <#else>
        <dl class="payment-plan">
            <dt>回款计划</dt>
            <#list invest.investRepays as repay>
            <dd>
                <span>${repay.repayDate?string("yyyy/MM/dd")}</span>
                <span>${repay.amount/100}元</span>
                <em class="status">${repay.statusDesc}</em>
            </dd>
            </#list>
        </dl>
        </#if>
        <ul class="input-list">
            <li>
                <label>预期年化收益</label>
                <em>${(invest.baseRate+invest.activityRate)*100}%</em>
            </li>

            <li>
                <label>项目期限</label>
                <em>最长${invest.duration}天</em>
            </li>
            <#if !invest.experience>
            <li>
                <label>所用优惠券</label>
                <em><#list invest.usedCoupons as coupon>
                    ${coupon}
                <#else>无
                </#list></em>
            </li>
            <li>
                <label>会员优惠</label>
                <em>${invest.serviceFeeDesc}</em>
            </li>
            </#if>
            <li>
                <label>购买日期</label>
                <em>${invest.investTime?string("yyyy/MM/dd HH:mm:ss")}</em>
            </li>
            <#if !invest.experience>
            <li class="loan-detail-link" data-url="/m/loan/${invest.loanId?string.computer}">
                <label>产品详情</label>
                <a href="/m/loan/${invest.loanId?string.computer}">
                    <i class="fa fa-angle-right"></i>
                </a>
            </li>
            </#if>
            <#if invest.contractUrl??>
            <li class="invest-contract-link" data-url="${invest.contractUrl}">
                <label>查看合同</label>
                <a href="${invest.contractUrl}">
                    <i class="fa fa-angle-right"></i>
                </a>
            </li>
            </#if>
        </ul>
    </div>
</div>
</@global.main>
