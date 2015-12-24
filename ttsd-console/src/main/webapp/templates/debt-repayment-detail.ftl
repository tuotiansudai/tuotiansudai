<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="debt-repay-plan.js" headLab="finance-manage" sideLab="debtRepay" title="债权还款计划详情">

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

                    <select class="selectpicker status" data-style="btn-default">
                        <option value="" <#if repayStatus??><#else>selected="selected" </#if>>全部</option>
                        <option value="COMPLETE"
                                <#if repayStatus?? && repayStatus=='COMPLETE'>selected="selected" </#if>>已还款
                        </option>
                        <option value="REPAYING"
                                <#if repayStatus?? && repayStatus=='REPAYING'>selected="selected" </#if>>未还款
                        </option>
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
                    <td><a href="${webServer}/loan/${debtRepaymentPlanDetail.loanId?string('0')}" target="_blank">${debtRepaymentPlanDetail.loanName!}</a></td>
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
</@global.main>