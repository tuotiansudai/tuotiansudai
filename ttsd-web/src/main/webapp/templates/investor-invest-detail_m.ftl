<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.invest_detail}" pageJavascript="${m_js.invest_detail}" title="${investDetail.loanName}">

<div class="my-account-content amount-detail" id="wrapperOut">
    <div class="m-header"><em class="icon-left" id="goBackIcon"><i
            class="fa fa-angle-left"></i></em>${investDetail.loanName}</div>
    <div class="amount-detail-inner">
        <ul class="input-list">
            <li>
                <label>投资金额</label>
                <em>${investDetail.amount/100}元</em>
            </li>

            <li>
                <label>预期总收益</label>
                <em>${investDetail.expectInterest/100}元</em>
            </li>
            <li>
                <label>已收回款</label>
                <em>${investDetail.repayAmount/100}元</em>
            </li>
            <li>
                <label>待收回款</label>
                <em>${investDetail.unRepayAmount/100}元</em>
            </li>
            <li>
                <label>起息日</label>
                <em>${investDetail.interestBeginTime?string("yyyy/MM/dd")}</em>
            </li>
            <li>
                <label>到期日</label>
                <em>${investDetail.deadline?string("yyyy/MM/dd")}</em>
            </li>
        </ul>
        <dl class="payment-plan">
            <dt>回款计划</dt>
            <#list investDetail.repayList as repay>
            <dd>
                <span>${repay.repayDay?string("yyyy/MM/dd")}</span>
                <span>${repay.amount/100}元</span>
                <em class="status">${repay.status.getDescription()}</em>
            </dd>
            </#list>
        </dl>

        <ul class="input-list">
            <li>
                <label>预期年化收益</label>
                <em>${investDetail.annualizedRate*100}%</em>
            </li>

            <li>
                <label>项目期限</label>
                <em>${investDetail.duration}天</em>
            </li>
            <li>
                <label>所用优惠券</label>
                <em> 0.5%加息券</em>
            </li>
            <li>
                <label>会员优惠</label>
                <em><i class="membership-level v0"></i>服务费九折</em>
            </li>
            <li>
                <label>购买日期</label>
                <em>${investDetail.investTime?string("yyyy/MM/dd")}</em>
            </li>
            <li class="loan-detail-link" data-url="/m/loan/${investDetail.loanId?string.computer}">
                <label>产品详情</label>
                <a href="/m/loan/${investDetail.loanId?string.computer}">
                    <i class="fa fa-angle-right"></i>
                </a>
            </li>

            <#if investDetail.contractUrl??>
            <li class="invest-contract-link" data-url="${investDetail.contractUrl}">
                <label>查看合同</label>
                <a href="${investDetail.contractUrl}">
                    <i class="fa fa-angle-right"></i>
                </a>
            </li>
            </#if>
        </ul>
    </div>
</div>
</@global.main>
