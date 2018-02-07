<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.loan_detail}" pageJavascript="${m_js.loan_detail}" title="直投借款详情">
<input id="whichPage" type="hidden" data-page="invest">
<div class="my-account-content experience-amount show-page" id="loanDetail" style="display: none">

    <div class="account-summary">
        <em id="iconDetail" class="icon-left"><i></i></em>
        <#if extraLoanRates?? >
            <a href="javascript:void(0);"><i class="icon-help"></i></a>
        </#if>
        <div class="collection">
            <span class="title">
                ${loan.name}
                    <div style="height: 27px;">
                    <#if loan.activity?string("true","false") == "true">
                        <i class="icon-sign">${loan.activityDesc!}</i>
                    </#if>
                    <#if loan.productType != 'EXPERIENCE' && loan.activityType == 'NEWBIE'>
                        <i class="icon-sign">新手专享</i>
                    </#if>
                    <#if loan.extraSource?? && loan.extraSource == "MOBILE">
                        <i class="icon-sign">APP专享</i>
                    </#if>
                </div>

            </span>
            <span class="summary-box">
                <b>
                    <@percentInteger>${loan.baseRate+loan.activityRate}</@percentInteger><@percentFraction>${loan.baseRate+loan.activityRate}</@percentFraction><#if extraLoanRates??>~<@percentInteger>${loan.baseRate+loan.activityRate+loan.maxExtraLoanRate}</@percentInteger><@percentFraction>${loan.baseRate+loan.activityRate+loan.maxExtraLoanRate}</@percentFraction>
                    </#if>
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
                    <li><span class="title left-title">投资金额</span><span class="title">投资奖励</span></li>

                    <#list extraLoanRates.items as extraLoanRate>
                        <li><span class="left-title">投资金额><em class="money">${extraLoanRate.amountLower?string}</em>元</span><span>${extraLoanRate.rate}%</span>
                        </li>
                    </#list>
                </ul>
            </div>
        </#if>
    </div>

    <div class="experience-total">
    <span>


        <b><em class="money">${loan.minInvestAmount}</em>元</b>
        <i>起投金额</i>
    </span>
        <span>
        <b>最长${loan.duration}天</b>
        <i>项目期限</i>
    </span>
        <span>
        <b><em class="money"><@amount>${interestPerTenThousands?string.computer}</@amount></em>元</b>

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
            <span>项目总额：<i><em class="money"><@amount>${loan.loanAmount?string.computer}</@amount></em>元</i></span>
            <span>剩余金额：<i><em class="money"><@amount>${loan.amountNeedRaised?string.computer}</@amount></em>元</i></span>
        </div>
    </div>


    <ul class="detail-list">
        <li>
            <label>投资上限</label>

            <span><em class="money">${loan.maxInvestAmount}</em>元</span>


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
            <label>最后回款</label>
            <span>${(loan.deadline?string("yyyy-MM-dd"))!}</span>
        </li>
        <li>
            <label>借款协议</label>
            <span><a id="agrement" data-url="${commonStaticServer}/images/pdf/loanAgreement-sample.pdf">债权转让协议（样本）</a><em class="pdf"><i
                    class="iconRight" ></i></em></span>

        </li>
        <li id="to_project_detail" style="margin-top: 10px;">
            <label>项目详情</label>
            <span><em class="pdf"><i class="iconRgihtt"></i></em></span>
        </li>
    </ul>

    <#if loan.loanStatus=='RAISING'>
        <button id="toInvest" class="to-invest-project" type="button">立即投资</button>
    <#elseif loan.loanStatus == "PREHEAT">
        <button class="to-invest-project" type="button" disabled>预热中</button>
    <#else>
        <button class="to-invest-project" type="button" disabled>已售罄</button>
    </#if>

</div>
    <#include 'project-detail_m.ftl'>
    <#include 'buy-loan_m.ftl'>

    <#include 'select-coupon_m.ftl'>


</@global.main>
