<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="账户总览" pageCss="${css.global}"></@global.head>
</head>
<body>
<#include "header.ftl" />
<div class="mainFrame AccountOverview">
    <aside class="menuBox fl">
        <ul class="menu-list">
            <li><a href="javascript:" class="actived">账户总览</a></li>
            <li><a href="javascript:">投资记录</a></li>
            <li><a href="javascript:">债权转让</a></li>
            <li><a href="javascript:">资金管理</a></li>
            <li><a href="javascript:">个人资产</a></li>
            <li><a href="javascript:" >自动投标</a></li>
            <li><a href="javascript:">积分红包</a></li>
            <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>
    <div class="contentContainer fr autoHeight">
        <div class="bRadiusBox spad">
            <img src="/images/sign/profile.jpg" class="fl accountImg" >
            <div class="profileBox">
                <span><em>您好：${loginName!}</em></span>
                <ul class="proList">
                    <li class="fl"><a class="fa fa-envelope-o fa-fw" href="/personal-info"></a></li>
                    <li class="fr"><a class="btn-normal" href="/recharge">充值</a></li>
                    <li class="fr"><a class="btn-primary" href="/withdraw">提现</a></li>
                </ul>
            </div>

        </div>
        <div class="clearBlank"></div>
        <div class="AssetsBox">
            <div class="AssetsReport bRadiusBox fl">
                <h3>资产总额：<span>${(((balance+freeze+collectingPrincipal+collectingInterest)/100)?string('0.00'))!}元</span></h3>
                <div id="ReportShow" style="width:100%; height:115px; "></div>
            </div>
            <div class="AssetsDetail bRadiusBox fr">
                <ul class="DetailList">
                    <li><b>我的余额：</b><span>${((balance/100)?string('0.00'))!}</span>元</li>
                    <li><b>累计收益：</b><span>${(((collectedReward+collectedInterest)/100)?string('0.00'))!}</span>元</li>
                    <li><b>待收本金：</b><span>${((collectingPrincipal/100)?string('0.00'))!}</span>元</li>
                    <li><b>待收利息：</b><span>${((collectingInterest/100)?string('0.00'))!}</span>元</li>
                    <li><b>已收利息：</b><span>${((collectedInterest/100)?string('0.00'))!}</span>元</li>
                    <li><b>冻结金额：</b><span>${((freeze/100)?string('0.00'))!}</span>元</li>
                </ul>
            </div>
        </div>
    <div class="clearBlank"></div>
            <#if successSumRepay??>
            <div class="LastMonth bRadiusBox">
                <ul class="PaymentSwitch">
                    <li class="current"><a href="javascript:void(0);">本月未还款</a></li>
                </ul>
                <table class="table table-striped">
                    <caption>本月未还款总额：￥${((successSumRepay/100)?string('0.00'))!}元 <a href="/loaner/loan-list" class="fr">更多...</a> </caption>
                    <thead>
                        <tr>
                            <th>项目名称</th>
                            <th>年利率</th>
                            <th>贷款周期</th>
                            <th>项目周期</th>
                            <th>预计还款</th>
                            <th>还款日期</th>
                        </tr>
                    </thead>
                    <tbody>
                    <#if repayList??>
                        <#list repayList as repay>
                        <tr>
                            <td><a href="/loan/${repay.loan.id}">${repay.loan.name!}</a></td>
                            <td>${(((repay.loan.baseRate+repay.loan.activityRate)*100)?string('0.00'))!} % </td>
                            <td>${(repay.loan.periods?string('0'))!}个月</td>
                            <td>第${(repay.period?string('0'))!}期/${(repay.loan.periods?string('0'))!}期</td>
                            <td><#if repay.status == 'COMPLETE'>${(((repay.corpus+repay.actualInterest+repay.defaultInterest)/100)?string('0.00'))!}<#else>${(((repay.corpus+repay.expectedInterest+repay.defaultInterest)/100)?string('0.00'))!}</#if>元</td>
                            <td><#if repay.status == 'COMPLETE'>${(repay.actualRepayDate?string('MM月dd日'))!}<#else>${(repay.repayDate?string('MM月dd日'))!}</#if></td>
                        </tr>
                        </#list>
                    <#else>
                        <tr>
                            <td colspan="6" class="tc">暂时没有记录</td>
                        </tr>
                    </#if>
                    </tbody>
                </table>
            </div>
            </#if>
        <div class="clearBlank"></div>
        <div class="tMonthPayment bRadiusBox" id="tMonthBox">

            <ul class="PaymentSwitch">
                <li class="current"><a href="javascript:void(0);"> 本月已收回款</a></li>
                <li><a href="javascript:void(0);">本月待收回款</a></li>
            </ul>
            <table class="table table-striped">
                <caption>本月已收回款总额：￥${((successSumInvestRepay/100)?string('0.00'))!}元 <a href="/investor/invest-list" class="fr">更多...</a> </caption>
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>年利率</th>
                    <th>贷款周期</th>
                    <th>项目周期</th>
                    <th>预计还款</th>
                    <th>还款日期</th>
                </tr>
                </thead>
                <tbody>
                <#if successSumInvestRepayList??>
                <#list successSumInvestRepayList as successSumInvestRepay>
                <tr>
                    <td><a href="/loan/${successSumInvestRepay.loan.id}">${successSumInvestRepay.loan.name!}</a></td>
                    <td>${(((successSumInvestRepay.loan.activityRate+successSumInvestRepay.loan.baseRate)*100)?string('0.00'))!}%</td>
                    <td>${(successSumInvestRepay.loan.periods?string('0'))!}个月</td>
                    <td>第${(successSumInvestRepay.period?string('0'))!}期/${(successSumInvestRepay.loan.periods?string('0'))!}期</td>
                    <td>${(((successSumInvestRepay.corpus+successSumInvestRepay.defaultInterest+successSumInvestRepay.actualInterest-successSumInvestRepay.actualFee)/100)?string('0.00'))!}元</td>
                    <td>${(successSumInvestRepay.actualRepayDate?string('MM月dd日'))!}</td>
                </tr>
                </#list>
                </#if>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="6" class="tc">本月应收本息${(((successSumInvestRepay+notSuccessSumInvestRepay)/100)?string('0.00'))!}元</td>
                </tr>
                </tfoot>
            </table>
            <table class="table table-striped">
                <caption>本月待收回款总额：￥${((notSuccessSumInvestRepay/100)?string('0.00'))!}元<a href="/investor/invest-list" class="fr">更多...</a> </caption>
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>年利率</th>
                    <th>贷款周期</th>
                    <th>项目周期</th>
                    <th>预计还款</th>
                    <th>还款日期</th>
                </tr>
                </thead>
                <tbody>
                <#if notSuccessSumInvestRepayList??>
                <#list notSuccessSumInvestRepayList as notSuccessSumInvestRepay>
                <tr>
                    <td><a href="/loan/${notSuccessSumInvestRepay.loan.id}">${notSuccessSumInvestRepay.loan.name!}</a></td>
                    <td>${(((notSuccessSumInvestRepay.loan.activityRate+notSuccessSumInvestRepay.loan.baseRate)*100)?string('0.00'))!}%</td>
                    <td>${(notSuccessSumInvestRepay.loan.periods?string('0'))!}个月</td>
                    <td>第${(notSuccessSumInvestRepay.period?string('0'))!}期/${(notSuccessSumInvestRepay.loan.periods?string('0'))!}期</td>
                    <td>${(((notSuccessSumInvestRepay.corpus+notSuccessSumInvestRepay.defaultInterest+notSuccessSumInvestRepay.expectedInterest-notSuccessSumInvestRepay.expectedFee)/100)?string('0.00'))!}元</td>
                    <td>${(notSuccessSumInvestRepay.repayDate?string('MM月dd日'))!}</td>
                </tr>
                </#list>
                </#if>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="6" class="tc">本月应收本息${(((successSumInvestRepay+notSuccessSumInvestRepay)/100)?string('0.00'))!}元</td>
                </tr>
                </tfoot>
            </table>
        </div>
        <div class="clearBlank"></div>
        <div class="newProjects bRadiusBox">
        <table class="table">
            <caption>最新投资项目 <a href="/investor/invests" class="fr">更多...</a> </caption>
            <thead>
            <tr>
                <th>交易时间</th>
                <th>交易详情</th>
                <th>交易状态</th>
                <th>下次回款</th>
                <th>我的投资</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#if (latestInvestList?size>0)>
            <#list latestInvestList as latestInvest>
            <tr>
                <td>${(latestInvest.investTime?string('yyyy-MM-dd'))!}</td>
                <td><a href="/loan/${latestInvest.loanId!}">${latestInvest.loanName!}</a></td>
                <td>投资成功</td>
                <td><#if latestInvest.status??>${(latestInvest.repayDate?string('yyyy-MM-dd'))!}/${(((latestInvest.corpus+latestInvest.defaultInterest+latestInvest.expectedInterest-latestInvest.expectedFee)/100)?string('0.00'))!}元<#else>-/-</#if>/2000元</td>
                <td>￥${(latestInvest.investAmount?string('0.00'))!}元</td>
                <td><a href="/contract/investor/${latestInvest.loanId!}">合同</a></td>
            </tr>
            </#list>
            <#else>
            <tr>
                <td colspan="6" class="tc">暂时没有记录</td>
            </tr>
            </#if>
            </tbody>
        </table>
        </div>
    </div>
</div>


<#include "footer.ftl">
<#--<@global.javascript pageJavascript="${js.accountOverview}">-->
<#--</@global.javascript>-->
<script src="${requestContext.getContextPath()}/js/dest/${js.config}"></script>
<script src="${requestContext.getContextPath()}/js/libs/require-2.1.20.min.js"
defer
async="true"
data-main="${requestContext.getContextPath()}/js/accountOverview.js"></script>
<script>
    var pydata = {
        balance:'${((balance/100)?string('0.00'))!}',
        collectingPrincipal:'${((collectingPrincipal/100)?string('0.00'))!}',
        collectingInterest:'${((collectingInterest/100)?string('0.00'))!}'
    };
</script>
</body>
</html>