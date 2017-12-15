<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.loan_detail}" pageJavascript="${m_js.loan_detail}" title="项目详情－转让详情">

<div class="my-account-content experience-amount" id="loanDetail">

    <div class="account-summary">
        <div class="collection">
            <span class="title">
                ${transferApplication.name!}
            </span>
            <span class="summary-box">
                <b>${transferApplication.baseRate!}
                        <#if loanDto.activityRate != '0.0'>
                            <i class="data-extra-rate">+ ${100 * loanDto.activityRate?number}</i>
                        </#if><i>%</i></b>
                <em>预期年化收益</em>
            </span>
        </div>

        <div class="amount-balance transfer-hack">
            <span>剩余期限<br/>${transferApplication.leftDays!}天</span>
            <span>转让价格(元)<br/>${transferApplication.transferAmount!}</span>
        </div>

    </div>

    <#if (transferApplication.transferStatus.name() == "TRANSFERRING")>
    <div class="leave-time">距项目下架时间：${transferApplication.beforeDeadLine}</div>
    <#else>
    <div class="leave-time">${transferApplication.beforeDeadLine}</div>
    </#if>

    <ul class="detail-list">
        <li>
            <label>项目本金</label>
            <span>${transferApplication.investAmount!}元</span>

        </li>
        <li>
            <label>预期收益</label>
            <span>
               ${transferApplication.expecedInterest!}元
            </span>
        </li>
        <li>
            <label>项目到期时间</label>
            <span>${transferApplication.dueDate?string("yyyy-MM-dd")}</span>
        </li>
        <#if (transferApplication.transferStatus.name() == "TRANSFERRING")>
        <li class="repay-plan">
            <label>回款计划</label>
            <span><i class="fa fa-angle-right"></i> </span>
        </li>
        </#if>

        <#if (transferApplication.transferStatus.name() == "SUCCESS")>
        <li class="repay-plan">
            <label>债权承接记录</label>
            <span><i class="fa fa-angle-right"></i> </span>
        </li>
        </#if>
    </ul>

    <div class="history-record">
        <a href="/m/loan/${transferApplication.loanId?string.computer}">查看原始项目</a>
    </div>

    <#if (transferApplication.transferStatus.name() == "SUCCESS")>
        <button class="to-invest-project" type="button" disabled>已转让</button>
    <#elseif (transferApplication.transferStatus.name() == "CANCEL")>
        <button class="to-invest-project" type="button" disabled>已取消</button>
    <#else>
        <button class="to-invest-project" type="button">立即投资</button>
    </#if>

</div>
<#--回款计划-->
<div class="my-account-content amount-detail" id="wrapperOut3" >

    <div class="amount-detail-inner">

        <dl class="payment-plan">
            <dt>
                <span>回款时间</span>
                <span>金额</span>
                <span>还款状态</span>
            </dt>
            <dd>
                <span>2016/10/18</span>
                <span>0.46元</span>
                <em>已完成</em>
            </dd>
            <dd>
                <span>2016/10/18</span>
                <span>0.46元</span>
                <em class="status">待还款</em>
            </dd>
            <dd>
                <span>2016/10/18</span>
                <span>0.46元</span>
                <em class="status">待还款</em>
            </dd>
        </dl>


    </div>
</div>
<#--转让购买详情-->
    <#include 'buy-transfer_m.ftl'>
</@global.main>
