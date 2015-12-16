<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="debt-repay-plan.js" headLab="finance-manage" sideLab="debtRepay" title="债权还款计划">

<!-- content area begin -->
<div class="col-md-10">

    <div class="row">
        <div class="col-md-7">
            <div class="pull-left currentTab"><span> 债权还款计划（已还款的按照实际还款时间统计，未还款的按照预计还款时间统计。）</span></div></br>
        </div>
        <div class="col-md-5 text-right">
            <form action="" class="form-inline query-build">
                <div class="form-group">

                    <select class="selectpicker status" data-style="btn-default">
                        <option value="" <#if repayStatus??><#else>selected="selected" </#if>>全部</option>
                        <option value="COMPLETE"
                                <#if repayStatus?? && repayStatus=="COMPLETE">selected="selected" </#if>>已还款
                        </option>
                        <option value="REPAYING"
                                <#if repayStatus?? && repayStatus=="REPAYING">selected="selected" </#if>>未还款
                        </option>
                    </select>
                </div>
                <button class="btn btn-primary jq-search" type="button">查询</button>
            </form>
        </div>
    </div>

    <div class="row">
        <table class="table table-bordered table-hover table-center">
            <thead>
            <tr>
                <th>时间</th>
                <th>还款金额</th>
            </tr>
            </thead>
            <tbody>
                <#list debtRepaymentPlans as debtRepaymentPlan>
                <tr>
                    <td><a href="/finance-manage/debt-repayment-detail?date=${debtRepaymentPlan.repayDate!}"
                           class="linked">${debtRepaymentPlan.repayDate!}</a></td>
                    <td>${((debtRepaymentPlan.totalAmount/100)?string('0.00'))!}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>

<!-- content area end -->
</@global.main>