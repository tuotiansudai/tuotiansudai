<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="finance-manage" sideLab="userInvest" title="用户投资还款计划">

<!-- content area begin -->
<div class="col-md-10">
    <div class="table-responsive">
        <p>
        项目名称：<a href="${webServer}/loan/${loan.id?string.computer}" target="_blank">${loan.name}</a><br>
        项目状态：${loan.status.getDescription()}<br>
        投资金额：${(invest.amount/100)?string.computer}元<br>
        投资状态：${invest.status.getDescription()}
        </p>
    </div>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>期数</th>
                <th>应回款时间</th>
                <th>应回款金额(元)</th>
                <th>应收本金(元)</th>
                <th>应收利息(元)</th>
                <th>利息管理费(元)</th>
                <th>回款时间</th>
                <th>实收金额(元)</th>
                <th>实收利息(元)</th>
                <th>实收罚息(元)</th>
                <th>实扣利息管理费(元)</th>
                <th>状态</th>
            </tr>
            </thead>

            <tbody>
                <#list repayList as repay>
                <tr>
                    <td>${repay.period}</td>
                    <td>${repay.repayDate?date}</td>
                    <td>${repay.amount}</td>
                    <td>${repay.corpus}</td>
                    <td>${repay.expectedInterest}</td>
                    <td>${repay.expectedFee}</td>
                    <td>${(repay.actualRepayDate?datetime)!'--'}</td>
                    <td>${repay.actualAmount!'--'}</td>
                    <td>${repay.actualInterest!'--'}</td>
                    <td>${repay.defaultInterest!'--'}</td>
                    <td>${repay.actualFee!'--'}</td>
                    <td>${repay.status}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <div class="table-responsive">
        <p>应回款金额 = 应收本金 + 应收利息 - 利息管理费</p>
        <p>实收金额 = 应收本金 + 实收利息 + 实收罚息 - 实扣利息管理费</p>
    </div>
    <div class="table-responsive">
        <a href="javascript:history.back();" class="btn btn-sm btn-default btnSearch">返回</a>
    </div>
</div>
<!-- content area end -->
</@global.main>