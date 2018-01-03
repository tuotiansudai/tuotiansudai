<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.loan_detail}" pageJavascript="${m_js.loan_detail}" title="直投借款详情">

<div class="my-account-content experience-amount show-page" id="loanDetail" style="display: none">

    <div class="account-summary">

        <#if extraLoanRates?? >
            <a href="javascript:void(0);"><i class="icon-help"></i></a>
        </#if>
        <div class="collection">
            <span class="title">
                ${loan.name}<br/>
                    <#if loan.activity?string("true","false") == "true">
                        <i class="icon-sign">${loan.activityDesc!}</i>
                    </#if>
                    <#if loan.extraSource?? && loan.extraSource == "MOBILE">
                        <i class="icon-sign">APP专享</i>
                    </#if>
            </span>
            <span class="summary-box">
                <b>
                    <@percentInteger>${loan.baseRate}</@percentInteger><@percentFraction>${loan.baseRate}</@percentFraction>
                    <i><#if loan.activityRate != 0>
                        +<@percentInteger>${loan.activityRate}</@percentInteger><@percentFraction>${loan.activityRate}</@percentFraction>
                    </#if></i>
                    <i>%</i>
                </b>

                <em>预期年化收益</em>
            </span>
        </div>

        <div class="amount-balance">

            <#if extraLoanRates?? >活动加息 | </#if>
            <#if loan.nonTransferable >
                不支持债权转让
            <#else>
                支持债权转让
            </#if>

        </div>

        <#if extraLoanRates?? >

            <div class="invest-refer-box" style="display: none">
                <i class="fa fa-caret-up"></i>
                <ul>
                    <li><span class="title">投资金额</span><span class="title">投资奖励</span></li>

                    <#list extraLoanRates.items as extraLoanRate>
                        <li><span>投资金额>${extraLoanRate.amountLower?string}元</span><span>${extraLoanRate.rate}%</span>
                        </li>
                    </#list>
                </ul>
            </div>
        </#if>
    </div>

    <div class="experience-total">
    <span>


        <b>${loan.minInvestAmount}元</b>
        <i>起投金额</i>
    </span>
        <span>
        <b>最长${loan.duration}天</b>
        <i>项目期限</i>
    </span>
        <span>
        <b><@amount>${interestPerTenThousands?string.computer}</@amount>元</b>

        <i>最大万元收益</i>
    </span>
    </div>


    <div class="invest-progress-box">

        <div class="progress-bar">
            <div class="process-percent">
                <div class="percent" style="width:${loan.progress?string("0.00")}%">
                </div>
            </div>
        </div>

        <div class="project-info">
            <span>项目总额：<i><@amount>${loan.loanAmount?string.computer}</@amount>元</i></span>
            <span>剩余金额：<i><@amount>${loan.amountNeedRaised?string.computer}</@amount>元</i></span>
        </div>
    </div>


    <ul class="detail-list">
        <li>
            <label>投资上限</label>

            <span>${loan.maxInvestAmount}元</span>


        </li>
        <li>
            <label>计息方式</label>

            <span>${loan.type.interestPointName}</span>
        </li>
        <li>
            <label>还款方式</label>
            <span>${loan.type.repayType}</span>
        </li>
        <li>
            <label>发布日期</label>
            <span>${(loan.fundraisingStartTime?string("yyyy-MM-dd HH:mm:ss"))!}</span>
        </li>
        <li>
            <label>募集期限</label>
            <span>${loan.raisingDays}天</span>
        </li>
        <li>
            <label>借款协议</label>
            <span><a href="${commonStaticServer}/images/pdf/loanAgreement-sample.pdf">借款转让协议（样本）<i
                    class="fa fa-angle-right"></i></a></span>

        </li>
        <li id="to_project_detail">
            <label>项目详情</label>
            <span><i class="fa fa-angle-right"></i></span>
        </li>
    </ul>

    <#if loan.loanStatus='RAISING'>
        <button id="toInvest" class="to-invest-project" type="button">立即投资</button>
    <#else>
        <button class="to-invest-project" type="button" disabled>已售罄</button>
    </#if>

</div>
    <#include 'project-detail_m.ftl'>
    <#include 'buy-loan_m.ftl'>

    <#include 'select-coupon_m.ftl'>


</@global.main>
