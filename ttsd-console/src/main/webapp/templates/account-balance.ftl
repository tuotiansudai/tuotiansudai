<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="account-balance.js" headLab="finance-manage" sideLab="userBalance" title="用户余额">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="control-label">用户名：</label>
            <input type="text" class="form-control jq-loginName" name="loginName" value="${loginName!}">
        </div>
        <div class="form-group">
            <label for="control-label">余额：</label>
            <input type="text" class="form-control jq-balance-min" name="balanceMin" value="${balanceMin!50}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control jq-balance-max" name="balanceMax" value="${balanceMax!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <button class="btn btn-primary" type="submit">查询</button>
        <button class="btn btn-default" type="reset">重置</button>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>地区</th>
                <th>最后交易时间</th>
                <th>账户余额(共${sumBalance/100}元)</th>
            </tr>
            </thead>
            <tbody>
                <#list userAccountList as userAccount>
                <tr>
                    <td>${userAccount.loginName!''}
                        <#if userAccount.staff>
                            <span class="glyphicon glyphicon glyphicon-user" aria-hidden="true"></span>
                        </#if>
                    </td>
                    <td>${userAccount.userName!''}</td>
                    <td>${userAccount.mobile}</td>
                    <td>${userAccount.province!''}</td>
                    <td>${(userAccount.lastBillTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${userAccount.balance}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${count}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/finance-manage/account-balance?loginName=${loginName!}&balanceMin=${balanceMin!50}&balanceMax=${balanceMax!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/finance-manage/account-balance?loginName=${loginName!}&balanceMin=${balanceMin!50}&balanceMax=${balanceMax!}&index=${index+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
            <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>
