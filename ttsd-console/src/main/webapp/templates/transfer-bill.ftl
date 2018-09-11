<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="transfer-bill.js" headLab="finance-manage" sideLab="umpTransferBill" title="用户联动优势交易流水">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">用户名或手机号</label>
                <input type="text" class="form-control jq-loginName" value="${loginName!}">
            </div>
            <div class="form-group">
                <label for="control-label">时间</label>

                <div class='input-group date' id="startDate">
                    <input type='text' class="form-control jq-startDate" value="${(startDate?string('yyyy-MM-dd'))!}"/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
                -
                <div class='input-group date' id="endDate">
                    <input type='text' class="form-control jq-endDate" value="${(endDate?string('yyyy-MM-dd'))!}"/>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
            </div>
            <button class="btn btn-primary search" type="button">查询</button>
        </div>

    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>序号</th>
                <th>订单</th>
                <th>交易时间</th>
                <th>交易金额（元）</th>
                <th>账户余额（元）</th>
                <th>交易状态</th>
                <th>交易类型</th>
            </tr>
            </thead>
            <tbody>
                <#list data as row>
                <tr>
                    <td>${row_index + 1}</td>
                    <td>${row[0]}</td>
                    <td>${row[1]}</td>
                    <td>${row[2]}</td>
                    <td>${row[3]}</td>
                    <td>${row[4]}</td>
                    <td>${row[5]}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>
<!-- content area end -->
</@global.main>
