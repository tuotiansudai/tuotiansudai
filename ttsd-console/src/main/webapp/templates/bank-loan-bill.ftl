<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="bank-bill.js" headLab="finance-manage" sideLab="loanBill" title="用户联动优势交易流水">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/finance-manage/real-time-status/loan-bill" class="form-inline query-build" method="get">
        <div class="row">
            <div class="form-group">
                <label for="loanId">标的ID</label>
                <input type="text" name="loanId" class="form-control" value="<#if loanId??>${loanId?c}</#if>">
            </div>
            <button class="btn btn-primary search" type="submit">查询</button>
        </div>
    </form>

<#if data??>
    <div class="row">
        <#if data.status>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>订单</th>
                    <th>订单日期</th>
                    <th>交易时间</th>
                    <th>交易金额（元）</th>
                    <th>账户余额（元）</th>
                    <th>冻结余额（元）</th>
                    <th>说明</th>
                    <th>对方账户</th>
                </tr>
                </thead>
                <tbody>
                <#list data.items as item>
                <tr>
                    <td>${item_index + 1}</td>
                    <td>${item.bankOrderNo}</td>
                    <td>${item.bankOrderDate}</td>
                    <td>${item.tradeTime}</td>
                    <td>${item.amount}</td>
                    <td>${item.balance}</td>
                    <td>${item.freezeBalance}</td>
                    <td>${item.remark}</td>
                    <td>${item.toBankUserName!}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            ${data.message}
        </#if>
    </div>
</#if>
</div>
</@global.main>
