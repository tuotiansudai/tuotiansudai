<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>债权还款计划详情</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap-datepicker.css" rel="stylesheet" />
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/libs/bootstrap-select.css"/>
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" />

    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/index.css" />
<@global.javascript pageJavascript="debt-repay-plan.js"></@global.javascript>

</head>
<body>
<@menu.header label="finaMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="finaMan" sideLab="debtRepay"></@menu.sidebar>
            <!-- content area begin -->

            <div class="col-md-10">

                <div class="row">
                    <div class="col-md-3">
                        <div class="pull-left currentTab"><span> 债权还款计划</span></div>
                    </div>
                    <div class="col-md-9 text-right">
                        <form action="" class="form-inline query-build">
                            <input type="hidden" class="date" value="${date!}">
                            <div class="form-group">

                                <select class="selectpicker status"  data-style="btn-default" >
                                    <option value="" <#if repayStatus??><#else>selected="selected" </#if>>全部</option>
                                    <option value="COMPLETE" <#if repayStatus?? && repayStatus=='COMPLETE'>selected="selected" </#if>>已还款</option>
                                    <option value="REPAYING" <#if repayStatus?? && repayStatus=='REPAYING'>selected="selected" </#if>>未还款</option>
                                </select>
                            </div>
                            <button class="btn btn-primary jq-search-detail" type="button">查询</button>
                        </form>
                    </div>
                </div>

                <div class="row">
                    <table class="table table-bordered table-hover table-center">
                        <thead>
                        <tr>
                            <th>应还日期</th>
                            <th>还款金额</th>
                            <th>标的名称</th>
                            <th>代理人</th>
                            <th>还款状态</th>
                            <th>还款时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list debtRepaymentPlanDetails as debtRepaymentPlanDetail>
                            <tr>
                                <td>${(debtRepaymentPlanDetail.expertRepayDate?string('yyyy-MM-dd'))!}</td>
                                <td>${((debtRepaymentPlanDetail.repayAmount/100)?string('0.00'))!}</td>
                                <td>${debtRepaymentPlanDetail.loanName!}</td>
                                <td>${debtRepaymentPlanDetail.loginName!}</td>
                                <#if debtRepaymentPlanDetail.status == 'COMPLETE'>
                                    <td>还款完成</td>
                                <#else>
                                    <td class="badgeCol">尚未还款</td>
                                </#if>
                                <#if debtRepaymentPlanDetail.actualRepayDate??>
                                    <td>${(debtRepaymentPlanDetail.actualRepayDate?string('yyyy-MM-dd'))!}</td>
                                <#else>
                                    <td>---</td>
                                </#if>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

            </div>

            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->
</body>
</html>